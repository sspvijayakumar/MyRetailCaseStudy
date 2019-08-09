package com.target.myretail.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.target.myretail.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.target.myretail.entity.Price;
import com.target.myretail.entity.Product;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductController productControllerMock;

    @Test
    public void TestCase1_getProductByIdTest() throws Exception {
        Product productFinal = new Product();
        productFinal.setId(12);
        given(productControllerMock.getProductById(productFinal.getId())).willReturn(productFinal);
        mockMvc.perform(get("/products/" + productFinal.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(productFinal.getId())));
    }

    @Test
    public void TestCase2_updateProductPriceByIdTest() throws Exception {
        Product productFinal = new Product();
        productFinal.setId(13860428);
        productFinal.setCurrent_price(new Price(163.86F, "USD"));

        ObjectMapper objectMapper = new ObjectMapper();
        String inputJson = objectMapper.writeValueAsString(productFinal);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/products/" + productFinal.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int responseStatus = mvcResult.getResponse().getStatus();
        assertEquals(200, responseStatus);
    }

}
