package teamtim.teamtimapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

import teamtim.teamtimapp.util.ThisApp;

public class DefaultNetworkManager extends BroadcastReceiver implements NetworkManager {

    //
    // Static
    //

    private static NetworkManager instance = null;

    public static void initialize(Context appContext) {
        if (ThisApp.isRunningOnRealDevice()) {
            DefaultNetworkManager.instance = new DefaultNetworkManager(appContext);
        } else {
            DefaultNetworkManager.instance = new EmulatorNetworkManager();
        }
    }

    public static void shutdown() {
        instance.stopDiscoveringPeers();
        instance.disableReceiving();
        instance = null;
    }

    public static NetworkManager getDefault() {
        if (DefaultNetworkManager.instance == null) {
            throw new IllegalStateException("NetworkManager must be initialized (initialize()) before the default instance can be retrieved!");
        }

        return DefaultNetworkManager.instance;
    }

    //
    // Instance
    //

    private Context appContext;
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private IntentFilter intentFilter;

    private WifiP2pManager.PeerListListener currentPeerListListener;
    private WifiP2pManager.ConnectionInfoListener currentConnectionInfoListener;

    private DefaultNetworkManager(Context appContext) {
        this.appContext = appContext;

        manager = (WifiP2pManager) appContext.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(appContext, appContext.getMainLooper(), null);

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    public void enableReceiving() {
        appContext.registerReceiver(this, intentFilter);
    }

    @Override
    public void disableReceiving() {
        appContext.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);

            if (state == WifiP2pManager.WIFI_P2P_STATE_DISABLED) {
                System.out.println("Can't use WifiP2P since it's disabled!");
            }
        }

        if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (currentPeerListListener != null) {
                manager.requestPeers(channel, currentPeerListListener);
            } else {
                System.out.println("No available peer list listener! (This is not an error, just memo!)");
            }
        }

        if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            // If the network is fully connected and ready for data transfer, get connection info
            if (networkInfo.isConnected()) {
                if (manager != null) {
                    manager.requestConnectionInfo(channel, currentConnectionInfoListener);
                } else {
                    System.out.println("A connection has been established but there is no connection info listener!");
                }
            }
        }

        if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Relevant?
        }

    }

    @Override
    public void beginDiscoveringPeers(final WifiP2pManager.PeerListListener peerListListener) {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                currentPeerListListener = peerListListener;
            }

            @Override
            public void onFailure(int reason) {
                onFailureHandler(reason);
                currentPeerListListener = null;

                // To signify to the callee that there was an error and that no peers will be available
                peerListListener.onPeersAvailable(null);
            }
        });
    }

    @Override
    public void stopDiscoveringPeers() {
        currentPeerListListener = null;
    }

    @Override
    public void connectToDevice(WifiP2pDevice device, final WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.groupOwnerIntent = 15; //WifiP2pConfig.MAX_GROUP_OWNER_INTENT;
        config.wps.setup = WpsInfo.PBC; // Use simple push button configuration to connect

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                currentConnectionInfoListener = connectionInfoListener;
            }

            @Override
            public void onFailure(int reason) {
                onFailureHandler(reason);
                currentPeerListListener = null;

                // To signify to the callee that there was an error and that no connection info will be available
                connectionInfoListener.onConnectionInfoAvailable(null);
            }
        });
    }

    @Override
    public void setOnConnectedListener(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        this.currentConnectionInfoListener = connectionInfoListener;
    }

    private void onFailureHandler(int reason) {
        if (reason == WifiP2pManager.P2P_UNSUPPORTED){
            System.out.println("P2P not supported on this device!");
        } else if (reason == WifiP2pManager.BUSY){
            System.out.println("P2P is currently busy!");
        } else if (reason == WifiP2pManager.ERROR){
            System.out.println("Unknown P2P error!");
        }
    }
}
