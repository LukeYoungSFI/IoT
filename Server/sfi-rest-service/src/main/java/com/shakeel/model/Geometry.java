package com.shakeel.model;
//geometry class is set in Display class for the required format of Leaflet JavaScript API
import java.util.ArrayList;

public class Geometry {
private String type;
private ArrayList<Double> coordinates;
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public ArrayList<Double> getCoordinates() {
	return coordinates;
}
public void setCoordinates(ArrayList<Double> coordinates) {
	this.coordinates = coordinates;
}



}
