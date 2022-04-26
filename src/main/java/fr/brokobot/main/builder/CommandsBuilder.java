package fr.brokobot.main.builder;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

import javax.swing.text.html.Option;

public class CommandsBuilder extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands()

                //============================
                //       Player Commands
                //============================
                .addCommands(Commands.slash("profil","Afficher le profil du membre.")
                        .addOption(OptionType.USER, "member", "Membre", true))
                .addCommands(Commands.slash("don", "Permet de faire un donner des BrokoPoints à un membre.")
                        .addOption(OptionType.USER, "member","Member", true)
                        .addOption(OptionType.INTEGER, "amount", "Montant", true))
                .addCommands(Commands.slash("broccoli","Permet de récuperer ses BrokoPoints journalier."))

                //============================
                //       Games Commands
                //============================

                .addCommands(Commands.slash("justeprix", "Permet de lancer une partie de JustePrix.")
                        .addOption(OptionType.INTEGER, "bet","Mise", true))

                .queue();
    }
}
