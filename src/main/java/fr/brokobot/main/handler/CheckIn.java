package fr.brokobot.main.handler;

import fr.brokobot.main.Player;
import fr.brokobot.main.builder.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class CheckIn {

    public static MessageEmbed checkPoints(Player player, int amount) {
        if (player.getPoints() < amount) return MessageBuilder.getNotEnoughPoints();
        return null;
    }
}
