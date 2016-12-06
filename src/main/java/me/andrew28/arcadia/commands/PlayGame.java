package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.RequestManager;
import me.andrew28.arcadia.games.ActiveGameManager;
import me.andrew28.arcadia.games.GameManager;
import me.andrew28.arcadia.types.ArcadiaConsumers;
import me.andrew28.arcadia.types.annotations.Command;
import me.andrew28.arcadia.types.commands.RegexMatchCommand;
import me.andrew28.arcadia.types.games.Game;
import me.andrew28.arcadia.util.EmbedBuilderUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

/**
 * Created by Andrew Tran on 12/4/2016
 */
@Command(command = "play ([a-zA-Z0-9 ]+) with (@.*( )?)+", usage = "play <game> with <@Mention[ @Mention2][ @Mention 3]>", description = "WIP, ETA 12/8/16 CST")
public class PlayGame extends RegexMatchCommand{
    Matcher currentMatcher;
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        String game = currentMatcher.group(1);
        if (!GameManager.gameExists(game)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilderUtil.error(embedBuilder);
            embedBuilder.addField("Available Games: ", String.join(" ,", GameManager.getAllGameNames()),false);
            reply(message, embedBuilder.build());
            return;
        }
        if (message.getMentionedUsers().size() == 0){
            return;
        }

        String hostId = user.getId();

        ArrayList<String> members = new ArrayList<>();
        members.add(hostId); //Host needs to be in it too;

        List<User> opponents = message.getMentionedUsers();

        System.out.println(user.getId());
        if (!user.getId().equals("127170421987475456") && opponents.contains(user)){
            EmbedBuilder embedBuilder = new EmbedBuilder();
            EmbedBuilderUtil.error(embedBuilder);
            embedBuilder.setDescription("You may not add yourself as an opponent");
            reply(message, embedBuilder.build());
            return;
        }

        members.addAll(opponents.stream().map(user1 -> user1.getId()).collect(Collectors.toList()));

        if (RequestManager.memberIsAlreadyHosting(hostId)){
            RequestManager.Request request = RequestManager.getRequestMemberIsHosting(hostId);
            Collection<String> users = Arrays.stream(request.getParty()).map(userID -> Arcadia.getInstance().getJdaInstance().getUserById(userID).getAsMention()).collect(Collectors.toList());
            reply(message, "You already made a request for the members: " + String.join(" ", users));
            return;
        }
        RequestManager.registerRequest(new RequestManager.Request(user.getId(), game, members.toArray(new String[members.size()])), new RequestManager.RequestHandler() {
            @Override
            public void ready(RequestManager.Request request) {
                Game game = GameManager.newInstanceOfGame(GameManager.getGame(request.getGameName()), (TextChannel) message.getChannel(), members.toArray(new String[members.size()]));
                ActiveGameManager.addGame(game);
                game.start();
            }
        });
        Arcadia.log("Game " + game + " requested with players " + String.join(", ", members.stream().map(memberID -> Arcadia.getInstance().getJdaInstance().getUserById(memberID).getName()).collect(Collectors.toList())) + " on channel " + message.getChannel().getName() + " on server " + message.getGuild().getName());
        Game infoGame = GameManager.newInstanceOfGame(GameManager.getGame(game), null, null); //Just to get info
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("WORK IN PROGRESS " + game, infoGame.getIconURL(), infoGame.getIconURL());
        embedBuilder.setColor(infoGame.getAccentColor());
        embedBuilder.addField("Host", user.getAsMention(), false);
        embedBuilder.addField("Participants" + ((members.size() - 1) > 1 ? "s" : ""), String.join(", ", members.stream().map(memberID -> Arcadia.getInstance().getJdaInstance().getUserById(memberID).getAsMention()).collect(Collectors.toList())), false);
        reply(message, embedBuilder.build());

        //
        for (User opponent : opponents){
            opponent.openPrivateChannel().queue(privateChannel -> {
                EmbedBuilder inviteEmbedBuilder = new EmbedBuilder();
                inviteEmbedBuilder.setAuthor(game, infoGame.getIconURL(), infoGame.getIconURL());
                inviteEmbedBuilder.setColor(infoGame.getAccentColor());
                inviteEmbedBuilder.setDescription("Go to this channel to accept it!");
                inviteEmbedBuilder.addField("Host", user.getAsMention(), true);
                inviteEmbedBuilder.addField("Channel ", "<#" + message.getChannel().getId() +">", true);
                inviteEmbedBuilder.addField("Command", "__accept " + user.getAsMention(), false);
                privateChannel.sendMessage(inviteEmbedBuilder.build()).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR);
            }, ArcadiaConsumers.ERROR);
        }

        //reply(message, "EXTREMELY WORK IN PROGRESS:\nGame: " + game + "\nOpponent[s]: " + message.getMentionedUsers().get(0).getAsMention());
        // /reply(message, "WIP WIP WIP:\nGame: " + game + "\nOpponent: " + message.getMentionedUsers().get(0).getAsMention());

    }

    @Override
    public void saveMatchedGroups(Matcher matcher) {
        currentMatcher = matcher;
    }
}
