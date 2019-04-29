package com.shakeel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_location")
public class UserLocation {
	//private static final long serialVersionUID = 1L;
	@Id
	@Column(name="uid")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long uid;
	@Column(name="id")
	private String id;    
	@Column(name="location_x")
	private double location_x;
	@Column(name="location_y")
	private double location_y;
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public double getLocation_x() {
		return location_x;
		 
	}
	public void setLocation_x(double location_x) {
		this.location_x = location_x;
	}
	public double getLocation_y() {
		return location_y;
	}
	public void setLocation_y(double location_y) { 
		this.location_y = location_y;
	}
}
