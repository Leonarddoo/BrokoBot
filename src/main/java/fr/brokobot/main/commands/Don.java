package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.Player;
import fr.brokobot.main.handler.CheckIn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Don {

    public static MessageEmbed sendDonation(Member senderMember, Member receiverMember, int amount){

        //On recupere le joueur qui envoie et celui qui reçoit les points
        Player sender = Player.retrievePlayer(senderMember.getId());
        Player receiver = Player.retrievePlayer(receiverMember.getId());

        //On regarde si l'envoyeur a assez de BrokoPoints pour faire cela
        MessageEmbed errorMsg = CheckIn.checkPoints(sender, amount);
        if(errorMsg != null) return errorMsg;

        return new EmbedBuilder()
                .setAuthor(senderMember.getEffectiveName(), senderMember.getEffectiveAvatarUrl(), senderMember.getEffectiveAvatarUrl())
                .setThumbnail(receiverMember.getEffectiveAvatarUrl())
                .setColor(Color.GREEN)
                .setDescription("Vous venez de faire un don de "+amount+" "+Main.getBROKOPOINTS()+"à "+senderMember.getAsMention())
                .build();



    }
}
