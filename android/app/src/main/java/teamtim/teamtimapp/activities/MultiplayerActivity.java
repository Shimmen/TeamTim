package teamtim.teamtimapp.Activities;

import android.content.DialogInterface;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import teamtim.teamtimapp.R;
import teamtim.teamtimapp.network.DefaultNetworkManager;

public class MultiplayerActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);
        userList = (LinearLayout) findViewById(R.id.user_list);

        System.out.println("Begin looking for peers!");
        DefaultNetworkManager.getDefault().beginDiscoveringPeers(new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peers) {
                System.out.println("Available devices:");
                for (WifiP2pDevice device : peers.getDeviceList()) {
                    createButtons(device.toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultNetworkManager.getDefault().stopDiscoveringPeers();
    }

    private void createButtons(String text){
        Button butt = new Button(getApplicationContext());
        butt.setText(text);
        butt.setOnClickListener(this);
        userList.addView(butt);
    }

    @Override
    public void onClick(View v) {
        System.out.print("You clicked me");
    }
}
