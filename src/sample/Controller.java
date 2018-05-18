package sample;

import Network.NetworkManager;
import Network.NetworkScanHost;
import Network.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    NetworkScanHost net = new NetworkScanHost();

    @FXML
    private ImageView point;

    @FXML
    private ImageView grab;

    @FXML
    private ImageView fist;

    @FXML
    private ImageView flat;

    @FXML
    private ImageView two;

//    private ImageView[] imageViews = {point,grab,fist,flat,two};

    @FXML
    private ListView<Player> conList;

    GesturesFactory factory = new GesturesFactory();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        new Thread(()->{
            net.listenToPlayers();
            while (true)
            {

                net.sendMessageToPlayers("connect:Glove");
                net.sendPlayersList();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(net.isPlayersDirty())
                {
                    net.setPlayersDirty(false);
                    Platform.runLater(()->{
                        conList.getItems().clear();
                        conList.getItems().addAll(net.getPlayers().values());
                    });
                }
            }
        }).start();
        setupImageViews();
    }

    private void setupImageViews()
    {
        point.setOnMousePressed(mouseEvent -> {
//            net.sendMessageToPlayers("1");
            net.sendDMessageToPlayers(factory.getGesture(0));
            System.out.println("messageSent");
        });
        two.setOnMousePressed(mouseEvent -> {
            net.sendDMessageToPlayers(factory.getGesture(1));
            System.out.println("messageSent");
        });
        flat.setOnMousePressed(mouseEvent -> {
            net.sendDMessageToPlayers(factory.getGesture(2));
            System.out.println("messageSent");
        });
        fist.setOnMousePressed(mouseEvent -> {
            net.sendDMessageToPlayers(factory.getGesture(3));
            System.out.println("messageSent");
        });
        grab.setOnMousePressed(mouseEvent -> {
            net.sendDMessageToPlayers(factory.getGesture(4));
            System.out.println("messageSent");
        });


//        for(ImageView img : imageViews)
//        {
//            System.out.println("yaa");
//            img.setOnMousePressed(mouseEvent -> {
//                net.sendMessageToPlayers("imagePressed");
//                System.out.println("messageSent");
//            });
//        }
    }
}
