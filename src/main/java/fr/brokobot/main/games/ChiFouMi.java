package fr.brokobot.main.games;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.ErrorEmbed;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class ChiFouMi extends ListenerAdapter {

    private Member j1, j2;
    private String channel, message, choice1, choice2;
    private int bet, round1, round2;

    private static List<ChiFouMi> cfmList = new ArrayList<>();

    public ChiFouMi(){}

    public ChiFouMi(Member j1, Member j2, int bet){
        this.j1 = j1;
        this.j2 = j2;
        this.bet = bet;
        this.channel = null;
        this.message = null;
        this.choice1 = null;
        this.choice2 = null;
        this.round1 = 0;
        this.round2 = 0;
        cfmList.add(this);
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if(!event.getName().equals("chifoumi")) return;
        if(!event.getChannel().getId().equals(Main.getBROKOCHANNEL())) return;

        Member sender = Member.getMember(event.getUser().getId());
        Member receiver = Member.getMember(event.getOption("member").getAsUser().getId());
        int bet = event.getOption("bet").getAsInt();

        MessageEmbed errorMsg = ErrorEmbed.errorGames(sender, receiver, bet);
        if(errorMsg != null){
            event.replyEmbeds(errorMsg).queue();
            return;
        }

        if(retrieveCFMById(event.getUser().getId()) != null){
            event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getRED())
                            .setDescription("Vous avez déjà une partie en cours.")
                    .build()).queue();
            return;
        }else if(retrieveCFMById(event.getOption("member").getAsUser().getId()) != null){
            event.replyEmbeds(new EmbedBuilder()
                            .setColor(Main.getRED())
                            .setDescription("Cette personne à déjà une partie en cours.")
                    .build()).queue();
            return;
        }

        new ChiFouMi(sender, receiver, bet);

        event.getChannel().sendMessageEmbeds(new EmbedBuilder()
                        .setAuthor(event.getMember().getEffectiveName(), event.getMember().getEffectiveAvatarUrl(), event.getMember().getEffectiveAvatarUrl())
                        .setTitle("Demande de duel ChiFouMi")
                        .setColor(Main.getBLUE())
                        .setDescription("Cette demande de duel est destiné à : <@"+receiver.getId()+">\n" +
                                "La mise est de : **"+bet+"** \n" +
                                "Qui sera le grand gagnant ?")
                .build())
                .setActionRow(Button.success("accept", "Accepter"), Button.danger("deny", "Refuser")).queue(msg -> {
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            ChiFouMi cfm = retrieveCFMById(event.getUser().getId());
                            if(cfm != null && cfm.channel == null){
                                cfmList.remove(cfm);
                                msg.delete().queue();
                            }
                        }
                    }, 30000);
                });
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        ChiFouMi cfm = retrieveCFMById(event.getUser().getId());
        if(cfm == null) return;
        String buttonId = event.getButton().getId();
        if(!( buttonId.equals("deny") || buttonId.equals("accept") || buttonId.equals("rock") || buttonId.equals("paper") || buttonId.equals("cut") ) ) return;

        if(event.getButton().getId().equals("deny") || event.getButton().getId().equals("accept")){
            if(!cfm.j2.getId().equals(event.getUser().getId())) return;

            if(event.getButton().getId().equals("deny")){
                event.replyEmbeds(new EmbedBuilder()
                                .setColor(Main.getRED())
                                .setDescription(event.getMember().getAsMention()+" vient de refuser le duel.")
                        .build()).queue();
                cfmList.remove(cfm);
                event.getMessage().delete().queue();


            }else if(event.getButton().getId().equals("accept")) {
                event.getGuild().createTextChannel("ChiFouMi-" + event.getMember().getEffectiveName(), event.getGuild().getCategoryById(Main.getBROKOCATEGORIE()))
                        .addMemberPermissionOverride(Long.parseLong(cfm.j1.getId()), Permission.MESSAGE_SEND.getRawValue(), Permission.MESSAGE_MANAGE.getRawValue())
                        .addMemberPermissionOverride(event.getUser().getIdLong(), Permission.MESSAGE_SEND.getRawValue(), Permission.MESSAGE_MANAGE.getRawValue()).queue(channel -> {
                            channel.sendMessageEmbeds(new EmbedBuilder()
                                    .setColor(Main.getBLUE())
                                    .setDescription("Choisissez l'une des 3 propositions si dessous : *Pierre / Papier / Ciseau*\n" +
                                            "Une fois que les 2 joueurs auront fait leur choix, le vainqueur de la manche sera annoncé.\n" +
                                            "Le premier des 2 joueurs qui gagne 3 manches, remporta le duel.")
                                    .addField("Joueur 1 :" , "**Pseudo : **<@" + cfm.j1.getId() + ">\n" +
                                            "**Manches Gagnées :** " + cfm.round1, true)
                                    .addBlankField(true)
                                    .addField("Joueur 2 :", "**Pseudo : **<@" + cfm.j2.getId() + ">\n" +
                                            "**Manches Gagnées :** " + cfm.round2, true)
                                    .build()).setActionRow(Button.secondary("rock", "Pierre"), Button.secondary("paper", "Papier"), Button.secondary("cut", "Ciseau")).queue(message -> {
                                cfm.channel = channel.getId();
                                cfm.message = message.getId();
                                cfm.j1.removePoints(cfm.bet);
                                cfm.j2.removePoints(cfm.bet);
                            });
                        });
                event.getMessage().delete().queue();
            }

        }else if(event.getButton().getId().equals("rock") || event.getButton().getId().equals("paper") || event.getButton().getId().equals("cut")){
            if(!(event.getUser().getId().equals(cfm.j1.getId()) || event.getUser().getId().equals(cfm.j2.getId()) ))  return;
            if( (cfm.j1.getId().equals(event.getUser().getId()) && cfm.choice1 != null) || (cfm.j2.equals(event.getUser().getId()) && cfm.choice2 != null) ) return;
            cfm.setChoice(event.getUser().getId(), event.getButton().getLabel());
            event.reply("Votre choix a été pris en compte. En attente de votre adversaire.").setEphemeral(true).queue();
            if(cfm.choice1 != null && cfm.choice2 != null){

                String image = cfm.checkWinnerRound();
                event.getChannel().retrieveMessageById(cfm.message).queue(msg -> {
                    msg.editMessageEmbeds(new EmbedBuilder()
                            .setColor(Main.getBLUE())
                            .addField("Joueur 1 :", "**Pseudo : **<@" + cfm.j1.getId() + ">\n" +
                                    "**Manches Gagnées : **" + cfm.round1+"\n" +
                                    "**Choix : **"+cfm.choice1, true)
                            .addBlankField(true)
                            .addField("Joueur 2 :", "**Pseudo : **<@" + cfm.j2.getId() + ">\n" +
                                    "**Manches Gagnées : **" + cfm.round2+"\n" +
                                    "**Choix : **"+cfm.choice2, true)
                            .setImage(image)
                            .build()).queue();
                    cfm.choice1 = null;
                    cfm.choice2 = null;
                });

                if(cfm.round1 == 3 || cfm.round2 == 3){
                    String ping = cfm.checkWinnerGame();
                    cfmList.remove(cfm);
                    event.getTextChannel().sendMessageEmbeds(new EmbedBuilder()
                                    .setColor(Main.getGREEN())
                                    .setDescription("<@"+ping+"> Vient de remporter ce duel !\n" +
                                            "Ce channel sera automatique supprimé dans 10 secondes.")
                            .build()).queue();
                    event.getChannel().delete().queueAfter(10, TimeUnit.SECONDS);
                }
            }
        }

    }

    public static ChiFouMi retrieveCFMById(String id){
        for(ChiFouMi cfm : cfmList){
            if(cfm.j1.getId().equals(id) || cfm.j2.getId().equals(id)){
                return cfm;
            }
        }
        return null;
    }

    public void setChoice(String id, String choice){
        if(this.j1.getId().equals(id)){
            this.choice1 = choice;
        }else if(this.j2.getId().equals(id)){
            this.choice2 = choice;
        }
    }

    public String checkWinnerRound(){
        String image = null;
        if(this.choice1.equals(this.choice2)){
            image = "https://i.imgur.com/lb9eurM.jpg";
        }else if( (this.choice1.equals("Pierre") && this.choice2.equals("Ciseau")) || (this.choice1.equals("Papier") && this.choice2.equals("Pierre")) || (this.choice1.equals("Ciseau") && this.choice2.equals("Papier")) ) {
            this.round1++;
            image = "https://i.imgur.com/utR8LBF.jpg";
        }else {
            this.round2++;
            image = "https://i.imgur.com/0fRrXFF.jpg";
        }
        return image;
    }

    public String checkWinnerGame(){
        if(this.round1 == 3){
            this.j1.addPoints(this.bet*2);
            this.j1.addWin();
            this.j2.addLoose();
            return this.j1.getId();
        }else{
            this.j2.addPoints(this.bet*2);
            this.j2.addWin();
            this.j1.addLoose();
            return this.j2.getId();
        }
    }
}
