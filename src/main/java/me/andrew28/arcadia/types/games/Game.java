package me.andrew28.arcadia.types.games;

import me.andrew28.arcadia.types.ArcadiaConsumers;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public abstract class Game {
    private String[] party;
    private TextChannel channel;
    public Game(TextChannel channel, String... party){
        this.party = party;
        this.channel = channel;
    }

    public TextChannel getChannel() {
        return channel;
    }

    public String[] getParty() {
        return party;
    }

    public void reply(String message){
        getChannel().sendMessage(message).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR);
    }

    public void reply(MessageEmbed message){
        getChannel().sendMessage(message).queue(ArcadiaConsumers.SUCCESS_MESSAGE, ArcadiaConsumers.ERROR);
    }

    public abstract String getGameName();
    public abstract String getIconURL();
    public abstract Color getAccentColor();
    public abstract void start();
}
