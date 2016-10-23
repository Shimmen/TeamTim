package teamtim.teamtimapp.network;

import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EmulatorNetworkManager implements NetworkManager {

    @Override
    public void beginDiscoveringPeers() {

    }

    @Override
    public void setPeerListListener(WifiP2pManager.PeerListListener peerListListener) {
        try {

            // Create fake device list
            ArrayList<WifiP2pDevice> devices = new ArrayList<>();
            for (int i = 0; i < 19; i++) {
                devices.add(createTestDevice(i));
            }

            WifiP2pDeviceList deviceList = WifiP2pDeviceList.class.getConstructor(ArrayList.class).newInstance(devices);
            peerListListener.onPeersAvailable(deviceList);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WifiP2pDevice createTestDevice(int id) {
        WifiP2pDevice device = new WifiP2pDevice();

        device.deviceAddress = "INVALID_TEST_ADDRESS_" + id;
        device.deviceName = "TestDevice_" + id;

        return device;
    }

    @Override
    public void connectToDevice(WifiP2pDevice device, final WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    WifiP2pInfo info = new WifiP2pInfo();
                    info.groupFormed = true;

                    // Must run on non-main thread!
                    info.groupOwnerAddress = InetAddress.getLocalHost();
                    info.isGroupOwner = true;

                    connectionInfoListener.onConnectionInfoAvailable(info);

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void setOnConnectedListener(WifiP2pManager.ConnectionInfoListener connectionInfoListener) {
        // Do nothing
    }

    @Override
    public void cancelAllAttemptingConnections(Then then) {
        if (then != null) {
            then.then();
        }
    }

    @Override
    public void removeAllConnectedConnections(Then then) {
        if (then != null) {
            then.then();
        }
    }

}
