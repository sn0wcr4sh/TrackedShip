package sk.sn0wcr4sh.trackedship;

import android.net.wifi.p2p.WifiP2pGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WifiBroadcastReceiver mWifiReceiver;

    private P2pManager mP2p;

    private TextView mTvWifiState;
    private TextView mTvNetworkName;
    private TextView mTvDeviceAddress;
    private Button mBtRecreateGroup;

    public void setWifiState(boolean state) {
        mTvWifiState.setText(state
                ? R.string.wifi_on
                : R.string.wifi_off);

        mBtRecreateGroup.setEnabled(state);
    }

    public void onCreateGroupClick(View v) {
        mP2p.ensureGroup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetReferences();


        mP2p = new P2pManager(this, new P2pManager.Listener() {
            @Override
            public void onGroupUpdate(WifiP2pGroup group) {
                if (group != null) {
                    mTvNetworkName.setText(group.getNetworkName());
                    mTvDeviceAddress.setText(group.getOwner().deviceAddress);
                }
            }
        });
        mP2p.registerService();

        mWifiReceiver = new WifiBroadcastReceiver(this, mP2p);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mWifiReceiver, mP2p.getFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiReceiver);
    }

    private void GetReferences() {
        mTvWifiState = (TextView) findViewById(R.id.tvWifiState);
        mTvNetworkName = (TextView)findViewById(R.id.tvNetworkName);
        mTvDeviceAddress = (TextView)findViewById(R.id.tvDeviceAddress);

        mBtRecreateGroup = (Button)findViewById(R.id.btRecreateGroup);
    }
}
