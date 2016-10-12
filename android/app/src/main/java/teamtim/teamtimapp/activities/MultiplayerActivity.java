package teamtim.teamtimapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.net.InetAddress;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.application.TeamTimApp;
import teamtim.teamtimapp.database.MockDatabase;
import teamtim.teamtimapp.database.WordQuestion;
import teamtim.teamtimapp.managers.MultiPlayerClient;
import teamtim.teamtimapp.network.DefaultNetworkManager;
import teamtim.teamtimapp.network.GameServer;
import teamtim.teamtimapp.network.NetworkManager;

public class MultiplayerActivity extends AppCompatActivity implements WifiP2pManager.ConnectionInfoListener {

    private LinearLayout userList;
    private NetworkManager networkManager;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_friends);
        userList = (LinearLayout) findViewById(R.id.user_list);

        selectedCategory = getIntent().getStringExtra("CATEGORY");
        networkManager = DefaultNetworkManager.getDefault();

        // Show the 'preliminary' devices into the users list
        List<WifiP2pDevice> preliminaryDevices = ((TeamTimApp) getApplication()).getAvailablePeers();
        for (WifiP2pDevice device: preliminaryDevices) {
            createAndAddDeviceButton(device);
        }

        // Make this activity the current listener for new peers (the application will take over again when this activity is destroyed)
        networkManager.setPeerListListener(new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                userList.removeAllViews();
                for (WifiP2pDevice device : peers.getDeviceList()) {
                    createAndAddDeviceButton(device);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        networkManager.removeAllConnectedConnections(new NetworkManager.Then() {
            @Override
            public void then() {
                networkManager.cancelAllAttemptingConnections(null);
            }
        });

        // When this activity is destroyed, let the application handle the new peers and connections once again.
        TeamTimApp app = ((TeamTimApp) getApplication());
        app.becomeActivePeerListListener();
        app.becomeActiveOnConnectedListener();
    }

    private void createAndAddDeviceButton(final WifiP2pDevice device){
        Button butt = new Button(MultiplayerActivity.this);
        butt.setText(device.deviceName);

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToConnectToDevice(device);
            }
        });

        userList.addView(butt, 0);
    }

    private void tryToConnectToDevice(final WifiP2pDevice device) {
        // NOTE: The multiplayer activity will only listen for connection info IF the connection attempt succeeded!
        networkManager.connectToDevice(device, this);
        ProgressDialog.show(this, "Ansluter", "Försöker att ansluta till enheten...", true, false, null);
    }

    @Override
    public void onConnectionInfoAvailable(final WifiP2pInfo info) {
        if (info.groupFormed) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ProgressDialog.show(MultiplayerActivity.this, "Du är ansluten!", "Spelet börjar snart...", true, false, null);
                }
            });

            // Set up server
            setUpMultiplayerServer(info.groupOwnerAddress);

            // Wait some short time then create the local client (to make sure the server is set up and running)
            final int waitTime = 300;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    setUpMultiplayerClient(info.groupOwnerAddress);
                }
            }, waitTime);


        } else {
            System.out.println("MultiplayerActivity: connection info is available but a group hasn't yet formed!");
        }
    }

    private void setUpMultiplayerServer(InetAddress serverAddress) {
        new GameServer(serverAddress).start();
    }

    private void setUpMultiplayerClient(InetAddress serverAddress) {
        final MultiplayerActivity multiplayerActivity = this;

        List<WordQuestion> questions = MockDatabase.getInstance().getQuestions(selectedCategory, -1);
        new MultiPlayerClient("InitiatingClient", serverAddress, questions);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Start the play activity. When it's loaded, it will reach out to the multiplayer client
                Intent intent = new Intent(multiplayerActivity, PlayActivity.class);
                startActivity(intent);
            }
        });
    }

}

