package sk.sn0wcr4sh.trackedship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by sn0wcr4sh on 9/7/2017.
 *
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "TrackedShip";

    private MainActivity mActivity;
    private P2pManager mP2pManager;

    public WifiBroadcastReceiver(MainActivity activity, P2pManager p2pManager) {

        mActivity = activity;
        mP2pManager = p2pManager;
        mActivity.setWifiState(false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            mActivity.setWifiState(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
            return;
        }

        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "Connection changed");
            mP2pManager.readGroup();
            return;
        }
    }
}
