package com.locus.demo;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.locus.demo.domain.LatLng;
import com.locus.demo.domain.LocationsResponse;
import com.locus.demo.service.LocationSimulatorService;

@SpringBootApplication
public class LocationSimulatorApplication implements CommandLineRunner {

	@Autowired
	LocationSimulatorService locationSimulatorService;

	public static void main(String[] args) {
		SpringApplication.run(LocationSimulatorApplication.class, args);
	}

	@SuppressWarnings("resource")
	@Override
	public void run(String... args) throws Exception {
		try {
			LatLng start, end;

			Scanner in = new Scanner(System.in);
			System.out.print("\nSource Coordinates [12.93175, 77.62872] : ");
			try {
				String[] st = in.nextLine().trim().split(",");
				start = new LatLng(Double.valueOf(st[0]), Double.valueOf(st[1]));
			} catch (Exception e) {
				start = new LatLng(12.93175, 77.62872);
			}
			System.out.print("\nDestination Coordinates [12.92662, 77.63696] : ");
			try {
				String[] ed = in.nextLine().trim().split(",");
				end = new LatLng(Double.valueOf(ed[0]), Double.valueOf(ed[1]));
			} catch (Exception e) {
				end = new LatLng(12.92662, 77.63696);
			}
			// Call LocationSimulatorService to get all the locations present between source
			// and destination
			LocationsResponse locations = locationSimulatorService.getLocations(start, end);
			if (locations.getLocations() == null) {
				System.out.println("No locations found for src: " + start.toString() + " and destination: "
						+ end.toString() + ". Error: " + locations.getStatusMessage());
				System.exit(0);
			}
			printLocations(locations.getLocations());
		} catch (Exception e) {
			System.err.println("your coordinates are malformed. Please try again!");
		}

	}

	private static void printLocations(List<LatLng> locations) {
		System.out.println();
		System.out.println("Cordinates found!");
		System.out.println();
		for (LatLng location : locations) {
			System.out.println(location.toString() + ",");
		}

	}

}
