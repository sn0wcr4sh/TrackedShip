package sk.sn0wcr4sh.trackedship;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sn0wcr4sh on 9/11/2017.
 *
 */


class P2pManager {

    interface Listener {
        void onGroupUpdate(WifiP2pGroup group);
    }

    private Listener mListener;
    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;

    public P2pManager(Context context, Listener listener) {
        mListener = listener;

        mManager = (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(context, context.getMainLooper(), null);
    }

    void ensureGroup() {
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                Log.d(Constants.TAG, "Current group\n" + group);

                if (group != null)
                    removeGroup();
                else
                    createGroup();
            }
        });
    }

    void registerService() {
        Map record = new HashMap();
        record.put("listen_port", String.valueOf(TcpServer.PORT));

        WifiP2pDnsSdServiceInfo serviceInfo =
                WifiP2pDnsSdServiceInfo.newInstance("tracked_ship", "tsp._tcp", record);
        mManager.addLocalService(mChannel, serviceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(Constants.TAG, "Ship service added");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(Constants.TAG, "Failed to create local service: " + reason);
            }
        });
    }

    void readGroup() {
        Log.d(Constants.TAG, "Read group because connection changed");
        mManager.requestGroupInfo(mChannel, new WifiP2pManager.GroupInfoListener() {
            @Override
            public void onGroupInfoAvailable(WifiP2pGroup group) {
                mListener.onGroupUpdate(group);
            }
        });
    }

    private void removeGroup() {
        mManager.removeGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(Constants.TAG, "Group removed.");
                createGroup();
            }

            @Override
            public void onFailure(int reason) {
                Log.d(Constants.TAG, "Remove group failed: " + reason);
            }
        });
    }

    private void createGroup() {
        mManager.createGroup(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(Constants.TAG, "Group created.");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(Constants.TAG, "Create group failed: " + reason);
            }
        });
    }
}
