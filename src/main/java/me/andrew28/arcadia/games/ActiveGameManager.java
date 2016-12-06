package me.andrew28.arcadia.games;

import me.andrew28.arcadia.types.games.Game;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Andrew Tran on 12/5/2016
 */
public class ActiveGameManager {
    private static Set<Game> activeGames = new HashSet<>();
    public static void addGame(Game game){
        activeGames.add(game);
    }
    public static void removeGame(Game game){
        activeGames.remove(game);
    }
    public static Boolean partyHasGame(String... party){
        for (Game game : activeGames){
            if (game.getParty().equals(party)){
                return true;
            }
        }
        return false;
    }
}
