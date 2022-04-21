package fr.brokobot.main.games;

import fr.brokobot.main.Main;
import fr.brokobot.main.setup.ErrorEmbed;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JustePrix extends ListenerAdapter {

    private Member member;
    private String channel, message;
    private int number, bet, attemps;
    private boolean start;

    private static final List<JustePrix> jpList = new ArrayList<>();

    public JustePrix(){}

    public JustePrix(Member m, String c, String msg, int b){
        this.member = m;
        this.channel = c;
        this.message = msg;
        this.number = new Random().nextInt(999)+1;
        this.bet = b;
        this.attemps = 0;
        this.start = false;
    }

    public static MessageEmbed start(net.dv8tion.jda.api.entities.Member member, int bet){

        if(memberIsPlaying(member.getId())) return ErrorEmbed.getAlreadyIngame();

        Member m = Member.getMember(member.getId());
        MessageEmbed errorMsg = ErrorEmbed.errorGames(m, null, bet);
        if(errorMsg != null) return errorMsg;

        Guild guild = Main.getLC();
        guild.createTextChannel("justeprix-"+member.getEffectiveName(), guild.getCategoryById(Main.getBROKOCATEGORIE())).syncPermissionOverrides()
                .addMemberPermissionOverride(member.getIdLong(), Permission.MESSAGE_SEND.getRawValue(), Permission.MESSAGE_MANAGE.getRawValue()).queue(channel -> {
            channel.getManager().setTopic(Integer.toString(bet));
            channel.sendMessageEmbeds(new EmbedBuilder()
                            .setTitle("**Partie de : **"+member.getEffectiveName())
                    .setDescription("**Règles :**\n" +
                            "Dès que la partie sera lancé, le bot choisira un nombre entre **1** et **1000**.\n" +
                            "Vous aurez **9 essaies** pour trouver ce nombre et avoir la chance de doubler votre mise.\n" +
                            "Si ne parvenez pas à trouvez le nombre, vous aurez perdu.\n" +
                            "\n" +
                            "**Jouer :**\n" +
                            "Il vous suffit simplement d'écrire message ne contenant uniquement un nombre!\n" +
                            "Si le message n'est pas un nombre entre 1 et 1000, cette tentative ne sera pas prise en compte.\n" +
                            "Ce message sera modifié durant la partie et le bot vous répondra le nombre est plus petit ou plus grand que votre proposition.\n" +
                            "\n" +
                            "**Bonne Chance!**")
                            .setImage("https://i.imgur.com/FUtllQy.jpg")
                    .build()).setActionRow(Button.success("start", "Lancer la partie!"), Button.danger("cancel", "Annuler")).queue(msg -> {
                     jpList.add(new JustePrix(m, channel.getId(), msg.getId(), bet));
            });
            channel.sendMessage(member.getAsMention()).queue(ping -> {
                ping.delete().queueAfter(1, TimeUnit.SECONDS);
            });
        });
        return new EmbedBuilder()
                .setColor(Main.getGREEN())
                .setDescription("Un channel va se créer pour votre partie.")
                .build();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        JustePrix game = retrieveJPbyChannel(event.getChannel().getId());
        if(game == null) return;
        if(!event.getUser().getId().equals(game.member.getId())) return;

        if(event.getButton().getId().equals("cancel")){
            jpList.remove(game);
            event.getChannel().delete().queue();
        }else if(event.getButton().getId().equals("start")){

            game.member.removePoints(game.bet);
            game.start = true;

            event.getTextChannel().retrieveMessageById(game.message).queue(msg -> {
                msg.editMessageEmbeds(new EmbedBuilder()
                        .setTitle("La partie a commencé !")
                        .setColor(Main.getBLUE())
                        .setDescription("Joueur : <@"+game.member.getId()+">\n" +
                                "Mise : **"+game.bet+"**\n" +
                                "Tentatives restantes :** "+ (9 - game.attemps) +"**\n" +
                                "Dernier nombre : *vide*")
                        .build()).queue();
                msg.editMessageComponents().queue();
            });

            event.reply("Let's go, à toi de jouer !").setEphemeral(true).queue();
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        if(!event.getChannelType().equals(ChannelType.TEXT)) return;
        JustePrix game = retrieveJPbyChannel(event.getChannel().getId());
        if(game == null) return;
        if(!game.start) return;
        if(!event.getAuthor().getId().equals(game.member.getId())) return;
        if(event.getMessage().getContentRaw().split(" ").length > 1) return;

        try{
            String image;
            int num = Integer.parseInt(event.getMessage().getContentRaw());
            if(num > 0 && num < 1001){
               game.attemps++;
            }
            if(num > game.number){
                image = "https://i.imgur.com/PdjoyB6.jpg";
            }else if(num < game.number){
                image = "https://i.imgur.com/Dpb8Iwr.jpg";
            }else {
                game.member.addPoints(game.bet*2);
                game.member.addWin();
                event.getChannel().retrieveMessageById(game.message).queue(msg ->{
                    msg.editMessageEmbeds(new EmbedBuilder()
                                    .setTitle("Victoire")
                                    .setColor(Main.getGREEN())
                                    .setDescription("Bien joué ! Le nombre était bien :**"+game.number+"**\n" +
                                            "Vous avez remporté <:brokopoints:962685048748908585> **"+(game.bet*2)+"BrokoPoints**\n" +
                                            "Le channel sera automatiquement fermer dans 5 secondes.")
                            .build()).queue();
                });
                event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                jpList.remove(game);
                return;
            }
            if(game.attemps == 9){
                game.member.addLoose();
                event.getChannel().retrieveMessageById(game.message).queue(msg ->{
                    msg.editMessageEmbeds(new EmbedBuilder()
                            .setTitle("Défaite")
                            .setColor(Main.getRED())
                            .setDescription("Dommage ! Le nombre était : **"+game.number+"**\n" +
                                    "Le channel sera automatiquement fermer dans 5 secondes.")
                            .build()).queue();
                });
                event.getChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                jpList.remove(game);
                return;
            }

            event.getChannel().retrieveMessageById(game.message).queue(msg ->{
                msg.editMessageEmbeds(new EmbedBuilder()
                        .setTitle("Essaye encore !")
                        .setColor(Main.getBLUE())
                        .setDescription("Joueur : <@"+game.member.getId()+">\n" +
                                "Mise : **"+game.bet+"**\n" +
                                "Tentatives restantes :**"+ (9 - game.attemps) +"**\n" +
                                "Dernier nombre : **"+num+"**\n")
                        .setImage(image)
                        .build()).queue();
            });

        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        event.getMessage().delete().queue();
    }

    public static boolean memberIsPlaying(String id){
        for(JustePrix jp : jpList){
            if(jp.member.getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public static JustePrix retrieveJPbyChannel(String channel){
        for(JustePrix jp : jpList){
            if(jp.channel.equals(channel)){
                return jp;
            }
        }
        return null;
    }
}
