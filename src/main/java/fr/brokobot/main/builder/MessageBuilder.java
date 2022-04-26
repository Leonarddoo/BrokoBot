package fr.brokobot.main.builder;

import fr.brokobot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class MessageBuilder {

    private static final MessageEmbed notEnoughPoints = new EmbedBuilder()
            .setColor(Color.RED)
            .setDescription("Désolé vous n'avez pas assez de "+ Main.getBROKOPOINTS())
            .build();

    private static final MessageEmbed dailyBonusAlreadyReceived = new EmbedBuilder()
            .setColor(Color.RED)
            .setDescription("Vous avez déjà reçu vos "+Main.getBROKOPOINTS()+" de la journée.")
            .build();

    private static final MessageEmbed alreadyInGame = new EmbedBuilder()
            .setColor(Color.RED)
            .setDescription("Vous avez déjà une game en cours.")
            .build();

    public static MessageEmbed getNotEnoughPoints() {
        return notEnoughPoints;
    }

    public static MessageEmbed getDailyBonusAlreadyReceived() {
        return dailyBonusAlreadyReceived;
    }

    public static MessageEmbed getAlreadyInGame() {
        return alreadyInGame;
    }
}
