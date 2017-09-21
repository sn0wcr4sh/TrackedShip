package sk.sn0wcr4sh.trackedship;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sn0wcr4sh on 9/19/2017.
 */

public class Permissions {

    public static final int REQ_FINE_LOCATION = 1;

    private AppCompatActivity mActivity;

    public Permissions(AppCompatActivity activity) {
        mActivity = activity;
    }


    public void requestFineLocation() {
        if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    mActivity,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_FINE_LOCATION);
        }
    }
}
