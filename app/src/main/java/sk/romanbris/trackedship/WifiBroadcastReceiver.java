package sk.romanbris.trackedship;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;

/**
 * Created by Roman Bris on 9/7/2017.
 */

public class WifiBroadcastReceiver extends BroadcastReceiver {

    private MainActivity mActivity;

    public WifiBroadcastReceiver(MainActivity activity) {

        mActivity = activity;
        mActivity.setWifiState(false);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            mActivity.setWifiState(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
        }
    }
}
