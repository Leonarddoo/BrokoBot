package fr.brokobot.main;

import fr.brokobot.main.commands.*;
import fr.brokobot.main.games.ChiFouMi;
import fr.brokobot.main.games.JustePrix;
import fr.brokobot.main.setup.CommandsBuilder;
import fr.brokobot.main.setup.CommandsHandler;
import fr.brokobot.main.setup.Member;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static JDA jda;

    private static final Color BLUE = new Color(0, 0, 255);
    private static final Color GREEN = new Color(20, 184, 42);
    private static final Color RED = new Color(255, 0, 0);

    private static final String GUILDID = "387304030390583296";
    private static final String BROKOCHANNEL = "962648366234365982";
    private static final String BROKOCATEGORIE = "962697330782502912";

    private static List<String> dailyList = new ArrayList<>();

    public static void main(String[] args) throws LoginException {
        jda = JDABuilder.createDefault("")
                .addEventListeners(new CommandsBuilder(),
                        new CommandsHandler(),
                        new Info(),
                        new JustePrix(),
                        new ChiFouMi())
                .build();
        Member.getSerialize();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(Calendar.HOUR == 0 && Calendar.MINUTE == 0){
                    dailyList = new ArrayList<>();
                }
            }
        }, 1000, 60000);
    }

    public static JDA getJda() {
        return jda;
    }

    public static Color getBLUE() {
        return BLUE;
    }

    public static Color getGREEN() {
        return GREEN;
    }

    public static Color getRED() {
        return RED;
    }

    public static Guild getLC(){
        return jda.getGuildById(Main.getGUILDID());
    }

    public static String getGUILDID() {
        return GUILDID;
    }

    public static String getBROKOCHANNEL() {
        return BROKOCHANNEL;
    }

    public static String getBROKOCATEGORIE() {
        return BROKOCATEGORIE;
    }

    public static List<String> getDailyList() {
        return dailyList;
    }
}
