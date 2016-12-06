package me.andrew28.arcadia.games;


import me.andrew28.arcadia.types.games.Game;
import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.TextChannel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Andrew Tran on 12/5/2016
 */
public class GameManager {
    private static HashMap<String,Class<? extends Game>> games = new HashMap<>();

    public static void init(){
        registerGame("Tic Tac Toe", TicTacToe.class);
    }

    public static Game newInstanceOfGame(Class<? extends Game> game, TextChannel channel, String[] party){
        try {
            Class<?extends Game> gameClass = GameManager.getGame(getGameName(game));
            Constructor<? extends Game> constructor = gameClass.getConstructor(net.dv8tion.jda.core.entities.TextChannel.class, String[].class);
            return constructor.newInstance(new Object[]{channel, party});
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String getGameName(Class<? extends Game> game) {
        for (Map.Entry<String, Class<? extends Game>> entry : games.entrySet()){
            if (entry.getValue().equals(game)){
                return entry.getKey();
            }
        }
        return null;
    }
    public static void registerGame(String gameName, Class<? extends Game> game){
        if (games.containsKey(gameName)){
            throw new IllegalArgumentException("Game with that name has already been registered");
        }
        games.put(gameName,game);
    }
    public static Set<String> getAllGameNames(){
        return games.keySet();
    }
    public static Boolean gameExists(String gameName){
        return games.containsKey(gameName);
    }
    public static Class<? extends Game> getGame(String gameName){
        return games.get(gameName);
    }
}
