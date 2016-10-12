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
import android.widget.Toast;

import teamtim.teamtimapp.util.ThisApp;

public class DefaultNetworkManager extends BroadcastReceiver implements NetworkManager {

    //
    // Static
    //

    private static NetworkManager instance = null;

    public static NetworkManager initialize(Context appContext) {
        if (ThisApp.isRunningOnRealDevice()) {
            DefaultNetworkManager.instance = new DefaultNetworkManager(appContext);
        } else {
            DefaultNetworkManager.instance = new EmulatorNetworkManager();
        }

        return instance;
    }

    public static void shutdownSystem() {
        instance.removeAllConnectedConnections(new Then() {
            @Override
            public void then() {
                instance.cancelAllAttemptingConnections(null);
            }
        });

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

    private WifiP2pManager.PeerListListener currentPeerListListener;
    private WifiP2pManager.ConnectionInfoListener currentConnectionInfoListener;

    private DefaultNetworkManager(Context appContext) {
        this.appContext = appContext;
        manager = (WifiP2pManager) appContext.getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(appContext, appContext.getMainLooper(), null);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        // Enable receiving of events to this broadcast receiver. This should be enabled as long as there is WifiP2P traffic.
        appContext.registerReceiver(this, intentFilter);
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
                System.err.println("No available peer list listener! There should always be one listener!");
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
    public void beginDiscoveringPeers() {
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Do nothing
            }

            @Override
            public void onFailure(int reason) {
                onFailureHandler("Peer discovery", reason);
            }
        });
    }

    @Override
    public void setPeerListListener(WifiP2pManager.PeerListListener peerListListener) {
        this.currentPeerListListener = peerListListener;
    }

    @Override
    public void connectToDevice(WifiP2pDevice device, final WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.groupOwnerIntent = 15; // WifiP2pConfig.MAX_GROUP_OWNER_INTENT;
        config.wps.setup = WpsInfo.PBC; // Use simple push button configuration to connect

        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // The connection request was successfully initiated. Set the connection info listener to this one.
                setOnConnectedListener(connectionInfoListener);
            }

            @Override
            public void onFailure(int reason) {
                // The connection request couldn't be initiated. Don't change the current on connected listener (so that the application is still doing that)
                onFailureHandler("Connecting to device", reason);
            }
        });
    }

    @Override
    public void cancelAllAttemptingConnections(final Then then) {
        manager.cancelConnect(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (then != null) {
                    then.then();
                }
            }

            @Override
            public void onFailure(int reason) {
                onFailureHandler("Cancel connect", reason);
            }
        });
    }

    @Override
    public void setOnConnectedListener(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        this.currentConnectionInfoListener = connectionInfoListener;
    }

    @Override
    public void removeAllConnectedConnections(final Then then) {
        manager.removeGroup(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                if (then != null) {
                    then.then();
                }
            }

            @Override
            public void onFailure(int reason) {
                onFailureHandler("Remove group", reason);
            }
        });
    }

    private void onFailureHandler(String context, int reason) {
        // TODO: These aren't really user presentable error messages!
        String message = "Error (" + context + "): ";
        if (reason == WifiP2pManager.P2P_UNSUPPORTED){
            message += "P2P not supported on this device!";
        } else if (reason == WifiP2pManager.BUSY){
            message += "P2P is currently busy!";
        } else if (reason == WifiP2pManager.ERROR){
            message += "P2P error!";
        }

        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show();
    }
}
