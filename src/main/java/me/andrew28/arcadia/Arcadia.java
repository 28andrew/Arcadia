package me.andrew28.arcadia;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.Console;

/**
 * Created by Andrew Tran on 12/3/2016.
 */
public class Arcadia {

    public static final String USAGE_MSG = "java -jar Arcadia.jar <TOKEN> <FLAGS>\nFlags:";
    public static String PREFIX = "__";
    private static Arcadia instance;

    private String token, censoredToken;

    private JDA jdaInstance;
    private Console console;
    public static Arcadia getInstance(){
        return instance;
    }

    public static void main(String[] args){
        instance = new Arcadia();

        if (args.length == 0){
            System.out.println(USAGE_MSG);
            exitError();
        }
        if (args[0].length() != 59){
            System.out.println("Token is not of valid length (59)");
            exitError();
        }
        getInstance().setToken(args[0]);
        getInstance().setCensoredToken(getInstance().censorToken(getInstance().getToken()));
        System.out.println("Arcadia Bot");
        System.out.println("Token: " + getInstance().getCensoredToken());

        try {
            getInstance().setJdaInstance(
                    new JDABuilder(AccountType.BOT)
                            .setToken(getInstance().getToken())
                            .buildBlocking());
        } catch (LoginException e) {
            System.out.println("Could not login using the token: " + getInstance().getCensoredToken());
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("No idea, what happened here (╯°□°）╯︵ ┻━┻");
            e.printStackTrace();
        } catch (RateLimitedException e) {
            System.out.println("A rate limit error, please try again later (or not).");
            e.printStackTrace();
        }

        //Start console when everything else is done
        getInstance().setConsole(System.console());
        new Thread(() -> {
            ConsoleProcessor consoleProcessor = new ConsoleProcessor();
            while(true){
                consoleProcessor.process(getInstance().getConsole().readLine("CMD: "));
            }
        }).start();
    }

    public static void exit(){
        System.exit(0);
    }

    public static void exitError(){
        System.exit(1);
    }

    public String censorToken(String token){
        String tokenCensored = "";
        for (Integer i = 15; i < token.length() - 20; i++){
            tokenCensored += "*";
        }
        tokenCensored = token.substring(0, 15) + tokenCensored + token.substring(token.length() - 20, token.length());
        return tokenCensored;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCensoredToken() {
        return censoredToken;
    }

    public void setCensoredToken(String censoredToken) {
        this.censoredToken = censoredToken;
    }

    public JDA getJdaInstance() {
        return jdaInstance;
    }

    public void setJdaInstance(JDA jdaInstance) {
        this.jdaInstance = jdaInstance;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }
}
