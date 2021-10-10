package com.locus.demo.service;

import com.locus.demo.domain.LatLng;
import com.locus.demo.domain.LocationsResponse;

/**
 * @author sanjaikumar.arumugam
 */
public interface LocationSimulatorService {

     LocationsResponse getLocations(LatLng src, LatLng dest);
}
