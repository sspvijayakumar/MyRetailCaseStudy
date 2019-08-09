package com.target.myretail.controller;

import com.target.myretail.entity.Product;
import com.target.myretail.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/")
public class ProductController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
     ProductService productService;

    /**
     * This method can be used to fetch the product details of a given product
     *
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductById(@PathVariable("id") Integer id) {
        logger.info("Calling getProduct with id {}", id);
        Product productById = productService.getProductById(id);
        return productById;
    }

    /**
     * This method can be used to update the price of a given product
     *
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Product updateProductPriceById(@PathVariable("id") Integer id,
                                      @RequestBody Product inputRequest) {
        logger.info("Calling  updateProductPrice with id {}", id);
        return productService.updateProductPrice(id, inputRequest);

    }
}
