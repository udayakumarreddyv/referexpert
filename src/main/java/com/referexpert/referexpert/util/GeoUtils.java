package com.referexpert.referexpert.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.referexpert.referexpert.beans.Coordinates;

@Component
public class GeoUtils {
    
    private static final Logger logger = LoggerFactory.getLogger(GeoUtils.class);

    public static boolean isDistanceInRange(Double srcLattitude, Double srcLongitude, Double destLattitude,
            Double destLongitude, int distance) {
        logger.info("GeoUtils :: In isDistanceInRange :: " + srcLattitude + " :: " + srcLongitude + " :: "+ destLattitude 
        		+ " :: "+ destLongitude + " :: "+ distance + " :: ");
        if (srcLattitude == null || destLattitude == null || srcLongitude == null || destLongitude == null) {
            return false;
        }
        if ((srcLattitude == destLattitude) && (srcLongitude == destLongitude)) {
            return false;
        } else {
            double theta = srcLongitude - destLongitude;
            double dist = Math.sin(Math.toRadians(srcLattitude)) * Math.sin(Math.toRadians(destLattitude))
                    + Math.cos(Math.toRadians(srcLattitude)) * Math.cos(Math.toRadians(destLattitude))
                            * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return dist <= distance;
        }
    }
    
    public static double getDistnace(Double srcLattitude, Double srcLongitude, Double destLattitude,
            Double destLongitude, int distance) {
        logger.info("GeoUtils :: In getDistnace :: " + srcLattitude + " :: " + srcLongitude + " :: "+ destLattitude 
        		+ " :: "+ destLongitude + " :: "+ distance + " :: ");
        if (srcLattitude == null || destLattitude == null || srcLongitude == null || destLongitude == null) {
            return 0;
        }
        if ((srcLattitude == destLattitude) && (srcLongitude == destLongitude)) {
            return 0;
        } else {
            double theta = srcLongitude - destLongitude;
            double dist = Math.sin(Math.toRadians(srcLattitude)) * Math.sin(Math.toRadians(destLattitude))
                    + Math.cos(Math.toRadians(srcLattitude)) * Math.cos(Math.toRadians(destLattitude))
                            * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            return Math.round(dist);
        }
    }
    
    public static Coordinates getCoordinates(String address, GeoApiContext geoApiContext) {
        logger.info("GeoUtils :: In getCoordinates from Google API :: " + address);
        try {
            GeocodingResult[] results = GeocodingApi.geocode(geoApiContext, address).await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Coordinates coordinates = new Coordinates();
            coordinates.setLattitude(new Double(gson.toJson(results[0].geometry.location.lat)));
            coordinates.setLongitude(new Double(gson.toJson(results[0].geometry.location.lng)));
            return coordinates;
        }
        catch (Exception e) {
        	logger.error("Exeption while getting Coordinated for address :: " + address);
        	logger.error("Exception details :: " + e);
        	e.printStackTrace();
            return new Coordinates(0.0, 0.0);
        }
    }
}
