package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Profil{

    public static MessageEmbed getProfil(net.dv8tion.jda.api.entities.Member m){
        Member member = Member.getMember(m.getId());
        return new EmbedBuilder()
                .setTitle(m.getEffectiveName()+"#"+m.getId())
                .setColor(Main.getGREEN())
                .setDescription("Pour plus d'informations concernant le **BrokoBot**, rendez vous sur la <#962704792189304852>")
                .addField("<:win:962653285628653619> Victoires :", Integer.toString(member.getWin()), true)
                .addField("<:loser:962652552833421322> Défaites :", Integer.toString(member.getLoose()), true)
                .addField("<:brokopoints:962685048748908585> BrokoPoints :", Integer.toString(member.getPoints()), true)
                .setThumbnail(m.getEffectiveAvatarUrl())
                .build();
    }

}
