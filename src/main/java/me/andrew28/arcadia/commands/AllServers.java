package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.ExactMatchCommand;
import me.andrew28.arcadia.types.annotations.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

/**
 * Created by Andrew Tran on 12/4/2016
 */
@Command(command = "servers", usage = "servers", description = "Get all servers I am on.")
public class AllServers extends ExactMatchCommand{
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        ArrayList<String> rmessage = new ArrayList<>();
        rmessage.add("Servers:");
        for (Guild guild : Arcadia.getInstance().getJdaInstance().getGuilds()){
            rmessage.add("-" + guild.getName());
        }
        reply(message, String.join("\n", rmessage));
    }
}
