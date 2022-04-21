package fr.brokobot.main.setup;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

    private static final long serialVersionUID = 42L;

    private String id;
    private int win, loose, points;
    private static List<Member> memberList = new ArrayList<>();

    public Member(String id){
        this.id = id;
        this.win = 0;
        this.loose = 0;
        this.points = 20;
    }

    public Member(){
        this.id = null;
        this.win = -1;
        this.loose = -1;
        this.points = -1;
    }

    public static Member getMember(String id){
        for(Member m : memberList){
            if(m.getId().equals(id)) return m;
        }
        Member m = new Member(id);
        memberList.add(m);
        serialize();
        return m;
    }

    public String getId() {
        return id;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public void addWin(){
        this.win++;
        serialize();
    }

    public int getWin() {
        return win;
    }

    public void setLoose(int loose) {
        this.loose = loose;
    }

    public void addLoose(){
        this.loose++;
        serialize();
    }

    public int getLoose() {
        return loose;
    }

    public void setPoints(int points) {
        this.points = points;
        serialize();
    }

    public void addPoints(int points){
        this.points += points;
        serialize();
    }

    public void removePoints(int points){
        this.points -= points;
        serialize();
    }

    public int getPoints() {
        return points;
    }

    public static List<Member> getMemberList() {
        return memberList;
    }

    public static void serialize(){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.ser"))){
            oos.writeObject(memberList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getSerialize(){
        try(ObjectInputStream oos = new ObjectInputStream(new FileInputStream("save.ser"))){
            memberList = (List<Member>) oos.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

}
