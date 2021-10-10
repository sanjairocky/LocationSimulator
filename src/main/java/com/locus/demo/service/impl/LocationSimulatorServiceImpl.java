package com.locus.demo.service.impl;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.locus.demo.domain.LatLng;
import com.locus.demo.domain.LocationsResponse;
import com.locus.demo.exception.LocationSimulatorException;
import com.locus.demo.manager.LocationSimulatorManager;
import com.locus.demo.service.LocationSimulatorService;

/**
 * @author sanjaikumar.arumugam
 */
@Service
public class LocationSimulatorServiceImpl implements LocationSimulatorService {

	private static final Logger LOGGER = Logger.getLogger(LocationSimulatorServiceImpl.class.getName());

	private LocationSimulatorManager locationSimulatorManager;

	public LocationSimulatorServiceImpl(LocationSimulatorManager locationSimulatorManager) {
		this.locationSimulatorManager = locationSimulatorManager;
	}

	public LocationsResponse getLocations(LatLng src, LatLng dest) {
		List<LatLng> locations;
		try {
			locations = locationSimulatorManager.getLocations(src, dest);
			return buildSuccessResponse(locations);
		} catch (LocationSimulatorException ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
			return buildErrorResponse(ex);
		}
	}

	private LocationsResponse buildSuccessResponse(List<LatLng> locations) {
		LocationsResponse resp = new LocationsResponse();
		resp.setLocations(locations);
		resp.setStatusCode(HttpURLConnection.HTTP_OK);
		return resp;
	}

	private LocationsResponse buildErrorResponse(LocationSimulatorException ex) {
		LocationsResponse resp = new LocationsResponse();
		resp.setStatusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
		resp.setStatusMessage(ex.getMessage());
		return resp;
	}

}
