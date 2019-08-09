package com.target.myretail.service;

import com.target.myretail.entity.Price;
import com.target.myretail.entity.Product;
import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.model.ProductDetail;
import com.target.myretail.repository.ProductDetailRepository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;


@RunWith(SpringRunner.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService productMockService;

    @Mock
    private ProductDetailRepository productMockDetailRepository;

    @Mock
    private TargetExternalService targetMockExternalService;

    @Test
    public void TestCase1_getProductByIdTest() {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct_id(13860428);
        productDetail.setPrice(163.86F);
        productDetail.setCurrency_code("USD");

        Product expectedProduct = new Product();
        expectedProduct.setId(productDetail.getProduct_id());
        expectedProduct.setName("Product name");
        expectedProduct.setCurrent_price(new Price(productDetail.getPrice(), productDetail.getCurrency_code()));

        Mockito.doReturn(productDetail).when(productMockDetailRepository).getProductDetail(productDetail.getProduct_id());
        Mockito.doReturn("Product name").when(targetMockExternalService).getProductName(productDetail.getProduct_id().toString());

        Product outputProductResponse = productMockService.getProductById(productDetail.getProduct_id());
        Assert.assertEquals(expectedProduct.getId(), outputProductResponse.getId());
        Assert.assertEquals(expectedProduct.getName(), outputProductResponse.getName());
        Assert.assertEquals(expectedProduct.getCurrent_price().getValue(), outputProductResponse.getCurrent_price().getValue());
    }

    @Test
    public void TestCase2_getProductByIdNotFoundTest() {
        ProductDetail prodDetail = new ProductDetail();
        prodDetail.setProduct_id(13860428);

        Mockito.doReturn(null).when(productMockDetailRepository).getProductDetail(prodDetail.getProduct_id());

        try {
            productMockService.getProductById(prodDetail.getProduct_id());
            fail();
        } catch (ProductNotFoundException e) {
            Assert.assertEquals("Product - " + prodDetail.getProduct_id() + " not found in Database", e.getMessage());
        }
    }

    @Test
    public void TestCase3_updateProductPriceTest() {
        ProductDetail prodDetail = new ProductDetail();
        prodDetail.setProduct_id(13860428);
        prodDetail.setPrice(15.99F);
        prodDetail.setCurrency_code("USD");

        Product expectedProduct = new Product();
        expectedProduct.setId(prodDetail.getProduct_id());
        expectedProduct.setName("Sample Product");
        expectedProduct.setCurrent_price(new Price(prodDetail.getPrice(), prodDetail.getCurrency_code()));

        Mockito.doReturn(prodDetail).when(productMockDetailRepository).getProductDetail(any(Integer.class));
        Mockito.doReturn(prodDetail).when(productMockDetailRepository).save(any(ProductDetail.class));
        Mockito.doReturn("Sample Product").when(targetMockExternalService).getProductName("13860428");

        Product updateProductResponse = productMockService.updateProductPrice(prodDetail.getProduct_id(), expectedProduct);
        Assert.assertTrue(new ReflectionEquals(expectedProduct, "").matches(updateProductResponse));
    }

    @Test
    public void TestCase4_updateProductPriceWithoutRequestTest() {
        try {
            productMockService.updateProductPrice(12345678, null);
            fail();
        } catch (ProductNotFoundException e) {
            Assert.assertEquals("Input request is not valid", e.getMessage());
        }
    }

    @Test
    public void TestCase5_updateProductPriceWithoutNameTest() {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setProduct_id(12345678);
        productDetail.setPrice(13.99F);
        productDetail.setCurrency_code("USD");

        Product expectedProduct = new Product();
        expectedProduct.setId(productDetail.getProduct_id());
        expectedProduct.setName("TestProduct");
        expectedProduct.setCurrent_price(new Price(productDetail.getPrice(), productDetail.getCurrency_code()));

        Product inputProduct = new Product();
        inputProduct.setId(productDetail.getProduct_id());
        inputProduct.setCurrent_price(expectedProduct.getCurrent_price());

        Mockito.doReturn(productDetail).when(productMockDetailRepository).getProductDetail(any(Integer.class));
        Mockito.doReturn(productDetail).when(productMockDetailRepository).save(any(ProductDetail.class));
        Mockito.doReturn("TestProduct").when(targetMockExternalService).getProductName("12345678");

        Product updateProductResponse = productMockService.updateProductPrice(productDetail.getProduct_id(), inputProduct);
        Assert.assertTrue(new ReflectionEquals(expectedProduct, "").matches(updateProductResponse));
    }

    @Test
    public void TestCase6_updateProductPriceNullObjectTest() {

        Product inputProdRequest = new Product();
        inputProdRequest.setId(13860428);
        inputProdRequest.setName("Sample Product");

        try {
            productMockService.updateProductPrice(inputProdRequest.getId(), inputProdRequest);
            fail();
        } catch (ProductNotFoundException e) {
            Assert.assertEquals("Product price cannot be NULL", e.getMessage());
        }
    }

    @Test
    public void TestCase7_updateProductPriceWithoutPriceTest() {

        Price priceResponse = new Price();
        priceResponse.setCurrency_code("USD");

        Product inputProductRequest = new Product();
        inputProductRequest.setId(13860428);
        inputProductRequest.setName("Sample Product");
        inputProductRequest.setCurrent_price(priceResponse);

        try {
            productMockService.updateProductPrice(inputProductRequest.getId(), inputProductRequest);
            fail();
        } catch (ProductNotFoundException e) {
            Assert.assertEquals("Product price cannot be NULL", e.getMessage());
        }
    }

    @Test
    public void TestCase8_updateProductPriceNotFoundTest() {
        int productId = 12345;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setName("Sample Product");
        expectedProduct.setCurrent_price(new Price(12.5F, "USD"));

        Mockito.doReturn(null).when(productMockDetailRepository).getProductDetail(any(Integer.class));

        try {
            productMockService.updateProductPrice(productId, expectedProduct);
            fail();
        } catch (ProductNotFoundException e) {
            Assert.assertEquals("Product - " + productId + " not found in Database", e.getMessage());
        }
    }

    @Test
    public void TestCase9_getProductNameFromRedSkyApi() {
        String expectedProductNameFromRedSkyApi = "The Big Lebowski (Blu-ray)";
        Mockito.doReturn(expectedProductNameFromRedSkyApi).when(targetMockExternalService).getProductName("13860428");
        String outputProductName = productMockService.getProductNameFromRedSkyApi(13860428);
        Assert.assertEquals(expectedProductNameFromRedSkyApi, outputProductName);
    }


}
