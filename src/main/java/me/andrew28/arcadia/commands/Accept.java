package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.RequestManager;
import me.andrew28.arcadia.types.annotations.Command;
import me.andrew28.arcadia.types.commands.RegexMatchCommand;
import me.andrew28.arcadia.util.EmbedBuilderUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;

/**
 * Created by Andrew Tran on 12/5/2016
 */
@Command(command = "accept (@.*)", usage = "accept <@Host>", description = "Accept a game request from a user (host of game)")
public class Accept extends RegexMatchCommand{
    Matcher currentMatcher;
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        User host = message.getMentionedUsers().get(0);
        String hostID = host.getId();
        if (!RequestManager.memberHasRequest(user.getId(), hostID)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilderUtil.error(embedBuilder);
            embedBuilder.setDescription("You do not have a request from " + host.getAsMention());
            return;
        }

        RequestManager.Request request = RequestManager.getRequestForMember(user.getId(), host.getId());

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Accepted", "http://i.imgur.com/RYOm7is.png", "http://i.imgur.com/RYOm7is.png");
        embedBuilder.setDescription(user.getAsMention() + " has accepted the game request from " + host.getAsMention());
        embedBuilder.addField("Game", request.getGameName(), false);
        reply(message, embedBuilder.build());

        request.accept(user.getId());
    }

    @Override
    public void saveMatchedGroups(Matcher matcher) {
        currentMatcher = matcher;
    }
}
