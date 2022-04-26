package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class Profil {

    public static MessageEmbed getProfil(Member member){
        //On recupere le joueur
        Player player = Player.retrievePlayer(member.getId());

        return new EmbedBuilder()
                .setAuthor(member.getEffectiveName(), member.getEffectiveAvatarUrl(), member.getEffectiveAvatarUrl())
                .setColor(Color.BLUE)
                .setDescription("Pour plus d'informations concernant le **BrokoBot**, nous vous invitons à vous rendre dans le channel : <#"+ Main.getPRESENTATIONCHANNEL()+">")
                .addField("<:win:962653285628653619> Victoires :", String.valueOf(player.getWin()), true)
                .addField("<:loser:962652552833421322> Défaites :", String.valueOf(player.getLoose()), true)
                .addField("<:brokopoints:962685048748908585> BrokoPoints :", String.valueOf(player.getPoints()), true)
                .build();
    }
}
