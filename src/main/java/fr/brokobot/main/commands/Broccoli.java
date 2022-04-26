package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.Player;
import fr.brokobot.main.builder.MessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Broccoli {

    private static List<Player> dailyList = new ArrayList<>();

    public static MessageEmbed dailyPoints(String id){
        //On recupere le joueur
        Player player = Player.retrievePlayer(id);

        //On regarde si il a déjà reçu son bonus quotidien
        if(dailyList.contains(player)){
            return MessageBuilder.getDailyBonusAlreadyReceived();
        }

        //Si ce n'est pas le cas, on choisi un nombre aléatoire entre 1 et 9
        int amount = new Random().nextInt(10);
        //On lui rajoute ces points
        player.setPoints(player.getPoints()+amount);
        //On le rajoute dans la liste des joueurs ayant reçu leurs BrokoPoints quotidien
        dailyList.add(player);


        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription("Vous venez de recevoir "+amount+" "+ Main.getBROKOPOINTS()+".")
                .build();

    }

    public static List<Player> getDailyList() {
        return dailyList;
    }
}
