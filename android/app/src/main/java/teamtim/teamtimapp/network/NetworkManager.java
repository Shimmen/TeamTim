package teamtim.teamtimapp.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

public interface NetworkManager {

    void enableReceiving();
    void disableReceiving();

    void onReceive(Context context, Intent intent);

    void beginDiscoveringPeers(WifiP2pManager.PeerListListener peerListListener);
    void stopDiscoveringPeers();

    public void connectToDevice(WifiP2pDevice device, WifiP2pManager.ConnectionInfoListener connectionInfoListener);
}
