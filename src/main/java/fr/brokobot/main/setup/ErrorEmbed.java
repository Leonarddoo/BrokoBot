package fr.brokobot.main.setup;

import fr.brokobot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ErrorEmbed{

    private static final MessageEmbed notEnoughPoints = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Vous n'avez pas assez de <:brokopoints:962685048748908585> **BrokoPoints** pour faire cela.")
            .build();

    private static final MessageEmbed opponentNotEnoughtPoints = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Votre adversaire n'a pas assez de <:brokopoints:962685048748908585> **BrokoPoints** pour faire cela.")
            .build();

    private static final MessageEmbed mustBePositive = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Le nombre de BrokoPoints doit obligatoirement être positif.")
            .build();

    private static final MessageEmbed sameMember = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Vous ne pouvez pas faire cette action sur vous-même.")
            .build();

    private static final MessageEmbed noPermission = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Vous n'avez pas la permission de faire cela.")
            .build();

    private static final MessageEmbed alreadyIngame = new EmbedBuilder()
            .setColor(Main.getRED())
            .setDescription("Vous avez déjà une partie en cours.")
            .build();



    public static MessageEmbed errorGames(Member m1, Member m2, int b){

        if(b < 1){
            return mustBePositive;
        }

        if(b > m1.getPoints()){
            return notEnoughPoints;
        }

        if(m2 != null){
            if(b > m2.getPoints()){
                return opponentNotEnoughtPoints;
            }
        }
        return null;
    }

    public static MessageEmbed getNotEnoughPoints() {
        return notEnoughPoints;
    }

    public static MessageEmbed getMustBePositive() {
        return mustBePositive;
    }

    public static MessageEmbed getSameMember() {
        return sameMember;
    }

    public static MessageEmbed getNoPermission() {
        return noPermission;
    }

    public static MessageEmbed getAlreadyIngame() {
        return alreadyIngame;
    }
}
