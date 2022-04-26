package fr.brokobot.main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable{

    private static final long serialVersionUID = 1L;

    private String id;
    private int win, loose, points;
    private static List<Player> playerList = new ArrayList<>();

    public Player(String id){
        this.id = id;
        this.win = 0;
        this.loose = 0;
        this.points = 50;
    }

    public static Player retrievePlayer(String id){
        for(Player p : playerList){
            if(p.getId().equals(id)) return p;
        }

        Player p = new Player(id);
        playerList.add(p);
        return p;
    }

    public static void save() throws IOException{
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"));
        oos.writeObject(playerList);
    }

    public static void getSave() throws IOException, ClassNotFoundException {
        ObjectInputStream oos = new ObjectInputStream(new FileInputStream("save.ser"));
        playerList = (List<Player>) oos.readObject();
    }


    public String getId() {
        return id;
    }

    public int getWin() {
        return win;
    }

    public int getLoose() {
        return loose;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
