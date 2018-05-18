package Network;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ressay on 06/06/17.
 */

public class Scanner {

    LinkedList<String> servers = new LinkedList<>();
    NetworkScanHost net = new NetworkScanHost();
    NetworkScanHost.Host selectedHost = null;


    public void selectHost(NetworkScanHost.Host host)
    {
        selectedHost = host;
    }

    protected Void doInBackground(Void... lists)
    {
        net.listenToHosts();
        while (true)
        {
            if(selectedHost != null)
            {
                //net.sendMessageToHost("connect:"+MainActivity.getNickName(),selectedHost);
                net.cancel();
                return null;
            }

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(net.isHostsDirty()) {
                net.setHostsDirty(false);
            }
        }
    }

}
