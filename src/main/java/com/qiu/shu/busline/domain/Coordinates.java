package com.qiu.shu.busline.domain;

import java.util.List;

public class Coordinates {

	private String type;
	
	private List<List<Double>> coordinates;

	public List<List<Double>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<Double>> coordinates) {
		this.coordinates = coordinates;
	}

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
