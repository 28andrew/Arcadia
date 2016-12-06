package me.andrew28.arcadia.types.games;


import net.dv8tion.jda.core.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public abstract class TurnBasedGame extends Game {
    private String currentRole = getFirstRole();

    public TurnBasedGame(TextChannel channel, String... party) {
        super(channel, party);
    }


    public abstract String[] getRoleNames();
    public List<String> getRoleNamesAsList(){
        return Arrays.asList(getRoleNames());
    }
    public abstract String getFirstRole();
    public abstract void turnAction();
    public String getCurrentRole(){
        return currentRole;
    }
    public void setCurrentRole(String role){
        currentRole = role;
    }
    public void nextTurn(){
        ArrayList<String> roleNames = new ArrayList<>(getRoleNamesAsList());
        int index = roleNames.indexOf(getCurrentRole());
        int next = index + 1;
        if (next > (roleNames.size() - 1)){
            next = 0;
        }
        setCurrentRole(roleNames.get(next));

    }
}
