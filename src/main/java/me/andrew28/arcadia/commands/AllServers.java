package me.andrew28.arcadia.commands;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.*;
import me.andrew28.arcadia.types.annotations.Command;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Andrew Tran on 12/4/2016
 */
@Command(command = "servers( detailed)?", usage = "servers[ detailed]", description = "Get all servers I am on.")
public class AllServers extends RegexMatchCommand {
    public static HashMap<String, Map.Entry<Long, GuildEmbed>> cachedGuildEmbeds = new HashMap<>();
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        if (message.getStrippedContent().contains("detailed")){
            ArrayList<GuildEmbed> embeds = new ArrayList<>();
            for (final Guild guild : Arcadia.getInstance().getJdaInstance().getGuilds()){
                Boolean generate = true;
                GuildEmbed embed = null;
                if (cachedGuildEmbeds.containsKey(guild.getId())){
                    if (System.currentTimeMillis() - cachedGuildEmbeds.get(guild.getId()).getKey() > 10000L/*10second cache*/){
                        generate = true;
                    }else{
                        generate = false;
                        embed = cachedGuildEmbeds.get(guild.getId()).getValue();
                    }
                }
                if(generate){
                    embed = new GuildEmbed(guild);
                    final Long currentTime = System.currentTimeMillis();
                    GuildEmbed finalEmbed = embed;
                    cachedGuildEmbeds.put(guild.getId(), new Map.Entry<Long, GuildEmbed>() {


                        private Long time = currentTime;
                        private GuildEmbed embed = finalEmbed;
                        @Override
                        public Long getKey() {
                            return time;
                        }

                        @Override
                        public GuildEmbed getValue() {
                            return embed;
                        }

                        @Override
                        public GuildEmbed setValue(GuildEmbed value) {
                            embed = value;
                            return embed;
                        }

                        @Override
                        public boolean equals(Object o) {
                            return false;
                        }

                        @Override
                        public int hashCode() {
                            return 0;
                        }
                    });

                }
                embeds.add(embed);
            }
            new Thread("Send All Servers"){
                @Override
                public void run() {
                    for (GuildEmbed embed : embeds){
                        reply(message, embed);
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {}
                    }
                }
            }.start();
        }else{
            reply(message, new AllGuildsEmbed(Arcadia.getInstance().getJdaInstance()));
        }
    }
}
