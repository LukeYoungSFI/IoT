package com.shakeel.model;
//this model is used to Leaflet JavaScript API 
public class DisplayPoint {
private Geometry geometry;
private String type;
private Property properties;
public DisplayPoint(){}
public Geometry getGeometry() {
	return geometry;
}

public void setGeometry(Geometry geometry) {
	this.geometry = geometry;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public Property getProperties() {
	return properties;
}

public void setProperties(Property properties) {
	this.properties = properties;
}
}
