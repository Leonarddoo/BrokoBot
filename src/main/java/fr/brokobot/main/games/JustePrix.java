package fr.brokobot.main.games;

import fr.brokobot.main.Main;
import fr.brokobot.main.Player;
import fr.brokobot.main.builder.MessageBuilder;
import fr.brokobot.main.handler.CheckIn;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class JustePrix {

    private Player player;
    private String channel, message;
    private static int number;
    private int bet;
    private int attemps;

    private static final List<JustePrix> JUSTE_PRIX_LIST = new ArrayList<>();

    private static final String LOWER = "https://i.imgur.com/PdjoyB6.jpg";
    private static final String UPPER = "https://i.imgur.com/Dpb8Iwr.jpg";

    private static final MessageEmbed startMessage = new EmbedBuilder()
            .setColor(Color.BLUE)
            .setDescription("__Règles du jeu :__\n" +
                    "Le BrokoBot va choisir un nombre entre 1 et 999. Vous aurez 9 tentatives pour trouver ce nombre.\n" +
                    "Si vous le trouvez, vous remportez le double de votre mise. Dans le cas contraire vous perdez celle-ci.\n" +
                    "\n" +
                    "__Fonctionnement :__\n" +
                    "A chaque fois que vous enverrez un nombre dans ce channel, ce message sera modifié et le BrokoBot vous donnera des indices.\n" +
                    "Attention ! Tous vos prochains message dans ce channel seront comptés comme des tentatives.\n" +
                    "\n" +
                    "*Maintenant que vous connaissez le fontionnement, nous vous souhaitons bonne chance !*")
            .setImage("https://i.imgur.com/FUtllQy.jpg")
            .build();

    private static final MessageEmbed channelWillBeCreate = new EmbedBuilder()
            .setColor(Color.GREEN)
            .setDescription("Restez attentif ! Un channel va se créer avec votre game.")
            .build();

    public JustePrix(Player p, int b){
        this.player = p;
        this.channel = null;
        this.message = null;
        this.number = new Random().nextInt(998)+1;
        this.bet = b;
        this.attemps = 0;
    }

    public static MessageEmbed createGame(Guild guild, Member member, int bet){
        //On recupere le joueur
        Player player = Player.retrievePlayer(member.getId());
        //On créer une game de JustePrix
        JustePrix game = new JustePrix(player, bet);

        //On regarde si les conditions sont ok pour lancé la partie
        MessageEmbed ErrorMsg = game.canPlay();
        if(ErrorMsg != null) return ErrorMsg;

        //Si c'est le cas on créer un channel, et on envoie le premier message
        guild.createTextChannel("justeprix-"+member.getEffectiveName(), guild.getCategoryById(Main.getBROKOCATEGORIE()))
                .syncPermissionOverrides()
                .addMemberPermissionOverride(member.getIdLong(), Permission.MESSAGE_SEND.getRawValue(), Permission.MANAGE_CHANNEL.getRawValue()).queue(channel -> {
                    channel.sendMessageEmbeds(startMessage).queue(msg -> {
                        //On configure le channel et le message de la game
                        game.channel = channel.getId();
                        game.message = msg.getId();
                    });
                    channel.sendMessage(member.getAsMention()).queue(ping -> {
                        ping.delete().queue();
                    });
                });
        //On enleve les BrokoPoints de la mise au joueur
        game.player.setPoints(game.player.getPoints()-game.bet);
        //On rajoute la game en cours dans la liste
        JUSTE_PRIX_LIST.add(game);
        return channelWillBeCreate;
    }

    public static void updateGame(JustePrix game, TextChannel channel, Message message){
        //Si le message est compose, ce n'est pas un nombre
        if(message.getContentRaw().split(" ").length > 1) return;
        int choice;
        try{
            //On essaye de récuperer le nombre du message
            choice = Integer.parseInt(message.getContentRaw());
        }catch (NumberFormatException e){
            //Si on y arrive pas on ne fait rien
            return;
        }

        //On recupere le message du jeu
        channel.retrieveMessageById(game.message).queue(msg -> {
            if(choice == game.number){
                msg.editMessageEmbeds(winGame(game)).queue();
                channel.delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }else if(game.attemps < 9){
                msg.editMessageEmbeds(updateEmbed(game, choice)).queue();
            }else{
                msg.editMessageEmbeds(looseGame(game)).queue();
                channel.delete().queueAfter(5, TimeUnit.SECONDS);
                return;
            }
        });
        //On rajoute une tentative utilisée au joueur
        game.attemps++;
        //On supprime message
        message.delete().queue();
    }

    public static MessageEmbed updateEmbed(JustePrix game, int msg){
        //On met l'image correspondant à l'indice
        String image = null;
        if(game.number > msg){
            image = UPPER;
        }else if(game.number < msg) {
            image = LOWER;
        }

        return new EmbedBuilder()
                .setTitle("Partie en cours")
                .setColor(Color.BLUE)
                .setDescription("Joueur <@"+game.player.getId()+">\n" +
                        "Mise :**"+game.bet+"**\n" +
                        "Tentative Restantes :**"+(9-game.attemps)+"**\n" +
                        "Dernier Nombre : **"+msg+"**")
                .setImage(image)
                .build();
    }

    public static MessageEmbed winGame(JustePrix game){
        //On rajoute les points au joueur
        game.player.setPoints(game.player.getPoints()+game.bet);
        //On supprime la game de la liste
        JUSTE_PRIX_LIST.remove(game);

        return new EmbedBuilder()
                .setColor(Color.GREEN)
                .setDescription("Bien joué ! Le nombre était : "+game.number+"\n" +
                        "Vous remportez donc " + game.bet*2 + Main.getBROKOPOINTS() + "\n" +
                        "\n" +
                        "*Ce channel sera automatiquement fermé dans 5 secondes.*")
                .build();
    }

    public static MessageEmbed looseGame(JustePrix game){
        //On supprime la game de la liste
        JUSTE_PRIX_LIST.remove(game);

        return new EmbedBuilder()
                .setColor(Color.RED)
                .setDescription("Dommage ! Le nombre était : "+game.number+"\n" +
                        "\n" +
                        "*Ce channel sera automatiquement fermé dans 5 secondes.*")
                .build();
    }

    public MessageEmbed canPlay(){
        //On initialise un message d'erreur
        MessageEmbed ErrorMsg;
        //Onr regarde si il a assez de points pour jouer
        ErrorMsg = CheckIn.checkPoints(this.player, this.bet);
        if(ErrorMsg != null) return ErrorMsg;
        //On regarde si le joueur n'a pas déjà une game de lancé.
        if(JUSTE_PRIX_LIST.contains(this)) return MessageBuilder.getAlreadyInGame();

        return null;
    }

    public static JustePrix retrieveJPByChannelID(String id){
        for(JustePrix jp : JUSTE_PRIX_LIST){
            if(jp.channel.equals(id)){
                return jp;
            }
        }
        return null;
    }
}
