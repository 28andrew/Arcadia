package me.andrew28.arcadia.games;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.games.TurnBasedGame;
import me.andrew28.arcadia.util.ObjectUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class TicTacToe extends TurnBasedGame {
    HashMap<String, User> users = new HashMap<>();

    public TicTacToe(TextChannel channel, String... party) {
        super(channel, party);
    }

    @Override
    public String getGameName() {
        return "Tic Tac Toe";
    }

    @Override
    public String getIconURL() {
        return "http://i.imgur.com/8SuyGpb.png";
    }

    @Override
    public Color getAccentColor() {
        return Color.GRAY;
    }

    @Override
    public String[] getRoleNames() {
        return new String[]{
                "ONE",
                "TWO"
        };
    }

    @Override
    public String getFirstRole() {
        return ObjectUtil.getRandom(getRoleNames());
    }

    @Override
    public void start() {
        int i = 0;
        for (String memberID : getParty()){
            users.put(getRoleNames()[i], Arcadia.getInstance().getJdaInstance().getUserById(memberID));
            i++;
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.GREEN);
        embedBuilder.setTitle("Starting Tic Tac Toe");
        embedBuilder.addField("Members: ", String.join(", ", Arrays.stream(getParty()).map(userID -> Arcadia.getInstance().getJdaInstance().getUserById(userID).getAsMention()).collect(Collectors.toList())), true);
        embedBuilder.setThumbnail("https://i.imgur.com/8URln8V.png"); // http://i.imgur.com/8URln8V.png
        reply(embedBuilder.build());
        turnAction();
    }

    @Override
    public void turnAction() {
        String currentRole = getCurrentRole();
        User currentUser = users.get(currentRole);

        nextTurn();
    }
}
