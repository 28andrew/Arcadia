package me.andrew28.arcadia;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public class ConsoleProcessor {
    public void process(String input){
        String[] split = input.split(" ");
        if (split.length > 0){
            switch(split[0]){
                case "help":
                    System.out.println(String.join("\n", new String[]{
                            "Commands:",
                            "(token) || View token",
                            "(exit|shutdown|quit|stop) || Shutdown the bot"
                    }));
                    break;
                case "token":
                    System.out.println("Token: " + Arcadia.getInstance().getCensoredToken());
                    break;
                case "exit":
                case "shutdown":
                case "quit":
                case "stop":
                    System.out.println("Shutting down..");
                    Arcadia.exit();
                    break;
            }
        }
    }
}
