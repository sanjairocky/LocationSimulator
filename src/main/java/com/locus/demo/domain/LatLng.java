package com.locus.demo.domain;

public class LatLng {

	// Radius of the earth in meters
	private static Integer earthRadiusInMeters = 6371000;
	public double lat;
	public double lng;
	public boolean include = true;
	public double leftDegree;
	public double rightDegree;
	public double distance;

	public LatLng(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	/**
	 * returns the lat an long of destination point given the this point aziuth, and
	 * distance
	 * 
	 * @param lat
	 * @param lng
	 * @param azimuth  aka bearing
	 * @param distance
	 * @return
	 */
	public LatLng destinationPoint(double distance, double azimuth) {
		double brng = Math.toRadians(azimuth); // Bearing is degrees converted to radians.
		double d = distance / earthRadiusInMeters; // Distance
		double lat1 = Math.toRadians(lat); // Current dd lat point converted to radians
		double lon1 = Math.toRadians(lng); // Current dd long point converted to radians
		double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d) + Math.cos(lat1) * Math.sin(d) * Math.cos(brng));
		double lon2 = lon1 + Math.atan2(Math.sin(brng) * Math.sin(d) * Math.cos(lat1),
				Math.cos(d) - Math.sin(lat1) * Math.sin(lat2));
		return new LatLng(Math.toDegrees(lat2), Math.toDegrees(lon2));
	}

	/**
	 * calculates the distance between this with LatLng coordinate pairs
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public double distanceTo(LatLng end) {

		double lat1Rads = Math.toRadians(lat);
		double lat2Rads = Math.toRadians(end.lat);

		double deltaLat = Math.toRadians(end.lat - lat);
		double deltaLng = Math.toRadians(end.lng - lng);

		double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
				+ Math.cos(lat1Rads) * Math.cos(lat2Rads) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = earthRadiusInMeters * c;
		return d;
	}

	/**
	 * calculates the azimuth in degrees from start point to end point"); double
	 * startLat = Math.toRadians(start.lat);
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public double initialBearingTo(LatLng end) {
		double startLat = Math.toRadians(lat);
		double startLong = Math.toRadians(lng);
		double endLat = Math.toRadians(end.lat);
		double endLong = Math.toRadians(end.lng);
		double dLong = endLong - startLong;
		double dPhi = Math
				.log(Math.tan((endLat / 2.0) + (Math.PI / 4.0)) / Math.tan((startLat / 2.0) + (Math.PI / 4.0)));
		if (Math.abs(dLong) > Math.PI) {
			if (dLong > 0.0) {
				dLong = -(2.0 * Math.PI - dLong);
			} else {
				dLong = (2.0 * Math.PI + dLong);
			}
		}
		return (Math.toDegrees(Math.atan2(dLong, dPhi)) + 360.0) % 360.0;
	}

	@Override
	public String toString() {
		return this.coor();
	}

	public String coor() {
		return decimalPoint(lat) + "," + decimalPoint(lng);
	}

	@SuppressWarnings("deprecation")
	private String decimalPoint(double val) {
		return val+"";
//		return BigDecimal.valueOf(val).setScale(5, BigDecimal.ROUND_HALF_UP).toString();
	}

}
