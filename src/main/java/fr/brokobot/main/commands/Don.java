package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.ErrorEmbed;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Don {

    public static MessageEmbed doDon(Member sender, Member receiver, int amount){

        if(sender.getId().equals(receiver.getId())) return ErrorEmbed.getSameMember();

        if(amount < 1) return ErrorEmbed.getMustBePositive();

        if(amount > sender.getPoints()) return ErrorEmbed.getNotEnoughPoints();

        sender.removePoints(amount);
        receiver.addPoints(amount);
        return new EmbedBuilder()
                .setColor(Main.getGREEN())
                .setDescription("Vous venez d'envoyer <:brokopoints:962685048748908585> "+amount+" **BrokoPoints** à <@"+receiver.getId()+">")
                .build();

    }
}
