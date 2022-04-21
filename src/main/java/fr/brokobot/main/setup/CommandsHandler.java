package fr.brokobot.main.setup;

import fr.brokobot.main.Main;
import fr.brokobot.main.commands.*;
import fr.brokobot.main.games.JustePrix;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandsHandler extends ListenerAdapter {

    private static Member author;
    private static Member other;
    private static int amount;


    private static final List<String> admCommands = new ArrayList(Arrays.asList("setpoints", "setwin", "setloose"));

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        author = Member.getMember(event.getUser().getId());
        if(event.getOptionsByType(OptionType.USER).size() > 0){
            other = Member.getMember(event.getOption("member").getAsUser().getId());
        }
        if(event.getOptionsByType(OptionType.INTEGER).size() > 0){
            amount = event.getOption("amount").getAsInt();
        }

        if(admCommands.contains(event.getName())){
            if(event.getMember().hasPermission(Permission.ADMINISTRATOR)){
                event.replyEmbeds(ErrorEmbed.getNoPermission()).queue();
                return;
            }

            switch (event.getName()){

                case "setpoints":
                    other.setPoints(amount);
                    event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getGREEN())
                            .setDescription("<@"+other.getId()+"> est maintenant à <:brokopoints:962685048748908585> "+other+" **BrokoPoints**.")
                            .build()).queue();
                    break;

                case "setwin":
                    other.setWin(amount);
                    event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getGREEN())
                            .setDescription("<@"+other.getId()+"> est maintenant à "+other+" victoires.")
                            .build()).queue();
                    break;

                case "setloose":
                    other.setLoose(amount);
                    event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getGREEN())
                            .setDescription("<@"+other.getId()+"> est maintenant à "+other+" défaites.")
                            .build()).queue();
                    break;
            }

        }else {
            if (!event.getChannel().getId().equals(Main.getBROKOCHANNEL())) return;
            switch (event.getName()) {

                case "broccoli":
                    event.replyEmbeds(Broccoli.doBroccoli(author)).queue();
                    break;

                case "classement":
                    event.replyEmbeds(LeaderBoard.getLeaderBoard()).queue();
                    break;

                case "don":
                    event.replyEmbeds(Don.doDon(author, other, amount)).queue();
                    break;

                case "profil":
                    event.replyEmbeds(Profil.getProfil(event.getOption("member").getAsMember())).queue();
                    break;

                case "boutique":
                    event.reply("Le menu de la boutique se supprimera automatique au bout de 3 minutes.").setEphemeral(true).queue();
                    event.getChannel().sendMessageEmbeds(Shop.printShop(event.getMember()))
                            .setActionRow(Button.primary("role"," Role"), Button.primary("other", "Divers"))
                            .queue(msg -> {
                                Shop.getShopList().add(new Shop(Member.getMember(event.getUser().getId()), msg.getId()));
                            });
                    break;

                case "justeprix":
                    event.replyEmbeds(JustePrix.start(event.getMember(), amount)).queue();
                    break;
            }
        }
    }
}
