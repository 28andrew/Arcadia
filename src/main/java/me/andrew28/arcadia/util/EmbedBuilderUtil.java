package me.andrew28.arcadia.util;

import net.dv8tion.jda.core.EmbedBuilder;

import java.awt.*;

/**
 * Created by Andrew Tran on 12/5/2016
 */
public class EmbedBuilderUtil {
    public static void error(EmbedBuilder embedBuilder){
        embedBuilder.setColor(Color.RED);
        embedBuilder.setAuthor("Error", "http://i.imgur.com/PicPkXf.png", "http://i.imgur.com/PicPkXf.png");
    }
}
