package com.target.myretail.repository;

import com.target.myretail.model.ProductDetail;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends MongoRepository<ProductDetail, String> {
    @Query(value = "{ 'product_id' : ?0 }")
    ProductDetail getProductDetail(Integer product_id);
}
