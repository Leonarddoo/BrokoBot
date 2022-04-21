package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class Shop {

    private Member member;
    private String message;

    private static final List<Shop> shopList = new ArrayList<>();

    public Shop() {}

    public Shop(Member m, String msg){
        this.member = m;
        this.message = msg;
    }

    public static MessageEmbed printShop(net.dv8tion.jda.api.entities.Member member){
        return new EmbedBuilder()
                .setAuthor(member.getEffectiveName(), member.getEffectiveAvatarUrl())
                .setTitle("Menu Shop")
                .setColor(Main.getBLUE())
                .setDescription("Ce menu boutique sera automatiquement supprimé dans 3 minutes ou après l'achat d'un objet.\n" +
                        "Choisissez le menu dans lequel vous souhaitez vous rendre.")
                .build();
    }

    public static Shop retriveBoutiqueByMember(String id){
        for(Shop b : shopList){
            if(b.member.getId().equals(id)){
                return b;
            }
        }
        return null;
    }

    public static List<Shop> getShopList() {
        return shopList;
    }
}
