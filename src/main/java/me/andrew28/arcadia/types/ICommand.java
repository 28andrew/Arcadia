package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public interface ICommand {
    void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event);
}
