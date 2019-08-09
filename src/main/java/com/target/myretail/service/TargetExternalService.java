package com.target.myretail.service;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.target.myretail.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TargetExternalService {

    /**
     * This method can be used to read the Json string for the given Product
     */
    @Autowired
    @Qualifier("restTemplate")
    RestTemplate restTemplate;

    @Value("${redskyexternalapi.rooturl}")
    String rootURL;

    @Value("${redskyexternalapi.excludeattribute}")
    String excludeAttribute;

    public String getProductName(String productId) {
        //final String uriRedSky = "https://redsky.target.com/v2/pdp/tcin/" + productId + "?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
        final String uriRedSky = rootURL + productId + "?" + excludeAttribute;
        String productName;
        try {
            ResponseEntity<String> responseJSONString = restTemplate.getForEntity(uriRedSky, String.class);
            productName = parseProductName(responseJSONString.getBody());
            if (productName == null) {
                throw new ProductNotFoundException("Product name returned as empty for id - " + productId);
            }
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new ProductNotFoundException("Product not found in red sky api for id - " + productId);
            } else
                throw new ProductNotFoundException("Error in calling external API - " + e.getStatusCode());
        }
        return productName;
    }

    /**
     * This method can be used to get the Product Name from RedSkyAPI
     */
    private String parseProductName(String jsonDataSourceString) {
        String jsonProductNamePath = "$['product']['item']['product_description']['title']";
        DocumentContext jsonContext = JsonPath.parse(jsonDataSourceString);
        return jsonContext.read(jsonProductNamePath);
    }
}
