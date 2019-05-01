package utils;

import java.text.DecimalFormat;

/**
 * Useful functions to deal with doubles.
 *
 * @author Cristian, Ferran, Iscle
 *
 */
public class DoubleUtilities {

    public static double[] computeRandomGeographicCoordinates() {
        double minLat = -90.00;
        double maxLat = 90.00;
        double latitude = minLat + (double)(Math.random() * ((maxLat - minLat) + 1));
        double minLon = 0.00;
        double maxLon = 180.00;
        double longitude = minLon + (double)(Math.random() * ((maxLon - minLon) + 1));
        return new double[]{latitude, longitude};
    }

    public static String computeFormattedGeographicCoordinate(double coordinate) {
        DecimalFormat df = new DecimalFormat("#.#####");
        return df.format(coordinate);
    }
}
