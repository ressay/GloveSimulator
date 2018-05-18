package Network;


import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by ressay on 09/06/17.
 */

public class NetworkScanHost
{
    static public class Host
    {
        private InetAddress address = null;
        private String name = "";
        Host(InetAddress a,String n)
        {
            name = n;
            address = a;
        }

        public InetAddress getAddress()
        {
            return address;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString() {
            return getName();
        }
    }



    private HashMap<String,Host> hosts = new HashMap<>();



    private HashMap<String,Player> players = new HashMap<>();
    private boolean hostsDirty = false;


    private boolean playersDirty = false;
    private boolean cancel = false;
    static int servers_port = NetworkManager.servers_port;
    static int servers_Dport = NetworkManager.servers_Dport;
    static int players_port = NetworkManager.players_port;
    NetworkManager net = NetworkManager.getInstance();
    NetworkManager.ListenerCallBack action;

    public void listenToHosts() {

        action = net.listen(servers_port, (receivedMessage, sender) ->
        {
            String text = receivedMessage;
            if(text.matches("connect:(.*)") && !hosts.containsKey(sender.getHostAddress())) {
                setHostsDirty(true);
                hosts.put(sender.getHostAddress(),new Host(sender, text.split(":")[1]));
            }
            else if(text.matches("disconnect:(.*)") && hosts.containsKey(sender.getHostAddress()))
            {
                setHostsDirty(true);
                hosts.remove(sender.getHostAddress());
            }
        });

    }

    public void listenToPlayers() {

        action = net.listen(players_port, (receivedMessage, sender) ->
        {
            String text = receivedMessage;
            if(text.matches("connect:(.*)") && !players.containsKey(sender.getHostAddress())) {
                setPlayersDirty(true);
                players.put(sender.getHostAddress(),new Player(sender, text.split(":")[1]));
                sendPlayersList();
                System.out.println(receivedMessage);
            }
            else if(text.matches("disconnect:(.*)") && players.containsKey(sender.getHostAddress()))
            {
                setPlayersDirty(true);
                players.remove(sender.getHostAddress());
                sendPlayersList();
                System.out.println(receivedMessage);
            }
        });
    }

    public void sendPlayersList()
    {
        String playerString = "players:\n";
        Iterator<Player> playerIterator = players.values().iterator();
        for(;playerIterator.hasNext();)
            playerString += playerIterator.next().nickName+"\n";
        sendMessageToPlayers(playerString);
    }

    static public String[] parsePlayers(String pl)
    {
        String[] p = pl.split("\n");
        String[] players = new String[p.length-1];
        for(int i=1;i<p.length;i++)
            players[i-1] = p[i];
        return players;
    }

    public void sendMessageToHost(String message,Host host) {
        net.sendMessage(message,players_port,host.getAddress());
    }

    public void sendMessageToPlayers(String message) {
        net.broadCastMessage(message,servers_port);
    }

    public void sendDMessageToPlayers(double[] message) {
        net.broadDCastMessage(message,servers_Dport);
    }

    public void broadCastMessage(String message,int port) {
        try
        {
            sendMessage(message,port,InetAddress.getByName("255.255.255.255"));
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message,int port,InetAddress address) {
        net.sendMessage(message,port,address);
    }

    public void cancel()
    {
        net.cancelAction(action);
        cancel = true;
    }

    public HashMap<String, Host> getHosts() {
        return hosts;
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public boolean isHostsDirty() {
        return hostsDirty;
    }

    public void setHostsDirty(boolean hostsDirty) {
        this.hostsDirty = hostsDirty;
    }


    public boolean isPlayersDirty() {
        return playersDirty;
    }

    public void setPlayersDirty(boolean playersDirty) {
        this.playersDirty = playersDirty;
    }
}
