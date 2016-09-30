package teamtim.teamtimapp.network;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;

public class EmulatorNetworkManager implements NetworkManager {

    @Override
    public void enableReceiving() {
        // Do nothing
    }

    @Override
    public void disableReceiving() {
        // Do nothing
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // Do nothing for now
    }

    @Override
    public void beginDiscoveringPeers(WifiP2pManager.PeerListListener peerListListener) {
        // Create fake empty device list
        WifiP2pDeviceList deviceList = new WifiP2pDeviceList();
        peerListListener.onPeersAvailable(deviceList);
    }

    @Override
    public void stopDiscoveringPeers() {
        // Do nothing
    }

}
