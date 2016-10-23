package teamtim.teamtimapp.application;

import android.app.Application;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.managers.MultiPlayerClient;
import teamtim.teamtimapp.network.DefaultNetworkManager;
import teamtim.teamtimapp.network.NetworkManager;

public class TeamTimApp extends Application implements WifiP2pManager.ConnectionInfoListener {

    NetworkManager networkManager;
    List<WifiP2pDevice> availableDevices = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Application created!");

        this.networkManager = DefaultNetworkManager.initialize(getApplicationContext());
        try {
            MockDatabase.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Become the active listener for when a connection is established
        becomeActiveOnConnectedListener();

        // Become the active peer list listener and start listening for peers
        becomeActivePeerListListener();
        networkManager.beginDiscoveringPeers();
    }

    public void becomeActiveOnConnectedListener() {
        if(networkManager != null) {
            networkManager.setOnConnectedListener(this);
        }
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        if (info.groupFormed) {

            // Wait some short time then create the local client (to make sure the server is set up and running)
            final int waitTime = 300;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    createMultiplayerClient(info.groupOwnerAddress);
                }
            }, waitTime);

        } else {
            System.out.println("TeamTimApp: connection info is available but a group hasn't yet formed!");
        }
    }

    private void createMultiplayerClient(InetAddress serverAddress) {
        // Create the multiplayer client. No need to keep a reference to it since it's stored statically
        new MultiPlayerClient("ExternalClient", serverAddress, null);

        Looper.prepare();
        new Handler(getApplicationContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TeamTimApp.this, "Du är nu ansluten! Spelet börjar snart.", Toast.LENGTH_LONG).show();
            }
        });

        // Start the play activity. When it's loaded, it will reach out to the multiplayer client
        Intent intent = new Intent(TeamTimApp.this, PlayActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void becomeActivePeerListListener() {
        // Invalidate the old list since it probably isn't up to date anymore
        availableDevices.clear();

        if (networkManager != null) {
            networkManager.setPeerListListener(new WifiP2pManager.PeerListListener() {
                @Override
                public void onPeersAvailable(WifiP2pDeviceList peers) {
                    availableDevices.clear();
                    availableDevices.addAll(peers.getDeviceList());
                }
            });
        }
    }

    public List<WifiP2pDevice> getAvailablePeers() {
        return availableDevices;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("Application terminated!");

        DefaultNetworkManager.shutdownSystem();
    }

}
