package com.locus.demo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.locus.demo.domain.LatLng;

/**
 * @author sanjaikumar.arumugam
 */
@Component
public class IntervalCalculator {
	public static final int INTERVAL = 50;

	/**
	 * We need to get the locations on the route such that they are separated by
	 * INTERVAL = 50m
	 * 
	 * @param locations
	 * @return
	 */
	public List<LatLng> getDesiredLocations(List<LatLng> locations) {
		LinkedList<LatLng> desiredLocations = new LinkedList<>();
		List<List<LatLng>> pairs = plotCoordinatesRoutes(locations);
		for (int i = 0; i < pairs.size(); i++) {
			List<LatLng> step = pairs.get(i);

			desiredLocations.add(step.get(0));
			if (step.size() < 2)
				continue;
			if (step.size() > INTERVAL && step.size() < 2 * INTERVAL)
				desiredLocations.add(step.get(step.size() / 2));
			else
				for (int j = 1; j < step.size() - 1; j++) {
					double distanceTo = step.get(j).distanceTo(desiredLocations.peekLast());
					if (!(distanceTo < INTERVAL))
						desiredLocations.add(step.get(j));
				}
			desiredLocations.add(step.get(step.size() - 1));
		}
		return desiredLocations;
	}

	/**
	 * call the consumer for eact pair for coordinates once they are analyzed or
	 * return final filtered LatLng pairs
	 * 
	 * @param locations
	 * @return
	 */
	public List<List<LatLng>> plotCoordinatesRoutes(List<LatLng> points) {
		List<List<LatLng>> steps = new LinkedList<>();
		steps.add(Arrays.asList(points.get(0)));
		for (int i = 1; i < points.size(); i++) {
			if (points.get(i).distanceTo(points.get(i - 1)) <= INTERVAL) {
				steps.add(Arrays.asList(points.get(i)));
			} else
				steps.add(getLocations(1, points.get(i - 1).initialBearingTo(points.get(i)), points.get(i - 1),
						points.get(i)));
		}

		return steps;
	}

	/**
	 * returns every coordinate pair in between two coordinate pairs given the
	 * desired interval
	 * 
	 * @param interval
	 * @param azimuth
	 * @param start
	 * @param end
	 * @source https://gis.stackexchange.com/questions/157693/getting-all-vertex-lat-long-coordinates-every-1-meter-between-two-known-points
	 * @return
	 */
	private List<LatLng> getLocations(int interval, double azimuth, LatLng start, LatLng end) {

		double d = start.distanceTo(end);
		int dist = (int) d / interval;
		int coveredDist = interval;
		List<LatLng> coords = new ArrayList<>();
		coords.add(start);
		for (int distance = 0; distance < dist; distance += interval) {
			coords.add(start.destinationPoint(coveredDist, azimuth));
			coveredDist += interval;
		}
		coords.add(end);

		return coords;

	}

}
