package fr.brokobot.main.handler;

import fr.brokobot.main.Main;
import fr.brokobot.main.commands.Broccoli;
import fr.brokobot.main.commands.Don;
import fr.brokobot.main.commands.Profil;
import fr.brokobot.main.games.JustePrix;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandsHandler extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getChannel().getId().equals(Main.getBROKOCHANNEL())) return;



        switch (event.getName()){
            case "profil":
                event.replyEmbeds(Profil.getProfil(event.getOption("member").getAsMember())).queue();
                break;

            case "don":
                event.replyEmbeds(Don.sendDonation(event.getMember(), event.getOption("member").getAsMember(), event.getOption("amount").getAsInt())).queue();
                break;

            case "broccoli":
                event.replyEmbeds(Broccoli.dailyPoints(event.getUser().getId())).queue();
                break;

            case "justeprix":
                event.replyEmbeds(JustePrix.createGame(event.getGuild(), event.getMember(), event.getOption("bet").getAsInt())).queue();
                break;
        }

    }
}
