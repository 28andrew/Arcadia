package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.entities.Game;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class SimpleGame implements Game {
    String name;
    public SimpleGame(String name){
        this.name = name;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public GameType getType() {
        return GameType.DEFAULT;
    }
}
