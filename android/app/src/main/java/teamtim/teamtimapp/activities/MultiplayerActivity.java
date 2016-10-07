package teamtim.teamtimapp.activities;

import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.managers.MultiPlayerClient;
import teamtim.teamtimapp.managers.OnResultCallback;
import teamtim.teamtimapp.network.DefaultNetworkManager;
import teamtim.teamtimapp.network.GameServer;
import teamtim.teamtimapp.network.NetworkManager;

public class MultiplayerActivity extends AppCompatActivity {

    private LinearLayout userList;
    private NetworkManager networkManager;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        selectedCategory = getIntent().getStringExtra("CATEGORY");

        userList = (LinearLayout) findViewById(R.id.user_list);

        networkManager = DefaultNetworkManager.getDefault();

        // Enable receiving of P2P events. This should be enabled for as long P2P traffic is going on.
        networkManager.enableReceiving();

        System.out.println("Begin looking for peers!");
        networkManager.beginDiscoveringPeers(new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                if (peers == null) {
                    Toast.makeText(MultiplayerActivity.this, "Ett fel uppstod, vänligen försök igen!", Toast.LENGTH_LONG).show();
                } else {
                    for (WifiP2pDevice device : peers.getDeviceList()) {
                        createAndAddDeviceButton(device);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        networkManager.stopDiscoveringPeers();
    }

    private void createAndAddDeviceButton(final WifiP2pDevice device){
        Button butt = new Button(MultiplayerActivity.this);
        butt.setText(device.deviceName);

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                networkManager.stopDiscoveringPeers();
                networkManager.connectToDevice(device, new WifiP2pManager.ConnectionInfoListener() {
                    @Override
                    public void onConnectionInfoAvailable(WifiP2pInfo info) {
                        if (info == null) {
                            Toast.makeText(MultiplayerActivity.this, "Kunde inte ansluta till enheten!", Toast.LENGTH_SHORT).show();
                        } else {
                            if (info.groupFormed) {
                                setUpMultiplayerGame(info.groupOwnerAddress, info.isGroupOwner);
                            } else {
                                // TODO: Why would it not be able to form a group? Anyway, handle this somehow...
                            }
                        }
                    }
                });

            }
        });

        userList.addView(butt, 0);
    }

    /**
     * Sets up a multi-player game.
     *
     * @param serverAddress The InetAddress of the server
     * @param isServerDevice Indicates if this call if of a
     */
    private void setUpMultiplayerGame(InetAddress serverAddress, boolean isServerDevice) {

        if (isServerDevice) {
            new GameServer(serverAddress, MockDatabase.getInstance().getQuestions(selectedCategory, -1)).start();
        }
        OnResultCallback rc = new MultiPlayerClient(serverAddress);
        Intent intent = new Intent(this, PlayActivity.class);
        intent.putExtra("LISTENER", rc);
        startActivity(intent);

        Toast.makeText(MultiplayerActivity.this, "Du är nu ansluten som " + ((isServerDevice) ? "client/host!" : "client!"),
                Toast.LENGTH_LONG).show();
    }

}

