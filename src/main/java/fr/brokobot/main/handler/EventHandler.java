package fr.brokobot.main.handler;

import fr.brokobot.main.games.JustePrix;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventHandler extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(!event.getChannelType().equals(ChannelType.TEXT)) return;
        if(event.getTextChannel().getName().contains("justeprix")){
            JustePrix jp = JustePrix.retrieveJPByChannelID(event.getChannel().getId());
            if(jp != null) {
                JustePrix.updateGame(jp, event.getTextChannel(), event.getMessage());
            }
        }

    }
}
