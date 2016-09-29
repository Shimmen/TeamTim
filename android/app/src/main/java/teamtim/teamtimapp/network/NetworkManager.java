package teamtim.teamtimapp.network;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;

public class NetworkManager extends BroadcastReceiver {

    //
    // Static
    //

    private static NetworkManager instance = null;

    public static void initialize(Context appContext) {
        NetworkManager.instance = new NetworkManager(appContext);
    }

    public static NetworkManager getDefault() {
        if (NetworkManager.instance == null) {
            throw new IllegalStateException("NetworkManager must be initialized (initialize()) before the default intstance can be retrieved!");
        }

        return NetworkManager.instance;
    }

    //
    // Instance
    //

    private Context appContext;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private IntentFilter intentFilter;

    private WifiP2pManager.PeerListListener currentPeerListListener;

    private NetworkManager(Context appContext) {
        this.appContext = appContext;

        // Set up manager, channel, and broadcast receiver for incoming and outgoing data
        manager = (WifiP2pManager) appContext.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(appContext, appContext.getMainLooper(), null);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // TODO: Maybe only have receiving enabled in the multiplayer activities?
        enableReceiving();
    }

    public void enableReceiving() {
        appContext.registerReceiver(this, intentFilter);
    }

    public void disableReceiving() {
        appContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        // Check if Wifi P2P is supported on this advice and enabled
        if (action.equals(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                // Wi-Fi P2P is enabled, do nothing
            } else {
                // TODO: Wi-Fi P2P is not enabled, show some prompt alerting the user that it must be enabled, etc.
            }
        }

        if (action.equals(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)) {
            if (currentPeerListListener != null) {
                manager.requestPeers(channel, currentPeerListListener);
            } else {
                System.out.println("No available peer list listener!");
            }
        }

    }

    public void beginDiscoveringPeers(final WifiP2pManager.PeerListListener peerListListener) {
        currentPeerListListener = peerListListener;
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                currentPeerListListener = peerListListener;
            }

            @Override
            public void onFailure(int reason) {
                if (reason == WifiP2pManager.P2P_UNSUPPORTED) {
                    System.err.println("P2P unsupported!");
                } else if (reason == WifiP2pManager.ERROR) {
                    System.err.println("P2P error!");
                } else if (reason == WifiP2pManager.BUSY) {
                    System.err.println("P2P busy!");
                }

                currentPeerListListener = null;
            }
        });
    }

    public void stopDiscoveringPeers() {
        currentPeerListListener = null;
    }
}
