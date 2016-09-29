package teamtim.teamtimapp.activities;

import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.network.NetworkManager;

public class MultiplayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        System.out.println("Begin looking for peers!");
        NetworkManager.getDefault().beginDiscoveringPeers(new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                System.out.println("Peers: " + peers.getDeviceList());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getDefault().stopDiscoveringPeers();
    }
}
