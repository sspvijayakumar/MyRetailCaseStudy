package com.target.myretail.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection= "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail {
    @Id
    private String id;
    private Integer product_id;
    private Float price;
    private String currency_code;
}
