package com.shakeel.model;


import javax.persistence.*;

/**
 * Created by shakeelosmani on 24/12/16.
 */

@Entity
@Table(name="customer")
public class Customer {

    @Id
    @Column(name="customer_id")
    @GeneratedValue(strategy = GenerationType.AUTO)

    private long customer_id;
    @Column(name="first_name")
    private String first_name;
    @Column(name="last_name")
    private String last_name;

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public long getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(long customer_id) {
		this.customer_id = customer_id;
	}

}
