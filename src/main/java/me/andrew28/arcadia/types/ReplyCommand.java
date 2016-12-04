package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;

import java.util.function.Consumer;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public abstract class ReplyCommand implements ICommand {
    public void reply(Message message, String reply){
        message.getChannel().sendMessage(reply).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR);
    }
    public void reply(Message message, MessageEmbed reply){
        message.getChannel().sendMessage(reply).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR);
    }
}
