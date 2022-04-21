package fr.brokobot.main.setup;

import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;

public class CommandsBuilder extends ListenerAdapter {

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands()

                .addCommands(Commands.slash("setpoints", "Permet de forcer le nombre de BrokoPoints d'un membre.")
                        .addOption(OptionType.USER, "member", "Membre", true)
                        .addOption(OptionType.INTEGER,"amount", "Montant", true))

                .addCommands(Commands.slash("setwin", "Permet de forcer le nombre de victoires d'un membre.")
                        .addOption(OptionType.USER, "member", "Membre", true)
                        .addOption(OptionType.INTEGER,"amount", "Montant", true))

                .addCommands(Commands.slash("setloose", "Permet de forcer le nombre de défaites d'un membre.")
                        .addOption(OptionType.USER, "member", "Membre", true)
                        .addOption(OptionType.INTEGER,"amount", "Montant", true))

                .addCommands(Commands.slash("info", "Permet d'afficher les informations du BrokoBot."))

                .addCommands(Commands.slash("boutique", "Permet de visualiser la boutique."))

                .addCommands(Commands.slash("profil", "Permet d'afficher le BrokoProfil d'un membre.'")
                        .addOption(OptionType.USER, "member", "Membre", true))

                .addCommands(Commands.slash("broccoli", "Permet de récuperer des BrokoPoints chaque jour."))

                .addCommands(Commands.slash("don", "Permet de faire un don à une personne.")
                        .addOption(OptionType.USER, "member", "Membre", true)
                        .addOption(OptionType.INTEGER, "amount", "Montant", true))

                .addCommands(Commands.slash("classement", "Permet d'afficher le classement des Membres ayant le plu de victoires."))

                .addCommands(Commands.slash("justeprix", "Permet de lancer une partie de juste prix.")
                        .addOption(OptionType.INTEGER, "amount", "Mise", true))

                .addCommands(Commands.slash("chifoumi", "Permet de lancer une partie de chifoumi.")
                        .addOption(OptionType.USER, "member", "Membre", true)
                        .addOption(OptionType.INTEGER, "bet", "Mise", true))

                .queue();
    }
}
