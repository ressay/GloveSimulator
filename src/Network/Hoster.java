package Network;


import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ressay on 09/06/17.
 */

public class Hoster  {

    LinkedList<Player> players = new LinkedList<>();
    NetworkScanHost net = new NetworkScanHost();


    protected Void doInBackground(Void... lists) {

        net.listenToPlayers();
        while (true)
        {
//            if(isCancelled())
//            {
//                net.cancel();
//                net.sendMessageToPlayers("disconnect:"+MainActivity.getNickName());
//                return null;
//            }
            net.sendPlayersList();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(net.isPlayersDirty())
            {
                net.setPlayersDirty(false);
            }
        }

    }




}