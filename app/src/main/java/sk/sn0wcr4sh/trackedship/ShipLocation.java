package sk.sn0wcr4sh.trackedship;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by sn0wcr4sh on 9/19/2017.
 *
 */

class ShipLocation {

    interface Listener {
        void onLocationChanged(Location location);
    }

    private LocationManager mLocationManager;

    private Listener mListener;
    private Location mLastLocation;

    ShipLocation(Context context, Listener listener) {

        mListener = listener;
        mLocationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        init();
    }

    void start() {
        try {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 1, mLocationListener);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    void stop() {
        try {
            mLocationManager.removeUpdates(mLocationListener);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    Location getLastLocation() {
        return mLastLocation;
    }

    private void init() {
        try {
            raiseUpdateLocation(mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
            raiseUpdateLocation(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void raiseUpdateLocation(Location location) {
        if (location == null)
            return;
        mLastLocation = location;
        mListener.onLocationChanged(mLastLocation);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            raiseUpdateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
