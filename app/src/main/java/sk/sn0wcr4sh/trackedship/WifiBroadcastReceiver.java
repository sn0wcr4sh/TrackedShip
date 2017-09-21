package sk.sn0wcr4sh.trackedship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

/**
 * Created by sn0wcr4sh on 9/7/2017.
 *
 */

interface WifiListener {
    void onWifiP2pStateChanged(int state);
    void onPeersChanged();
    void onConnectionChanged(Intent intent);
}

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private final String TAG = "TrackedShip";

    private WifiListener mListener;
    private IntentFilter mFilter;

    public WifiBroadcastReceiver(WifiListener listener) {

        mListener = listener;

        mFilter = new IntentFilter();
        mFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            mListener.onWifiP2pStateChanged(state);
            return;
        }

        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            Log.d(TAG, "Connection changed");
            mListener.onConnectionChanged(intent);
        }
    }

    IntentFilter getFilter() {
        return mFilter;
    }
}
