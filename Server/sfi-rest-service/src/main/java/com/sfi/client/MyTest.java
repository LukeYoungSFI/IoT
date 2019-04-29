package com.sfi.client;

import java.util.ArrayList;
import java.util.List;

import com.sf.server.archivemanager.util.JsonUtil;
import com.shakeel.model.DisplayPoint;
import com.shakeel.model.Geometry;
import com.shakeel.model.Product;
import com.shakeel.model.Property;

public class MyTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
    Geometry geometry = new Geometry();
    ArrayList<Double> coordinates = new ArrayList<Double>();
    coordinates.add(12.1);
    coordinates.add(13.1);
    geometry.setType("Point");
    geometry.setCoordinates(coordinates);
    DisplayPoint displayPoint = new DisplayPoint();
    displayPoint.setGeometry(geometry);
    displayPoint.setType("Feature");
    displayPoint.setProperties(new Property());
    
    try {
		String jsonString =  JsonUtil.getJSONString(displayPoint);
		System.out.println(jsonString);
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}

}
