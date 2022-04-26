package fr.brokobot.main;

import fr.brokobot.main.builder.CommandsBuilder;
import fr.brokobot.main.commands.Broccoli;
import fr.brokobot.main.handler.CommandsHandler;
import fr.brokobot.main.handler.EventHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static final String TOKEN = "";
    private static final String PRESENTATIONCHANNEL = "962704792189304852";
    private static final String BROKOCHANNEL = "962648366234365982";
    private static final String BROKOCATEGORIE = "962697330782502912";
    private static final String BROKOPOINTS = "<:brokopoints:962685048748908585> **BrokoPoints**";
    private static JDA jda;

    public static void main(String[] args) throws LoginException {
        //On créer le bot
        jda = JDABuilder.createDefault(TOKEN)
                .addEventListeners(new CommandsBuilder(),
                        new CommandsHandler(),
                        new EventHandler())
                .build();

        //On créer un chrono qui s'execute chaque minute
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    //On sauvegarde les données
                    Player.save();
                    //On regarde si on change de jour, pour réinitialiser la dailyList
                    if(Calendar.HOUR == 0 && Calendar.MINUTE == 0){
                        Broccoli.getDailyList().clear();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 6000);
    }

    public static String getPRESENTATIONCHANNEL() {
        return PRESENTATIONCHANNEL;
    }

    public static String getBROKOCHANNEL() {
        return BROKOCHANNEL;
    }

    public static String getBROKOCATEGORIE() {
        return BROKOCATEGORIE;
    }

    public static String getBROKOPOINTS() {
        return BROKOPOINTS;
    }
}
