package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.types.commands.RegexMatchCommand;
import me.andrew28.arcadia.types.annotations.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Andrew Tran on 12/3/2016
 */
//Hi(,)? (@)?<BOT NAME>( !|!)?
@Command(command = "(Hi|Hello|Sup)(,)? (@)?<BOT NAME>( !|!)?", usage = "(Hi|Hello|Sup)[,] @<BOT NAME>", description = "Say hello to the bot to get a reply", usePrefix = false)
public class Hello extends RegexMatchCommand{
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        reply(message, "Hello " + user.getAsMention() + "!");
        //System.out.println("Hello " + user.getAsMention() + "!");
    }

}
