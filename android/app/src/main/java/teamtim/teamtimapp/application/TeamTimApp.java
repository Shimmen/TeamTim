package teamtim.teamtimapp.application;

import android.app.Application;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import teamtim.teamtimapp.activities.PlayActivity;
import teamtim.teamtimapp.managers.MultiPlayerClient;
import teamtim.teamtimapp.network.DefaultNetworkManager;

public class TeamTimApp extends Application implements WifiP2pManager.ConnectionInfoListener {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Application created!");

        DefaultNetworkManager.initialize(getApplicationContext());
        //DefaultNetworkManager.getDefault().setOnConnectedListener(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        System.out.println("Application terminated!");

        DefaultNetworkManager.shutdown();
    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
/*
        if (info.groupFormed) {
            System.out.println("TeamTimApp: setting up multiplayer game!");

            // TODO: Start the play game activity and let the MultiPlayerClient manage it.
            MultiPlayerClient mpc = new MultiPlayerClient(info.groupOwnerAddress);
            Intent intent = new Intent(TeamTimApp.this, PlayActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
*/
    }
}
