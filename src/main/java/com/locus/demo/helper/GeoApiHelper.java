package com.locus.demo.helper;

import java.util.LinkedList;

import com.locus.demo.domain.LatLng;
import com.locus.demo.exception.LocationSimulatorException;

public interface GeoApiHelper {

	public LinkedList<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException;

}
