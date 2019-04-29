package com.shakeel.model;

import javax.persistence.*;

/**
 * Created by shakeelosmani on 24/12/16.
 */

@Entity
@Table(name="product")
public class Product {

    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Long product_id;
    @Column(name="product_name")
    private String product_name;
    @Column(name="product_price")
    private Double product_price;

    public Long getProductId() {
        return product_id;
    }

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Double getProduct_price() {
		return product_price;
	}

	public void setProduct_price(Double product_price) {
		this.product_price = product_price;
	}




}

