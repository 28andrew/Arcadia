package me.andrew28.arcadia;

import me.andrew28.arcadia.types.ConsoleColor;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public class ConsoleProcessor {
    public void process(String input){
        String[] split = input.split(" ");
        if (split.length > 0){
            switch(split[0]){
                case "help":
                    Arcadia.log(ConsoleColor.YELLOW + String.join("\n", new String[]{
                            "Commands:",
                            "(token) || View token",
                            "(exit|shutdown|quit|stop) || Shutdown the bot"
                    }));
                    break;
                case "token":
                    Arcadia.log("Token: " + Arcadia.getInstance().getCensoredToken());
                    break;
                case "exit":
                case "shutdown":
                case "quit":
                case "stop":
                    Arcadia.log( ConsoleColor.PURPLE + "Shutting down..");
                    Arcadia.exit();
                    break;
            }
        }
    }
}
