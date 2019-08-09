package com.target.myretail.service;

import com.target.myretail.entity.Price;
import com.target.myretail.entity.Product;
import com.target.myretail.exception.ProductNotFoundException;
import com.target.myretail.model.ProductDetail;
import com.target.myretail.repository.ProductDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class ProductService {

    @Autowired
    ProductDetailRepository productDetailRepository;

    @Autowired
    TargetExternalService targetExternalService;
    /**
     * This method can be used to fetch the product details of a given product
     *
     */
    public Product getProductById(Integer id) {
        ProductDetail productDetail = getProductDetail(id);

        return generateProductResponse(id, productDetail);
    }

    /**
     * This method can be used to update the price of a given product
     *
     */
    public Product updateProductPrice(Integer productId, Product inputProduct) {
        if (inputProduct == null) {
            throw new ProductNotFoundException("Input request is not valid");
        }

        if (inputProduct.getCurrent_price() == null || inputProduct.getCurrent_price().getValue() == null) {
            throw new ProductNotFoundException("Product price cannot be NULL");
        }

        if (inputProduct.getCurrent_price().getValue() < 0) {
            throw new ProductNotFoundException("Product price cannot be negative");
        }


        ProductDetail existingProductDetail =  getProductDetail(productId);
        ProductDetail updateProductDetail = new ProductDetail();
        updateProductDetail.setId(existingProductDetail.getId());
        updateProductDetail.setProduct_id(existingProductDetail.getProduct_id());
        updateProductDetail.setPrice(inputProduct.getCurrent_price().getValue());
        updateProductDetail.setCurrency_code(existingProductDetail.getCurrency_code());

        // update price in existing product price store
        ProductDetail updatedProduct = productDetailRepository.save(updateProductDetail);
        return generateProductResponse(productId, updatedProduct);

    }

        /**
         * This method can be used to get the Product Name from RedSkyAPI and generate the Product response
         *
         */
    private Product generateProductResponse(Integer productId, ProductDetail updatedProduct) {
        String productName = getProductNameFromRedSkyApi(productId);
        return new Product(productId, productName, new Price(updatedProduct.getPrice(), updatedProduct.getCurrency_code()));
    }

    private ProductDetail getProductDetail(Integer productId){
        ProductDetail  prodDetail = productDetailRepository.getProductDetail(productId);
        if(prodDetail == null) {
            throw new ProductNotFoundException("Product - " + productId + " not found in Database");
        }
        return prodDetail;
    }

    public String getProductNameFromRedSkyApi(Integer productId){
        return targetExternalService.getProductName(productId.toString());
    }

}
