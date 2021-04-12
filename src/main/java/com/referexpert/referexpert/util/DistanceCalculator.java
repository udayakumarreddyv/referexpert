package com.referexpert.referexpert.util;

import org.springframework.stereotype.Component;

@Component
public class DistanceCalculator {

    public static boolean isDistanceInRange(Double srcLattitude, Double srcLongitude, Double destLattitude,
            Double destLongitude, int distance) {
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
}
