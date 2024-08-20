package Logic;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import Menus.Observer;

public class PlayerModel implements Serializable {

    public static ArrayList<PlayerModel> players;
    private String name;
    private int money;

    public PlayerModel(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public static void loadSavedPlayers(){
        players = new ArrayList<>();
        PlayerModel playerModel;
        try {
            String userHome = System.getProperty("user.home");
            File file = new File(userHome, "save.txt");
            if(!file.createNewFile()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    while ((playerModel = (PlayerModel) objectInputStream.readObject()) != null) {
                        players.add(playerModel);
                        System.out.println("added player: " + playerModel.getName());
                    }
                } catch (EOFException e){
                    System.out.println("end of file");
                }
            } else {
                System.out.println("no existing save file - brand new one created");
            }

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public static void savePlayers(){
        String userHome = System.getProperty("user.home");
        File file = new File(userHome, "save.txt");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()))) {
            for(PlayerModel playerModel : PlayerModel.players) {
                out.writeObject(playerModel);
                System.out.println("saved player " + playerModel.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
        savePlayers();
    }

    @Override
    public String toString(){
        return name + " === " + money;
    }
}
