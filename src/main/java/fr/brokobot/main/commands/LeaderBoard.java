package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoard {

    private static Member maxWins;
    private static Member maxLoose;
    private static Member maxPoints;
    private static List<Member> winsList;
    private static List<Member> looseList;
    private static List<Member> pointList;
    private static StringBuilder leaderWins;
    private static StringBuilder leaderLoose;
    private static StringBuilder leaderPoint;

    public static MessageEmbed getLeaderBoard(){
        int loop = Math.min(Member.getMemberList().size(), 10);

        winsList = new ArrayList<>();
        looseList = new ArrayList<>();
        pointList = new ArrayList<>();
        leaderWins = new StringBuilder();
        leaderLoose = new StringBuilder();
        leaderPoint = new StringBuilder();

        for(int i = 0 ; i < loop ; i++) {
            maxWins = new Member();
            maxLoose = new Member();
            maxPoints = new Member();
            for (Member member : Member.getMemberList()) {
                if (member.getWin() >= maxWins.getWin() && !(winsList.contains(member) )) {
                    maxWins = member;
                }
                if (member.getLoose() >= maxLoose.getLoose() && !(looseList.contains(member) )){
                    maxLoose = member;
                }
                if (member.getPoints() >= maxPoints.getPoints() && !(pointList.contains(member) )){
                    maxPoints = member;
                }
            }
            winsList.add(maxWins);
            leaderWins.append("**"+(i+1)+"** : <@"+maxWins.getId()+"> : "+maxWins.getWin()+"\n");
            looseList.add(maxLoose);
            leaderLoose.append("<@"+maxLoose.getId()+"> : "+maxLoose.getLoose()+"\n");
            pointList.add(maxPoints);
            leaderPoint.append("<@"+maxPoints.getId()+"> : "+maxPoints.getPoints()+"\n");
        }

        return new EmbedBuilder()
                .setColor(Main.getGREEN())
                .setDescription("__Classement des **BrokoMembres**__")
                .addField("Victoires :", leaderWins.toString(), true)
                .addField("Défaites :", leaderLoose.toString(), true)
                .addField("BrokoPoints :", leaderPoint.toString(), true)
                .build();
    }



}
