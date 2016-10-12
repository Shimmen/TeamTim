package teamtim.teamtimapp.network;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

public interface NetworkManager {

    /**
     * Small interface for simple chained callbacks
     */
    interface Then {
        void then();
    }

    void beginDiscoveringPeers();
    void setPeerListListener(WifiP2pManager.PeerListListener peerListListener);

    void connectToDevice(WifiP2pDevice device, WifiP2pManager.ConnectionInfoListener connectionInfoListener);
    void setOnConnectedListener(WifiP2pManager.ConnectionInfoListener connectionInfoListener);
    void cancelAllAttemptingConnections(Then then);
    void removeAllConnectedConnections(Then then);

}
