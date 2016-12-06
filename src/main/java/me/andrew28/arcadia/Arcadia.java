package me.andrew28.arcadia;

import me.andrew28.arcadia.games.GameManager;
import me.andrew28.arcadia.types.*;
import me.andrew28.arcadia.types.annotations.Command;
import me.andrew28.arcadia.types.commands.BotCommand;
import me.andrew28.arcadia.types.commands.CommandType;
import me.andrew28.arcadia.types.commands.ICommand;
import me.andrew28.arcadia.types.games.TurnBasedGame;
import me.andrew28.arcadia.util.ClassFinder;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import org.apache.commons.io.IOUtils;
import org.yaml.snakeyaml.Yaml;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.*;

/**
 * Created by Andrew Tran on 12/3/2016.
 */
public class Arcadia {

    public static final String USAGE_MSG = "java -jar Arcadia.jar <TOKEN> <FLAGS>\nFlags:";
    public static String PREFIX = "\\_\\_";
    public static File configFile = new File("./config.yml").getAbsoluteFile();
    public static Yaml yaml = new Yaml();
    public static Timer timer = new Timer();
    public static StatManager statManager;
    private static Arcadia instance;

    private String token, censoredToken;
    private JDA jdaInstance;
    private ArrayList<BotCommand> commands = new ArrayList<>();
    private Console console;
    private ArrayList<String> arguments;
    private HashMap<String, Object> config;

    public static Arcadia getInstance(){
        return instance;
    }

    public static void main(String[] args){
        instance = new Arcadia();
        getInstance().setArguments(new ArrayList<>(Arrays.asList(args)));
        //CONFIG
        if (!configFile.exists()){
            InputStream inputStream = Arcadia.class.getResourceAsStream("/config.yml");
            try {
                configFile.createNewFile();
                OutputStream outputStream = new FileOutputStream(configFile);
                IOUtils.copy(inputStream, outputStream);
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                log(ConsoleColor.RED + "Something went wrong when creating and copying config.yml");
                e.printStackTrace();
            } finally {

            }
        }
        try {
            getInstance().setConfig((HashMap<String, Object>) yaml.load(new FileReader(configFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (args.length == 0){
            log(USAGE_MSG);
            exitError();
        }
        if (args[0].length() != 59){
            log(ConsoleColor.RED + "Token is not of valid length (59)");
            exitError();
        }
        getInstance().setToken(args[0]);
        getInstance().setCensoredToken(getInstance().censorToken(getInstance().getToken()));
        log(ConsoleColor.GREEN + "Arcadia Bot");
        log(ConsoleColor.GREEN + "Token: " + getInstance().getCensoredToken());
        log(ConsoleColor.GREEN + "Loading JDA..");
        try {
            getInstance().setJdaInstance(
                    new JDABuilder(AccountType.BOT)
                            .setToken(getInstance().getToken())
                            .buildBlocking());
        } catch (LoginException e) {
            log(ConsoleColor.RED + "Could not login using the token: " + getInstance().getCensoredToken());
            e.printStackTrace();
        } catch (InterruptedException e) {
            log(ConsoleColor.RED + "No idea, what happened here (╯°□°）╯︵ ┻━┻");
            e.printStackTrace();
        } catch (RateLimitedException e) {
            log(ConsoleColor.RED + "A rate limit error, please try again later (or not).");
            e.printStackTrace();
        }


        GameManager.init();


        if ((Boolean) getInstance().getConfig().get("use-discordpw-stats")){
            statManager = new StatManager(getInstance().getJdaInstance());
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    statManager.update(() -> getInstance().getJdaInstance().getGuilds().size());
                    log(ConsoleColor.GREEN + "Posted stats to bots.discord.pw");
                }
            }, 0, 1000L * Long.valueOf((Integer) getInstance().getConfig().get("discordpw-update-frequency")));
        }

        log(ConsoleColor.GREEN + "Registering commands..");
        new MessageListener(getInstance().getJdaInstance());
        getInstance().searchCommands("me.andrew28.arcadia");
        log(ConsoleColor.GREEN + "Finished registering commands");

        getInstance().getJdaInstance().setAutoReconnect(true);
        getInstance().getJdaInstance().getPresence().setGame(new SimpleGame("Use __help"));

        //Start console when everything else is done
        getInstance().setConsole(System.console());
        new Thread(() -> {
            ConsoleProcessor consoleProcessor = new ConsoleProcessor();
            while(true){
                consoleProcessor.process(getInstance().getConsole().readLine("CMD: "));
            }
        }).start();
    }

    public Role getRole(Guild guild){
        for(Role role : guild.getRoles()){
            for (Member member : guild.getMembersWithRoles(role)){
                if (member.getUser().getId().equals(jdaInstance.getSelfUser().getId())){
                    return role;
                }
            }
        }
        return null;
    }

    public String getBotUsername(){
        return getJdaInstance().getSelfUser().getName();
    }

    public static void exit(){
        System.exit(0);
    }

    public static void exitError(){
        System.exit(1);
    }

    public void searchCommands(String packageName){
        Class klass = Arcadia.class;

        CodeSource codeSource = klass.getProtectionDomain().getCodeSource();
        File file = null;
        try {
            file = new File(URLDecoder.decode(codeSource.getLocation().getPath(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log(ConsoleColor.RED + "Could not detect jar file ran to search for commands in");
            e.printStackTrace();
        }
        for (Class clazz : ClassFinder.getClasses(file, packageName)){
            if (ICommand.class.isAssignableFrom(clazz) && !clazz.getCanonicalName().contains("me.andrew28.arcadia.types")){
                if (clazz.isAnnotationPresent(Command.class)){
                    try {
                        registerCommand((ICommand) clazz.newInstance());
                    } catch (Exception e){
                        log(ConsoleColor.RED + "Failed to register command " + clazz.getCanonicalName());
                        e.printStackTrace();
                    }
                }else{
                    log(ConsoleColor.RED + clazz.getCanonicalName() + " does not have the annotation me.andrew28.arcadia.types.annotations.Command");
                }
            }
        }
    }

    public void registerCommand(ICommand command){
        commands.add(new BotCommand(CommandType.fromObject(command), command.getClass().getDeclaredAnnotation(Command.class), command));
        log(ConsoleColor.BLUE + "Registered Command: " + ConsoleColor.YELLOW + command.getClass().getCanonicalName());
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

    public ArrayList<BotCommand> getCommands() {
        return commands;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    public static void log(String s){
        if (getInstance().getArguments().contains("-nocolor")){
                for (ConsoleColor consoleColor : ConsoleColor.values()){
                    s = s.replace(consoleColor.getTerminalRepresentation(), "");
            }
        }
        System.out.println(s);
        System.out.print(ConsoleColor.RESET);
    }

    public HashMap<String, Object> getConfig() {
        return config;
    }

    public void setConfig(HashMap<String, Object> config) {
        this.config = config;
    }
}
