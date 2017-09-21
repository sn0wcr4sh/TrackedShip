package sk.sn0wcr4sh.trackedship;

import android.content.Intent;
import android.location.Location;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private WifiBroadcastReceiver mWifiReceiver;

    private Permissions mPermissions;
    private P2pManager mP2p;
    private TcpServer mTcpServer;
    private ShipLocation mLocation;

    private TextView mTvWifiState;
    private TextView mTvNetworkName;
    private TextView mTvDeviceAddress;
    private Button mBtRecreateGroup;

    public void onRecreateGroupClick(View v) {
        mP2p.ensureGroup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetReferences();

        mPermissions = new Permissions(this);

        mP2p = new P2pManager(this, new P2pManager.Listener() {
            @Override
            public void onGroupUpdate(WifiP2pGroup group) {
                if (group != null) {
                    mTvNetworkName.setText(group.getNetworkName());
                    mTvDeviceAddress.setText(group.getOwner().deviceAddress);

                    mTcpServer.start();
                }
            }
        });
        mP2p.registerService();

        mTcpServer = new TcpServer(new TcpServer.Listener() {
            @Override
            public void onClientConnected() {
                mTcpServer.sendLocationUpdate(mLocation.getLastLocation());
            }
        });
        mLocation = new ShipLocation(this, new ShipLocation.Listener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d(Constants.TAG, "Location update: " + location);
                mTcpServer.sendLocationUpdate(location);
            }
        });

        mWifiReceiver = new WifiBroadcastReceiver(new WifiListener() {
            @Override
            public void onWifiP2pStateChanged(int state) {
                setWifiState(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED);
            }

            @Override
            public void onPeersChanged() {
            }

            @Override
            public void onConnectionChanged(Intent intent) {
                mP2p.readGroup();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPermissions.requestFineLocation();

        registerReceiver(mWifiReceiver, mWifiReceiver.getFilter());
        mLocation.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mWifiReceiver);
        mLocation.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTcpServer.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Permissions.REQ_FINE_LOCATION:
                break;
        }
    }

    private void setWifiState(boolean state) {
        mTvWifiState.setText(state
                ? R.string.wifi_on
                : R.string.wifi_off);

        mBtRecreateGroup.setEnabled(state);
    }

    private void GetReferences() {
        mTvWifiState = (TextView) findViewById(R.id.tvWifiState);
        mTvNetworkName = (TextView)findViewById(R.id.tvNetworkName);
        mTvDeviceAddress = (TextView)findViewById(R.id.tvDeviceAddress);

        mBtRecreateGroup = (Button)findViewById(R.id.btRecreateGroup);
    }
}
