package fr.brokobot.main.commands;

import fr.brokobot.main.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Info extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("info")) return;
        if(! (event.getChannel().getId().equals(Main.getBROKOCHANNEL()) || event.getChannel().getId().equals("962704792189304852")) ) return;

        event.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setColor(Main.getBLUE())
                        .setTitle("Information sur le BrokoBot")
                        .setDescription("**__BrokoBot qu'est ce que c'est ?__**\n" +
                                "Le BrokoBot est un système de mini-jeux qui vous permet de gagner ou perdre des <:brokopoints:962685048748908585> **BrokoPoints**.\n" +
                                "Vous pouvez voir votre nombre de <:brokopoints:962685048748908585> **BrokoPoints** avec la commande **/profil**\n" +
                                "\n" +
                                "**__Comment gagner des <:brokopoints:962685048748908585> BrokoPoints ?__**\n" +
                                "Les <:brokopoints:962685048748908585> **BrokoPoints** s'obtiennent à l'issue de mini-jeux tel que le **/justeprix**. Il vous est aussi possible d'en récuperer chaque jours avec le **/broccoli**.\n" +
                                "\n" +
                                "**__Que faire avec mes points ?__**\n" +
                                "Il vous est possible de dépenser vos <:brokopoints:962685048748908585> **BrokoPoints** dans le **/boutique** afin d'acheter des rôles.\n" +
                                "Ces rôles permettent d'obtenir des couleurs ou des émojis sur votre pseudo lorsque vous envoyer des messages.\n" +
                                "\n" +
                                "*L'intégralité des commandes fonctionnent uniquement dans le channel : <#"+Main.getBROKOCHANNEL()+">*\n" +
                                "__*Crédits :*__\n" +
                                "*Graphisme : <@567941127760642082>*\n" +
                                "*Mascotte : <@377430376345501698>*")
                .build()).queue();
    }
}
