package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.ArcadiaConsumers;
import me.andrew28.arcadia.types.BotCommand;
import me.andrew28.arcadia.types.ExactMatchCommand;
import me.andrew28.arcadia.types.annotations.Command;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * Created by Andrew Tran on 12/4/2016
 */
@Command(command = "help", usage = "help", description = "Get help for this bot.")
public class Help extends ExactMatchCommand {
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        ArrayList<String> help = new ArrayList<>();
        help.add(Arcadia.getInstance().getBotUsername() + "'s Commands:");
        for (BotCommand command : Arcadia.getInstance().getCommands()){
            String line = "**>>** " + (command.usesPrefix() ? Arcadia.PREFIX : "") + command.getUsage() + "\n" + "**>>>>** " + command.getDescription();
            help.add(line);
            help.add("");
        }
        reply(message, "Sending you my help, " + user.getAsMention() + " !");
        user.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(String.join("\n", help)).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR), ArcadiaConsumers.ERROR);


    }
}
