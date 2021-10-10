package com.locus.demo.manager.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.springframework.stereotype.Service;

import com.locus.demo.domain.LatLng;
import com.locus.demo.exception.LocationSimulatorException;
import com.locus.demo.helper.GeoApiHelper;
import com.locus.demo.manager.LocationSimulatorManager;
import com.locus.demo.util.IntervalCalculator;

/**
 * @author sanjaikumar.arumugam
 */
@Service
public class LocationSimulatorManagerImpl implements LocationSimulatorManager {

	private IntervalCalculator calculator;

	private GeoApiHelper geoApiHelper;

	public LocationSimulatorManagerImpl(IntervalCalculator calculator, GeoApiHelper geoApiHelper) {
		this.calculator = calculator;
		this.geoApiHelper = geoApiHelper;
	}

	public List<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException {

		LinkedList<LatLng> locations = geoApiHelper.getLocations(src, destination);
		locations.addFirst(src);
		locations.addLast(destination);
		return calculator.getDesiredLocations(cleanUpExtraCoordinates(locations));

	}

	private List<LatLng> cleanUpExtraCoordinates(List<LatLng> points) {
		Stack<LatLng> steps = new Stack<>();
		steps.push(points.get(0));
		for (int i = 1; i < points.size() - 1; i++) {
			if (Math.abs(points.get(i - 1).initialBearingTo(points.get(i + 1))
					- points.get(i - 1).initialBearingTo(points.get(i))) > 5) {
				steps.push(points.get(i));
			}
		}
		steps.push(points.get(points.size() - 1));
		return steps;
	}

}
