package com.locus.demo.manager;

import java.util.List;

import com.locus.demo.domain.LatLng;
import com.locus.demo.exception.LocationSimulatorException;

/**
 * @author sanjaikumar.arumugam
 */
public interface LocationSimulatorManager {

    List<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException;
}
