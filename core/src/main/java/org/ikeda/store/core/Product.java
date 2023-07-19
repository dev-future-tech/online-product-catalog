package org.ikeda.store.core;

import jakarta.persistence.*;

import java.io.Serializable;

@NamedQuery(name="findAllProducts",
    query="Select c from Product c order by c.productId")
@Table(name = "products")
@Entity
public class Product implements Serializable {

    @Id
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "sku")
    private String sku;

    @Column(name = "product_name")
    private String name;

    @Column(name = "description")
    private String description;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
