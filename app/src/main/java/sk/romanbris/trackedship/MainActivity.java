package sk.romanbris.trackedship;

import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final IntentFilter mIntentFilter = new IntentFilter();
    private WifiBroadcastReceiver mWifiReceiver;

    private TextView mTvWifiState;

    public void setWifiState(boolean state) {
        mTvWifiState.setText(state
                ? R.string.wifi_on
                : R.string.wifi_off);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetReferences();

        mWifiReceiver = new WifiBroadcastReceiver(this);

        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    private void GetReferences() {
        mTvWifiState = (TextView) findViewById(R.id.tvWifiState);
    }
}
