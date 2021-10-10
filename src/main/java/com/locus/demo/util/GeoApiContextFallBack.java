package com.locus.demo.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.locus.demo.domain.LatLng;

public class GeoApiContextFallBack {

	private String apiKey;

	private Optional<Map<String, Object>> result;

	public GeoApiContextFallBack apiKey(String apiKey) {
		this.apiKey = apiKey;
		return this;
	}

	static {
		try {
			SSLContext sc = SSLContext.getInstance("SSL");

			sc.init(null, new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}

			} }, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// Install the all-trusting host verifier
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
		} catch (Exception e) {
		}
	}

	public GeoApiContextFallBack invokeDirectionApi(LatLng origin, LatLng destination) throws Exception {

		String readLine = null;
		HttpsURLConnection conection = (HttpsURLConnection) new URL(
				"https://maps.googleapis.com/maps/api/directions/json?" + "origin=" + origin + "&destination="
						+ destination + "&key=" + apiKey).openConnection();
		conection.setRequestMethod("GET");
		if (conection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
			StringBuffer response = new StringBuffer();
			while ((readLine = in.readLine()) != null) {
				response.append(readLine);
			}
			in.close();
			// print result
			result = Optional.of(new Gson().fromJson(response.toString(), new TypeToken<Map<String, Object>>() {
			}.getType()));
		}
		return this;
	}

	public static List<LatLng> decodePoly(String encoded) {
		List<LatLng> poly = new ArrayList<>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b = 0, shift = 0, result = 0;
			do {
				if (index < len) {
					b = encoded.charAt(index++) - 63;
				}
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				if (index < len) {
					b = encoded.charAt(index++) - 63;
				}
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)), (((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	@SuppressWarnings("rawtypes")
	public List<LatLng> decodeOverviewPolyline() {
		List<LatLng> response = new LinkedList<>();
		result.map(e -> ((Map) e).get("routes")).filter(e -> e != null).map(e -> ((List) e).get(0))
				.filter(e -> e != null).map(e -> ((Map) e).get("overview_polyline")).filter(e -> e != null)
				.map(e -> ((Map) e).get("points")).ifPresent(points -> response.addAll(decodePoly((String) points)));

		return response;
	}
}
