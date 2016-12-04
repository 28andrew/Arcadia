package me.andrew28.arcadia;

import me.andrew28.arcadia.commands.Help;
import me.andrew28.arcadia.types.BotCommand;
import me.andrew28.arcadia.types.RegexMatchCommand;
import me.andrew28.arcadia.types.annotations.Command;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public class MessageListener extends ListenerAdapter{
    public MessageListener(JDA jda){
        jda.addEventListener(this);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if (event.getAuthor().getId().equals(Arcadia.getInstance().getJdaInstance().getSelfUser().getId())){
            return;
        }
        if (event.getChannelType() == ChannelType.PRIVATE){
            new Help().handle(event.getAuthor(), event.getMessage(), event.getChannelType(), event);
        }
        String message = event.getMessage().getStrippedContent();
        for (BotCommand botCommand : Arcadia.getInstance().getCommands()){
            switch(botCommand.getCommandType()){

                case EXACT:
                    if (botCommand.isCaseSensitive()){
                        //Case Sensitive check
                        if (botCommand.getCommandTrigger().equals(message)){
                            handle(botCommand, event);
                        }
                    }else{
                        //Ignore case
                        if (botCommand.getCommandTrigger().equalsIgnoreCase(message)){
                            handle(botCommand, event);
                        }
                    }
                    break;
                case STARTS_WITH:
                    if (botCommand.isCaseSensitive()){
                        //Case Sensitive Check
                        if (message.startsWith(botCommand.getCommandTrigger())){
                            handle(botCommand, event);
                        }
                    }else{
                        if (message.toLowerCase().startsWith(botCommand.getCommandTrigger().toLowerCase())){
                            handle(botCommand, event);
                        }
                    }
                    break;
                case REGEX:
                    String regex = botCommand.getCommandTrigger();
                    if (!botCommand.isCaseSensitive()){
                        regex = "(?i)" + regex;
                    }
                    if (botCommand.usesPrefix()){
                        regex = regex.replace(Arcadia.PREFIX, Arcadia.PREFIX.replace("\\","\\\\"));
                    }
                    if(message.matches(regex)){
                        handle(botCommand, event);
                    }
                    break;
            }
        }
    }
    public void handle(BotCommand botCommand, MessageReceivedEvent event){
        Arcadia.log("User " + event.getAuthor().getName() + " has activated " + botCommand.getCommandClass().getClass().getSimpleName() + " on channel " + event.getChannel().getName() + " on server " + event.getMessage().getGuild().getName());
        botCommand.getCommandClass().handle(event.getAuthor(), event.getMessage(), event.getChannelType(), event);
    }
}
