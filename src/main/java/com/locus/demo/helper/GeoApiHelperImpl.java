/**
 * 
 */
package com.locus.demo.helper;

import java.util.LinkedList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.locus.demo.codes.LocationSimulatorErrorCodes;
import com.locus.demo.domain.LatLng;
import com.locus.demo.exception.LocationSimulatorException;
import com.locus.demo.util.GeoApiContextFallBack;

/**
 * @author sanjaikumar.arumugam
 *
 */
@Component
public class GeoApiHelperImpl implements GeoApiHelper {

	private String API_KEY;

	public GeoApiHelperImpl(@Value("${apiKey}") String apiKey) {
		this.API_KEY = apiKey;
	}

	@Override
	public LinkedList<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException {
		LinkedList<LatLng> locations = new LinkedList<>();
		try {

			GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

			DirectionsResult results = null;
			try {
				results = DirectionsApi.newRequest(context).origin(src.coor()).destination(destination.coor()).await();

				DirectionsRoute routes[] = results.routes;
				if (routes.length != 1) {
					throw new LocationSimulatorException("Invalid Routes: " + routes,
							LocationSimulatorErrorCodes.INVALID_GEO_RESPONSE);
				}

				locations.addAll(routes[0].overviewPolyline.decodePath().stream().map(l -> new LatLng(l.lat, l.lng))
						.collect(Collectors.toList()));

			} catch (Exception e) {
				locations.addAll(new GeoApiContextFallBack().apiKey(API_KEY).invokeDirectionApi(src, destination)
						.decodeOverviewPolyline());
			}
		} catch (Exception e) {
			throw new LocationSimulatorException(e.getMessage(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
		}

		return locations;
	}

}
