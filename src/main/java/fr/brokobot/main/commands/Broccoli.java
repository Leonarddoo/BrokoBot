package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Broccoli extends ListenerAdapter {

    private static final MessageEmbed alreadyReceive = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Vous avez déjà reçu vos <:brokopoints:962685048748908585> **BrokoPoints** de la journée.")
            .build();

    private static final MessageEmbed receive = new EmbedBuilder()
            .setColor(Main.getGREEN())
            .setDescription("Vous venez de recevoir vos <:brokopoints:962685048748908585> **BrokoPoints** de la journée.")
            .build();

    public static MessageEmbed doBroccoli(Member member){
        if(Main.getDailyList().contains(member.getId())){
            return alreadyReceive;
        }

        member.addPoints(5);
        Main.getDailyList().add(member.getId());
        return receive;
    }
}
