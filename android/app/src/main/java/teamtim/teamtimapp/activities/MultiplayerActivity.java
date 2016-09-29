package teamtim.teamtimapp.activities;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.network.DefaultNetworkManager;

public class MultiplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        System.out.println("Begin looking for peers!");
        DefaultNetworkManager.getDefault().beginDiscoveringPeers(new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                System.out.println("Available devices:");
                for (WifiP2pDevice device : peers.getDeviceList()) {
                    System.out.println(device);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultNetworkManager.getDefault().stopDiscoveringPeers();
    }
}
