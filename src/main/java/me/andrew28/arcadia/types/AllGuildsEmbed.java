package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class AllGuildsEmbed extends MessageEmbedImpl{
    public AllGuildsEmbed(JDA jda){
        setTitle("Servers:");
        setColor(Color.GREEN);
        ArrayList<Field> fields = new ArrayList<>();
        for (Guild guild : jda.getGuilds()){
            fields.add(new Field(guild.getName(), String.valueOf(guild.getMembers().size()), true));
        }
        setFields(fields);
    }
}
