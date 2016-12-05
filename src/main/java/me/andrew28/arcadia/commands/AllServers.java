package me.andrew28.arcadia.commands;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.RegexMatchCommand;
import me.andrew28.arcadia.types.annotations.Command;
import me.andrew28.arcadia.util.ImageUtil;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrew Tran on 12/4/2016
 */
@Command(command = "servers( detailed)?", usage = "servers[ detailed]", description = "Get all servers I am on.")
public class AllServers extends RegexMatchCommand {
    public static HashMap<String, Map.Entry<Long, MessageEmbed>> cachedGuildEmbeds = new HashMap<>();
    @Override
    public void handle(User user, Message message, ChannelType channelType, MessageReceivedEvent event) {
        if (message.getStrippedContent().contains("detailed")){
            ArrayList<MessageEmbed> embeds = new ArrayList<>();
            for (final Guild guild : Arcadia.getInstance().getJdaInstance().getGuilds()){
                Boolean generate = true;
                MessageEmbed embed = null;
                if (cachedGuildEmbeds.containsKey(guild.getId())){
                    if (System.currentTimeMillis() - cachedGuildEmbeds.get(guild.getId()).getKey() > 60000L/*60second cache*/){
                        generate = true;
                    }else{
                        generate = false;
                        embed = cachedGuildEmbeds.get(guild.getId()).getValue();
                    }
                }
                if(generate){
                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    if(guild.getIconUrl() != null){
                        embedBuilder.setThumbnail(guild.getIconUrl());
                        try{
                            final URL url = new URL(guild.getIconUrl());
                            final HttpURLConnection connection = (HttpURLConnection) url
                                    .openConnection();
                            connection.setRequestProperty(
                                    "User-Agent",
                                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
                            BufferedImage icon = ImageIO.read(connection.getInputStream());
                            embedBuilder.setColor(ImageUtil.commonColor(icon));
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        embedBuilder.setColor(Arcadia.getInstance().getRole(message.getGuild()).getColor());
                    }
                    embedBuilder.addField("Owner", guild.getOwner().getEffectiveName(), true);
                    embedBuilder.addField("Member Count", String.valueOf(guild.getMembers().size()), true);
                    embedBuilder.addField("Main Channel", "#" + guild.getPublicChannel().getName(), true);
                    embed = embedBuilder.build();
                    final Long currentTime = System.currentTimeMillis();
                    MessageEmbed finalEmbed = embed;
                    cachedGuildEmbeds.put(guild.getId(), new Map.Entry<Long, MessageEmbed>() {


                        private Long time = currentTime;
                        private MessageEmbed embed = finalEmbed;
                        @Override
                        public Long getKey() {
                            return time;
                        }

                        @Override
                        public MessageEmbed getValue() {
                            return embed;
                        }

                        @Override
                        public MessageEmbed setValue(MessageEmbed value) {
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
                    for (MessageEmbed embed : embeds){
                        reply(message, embed);
                        try {
                            sleep(200);
                        } catch (InterruptedException e) {}
                    }
                }
            }.start();
        }else{
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Servers:");
            for (Guild guild : Arcadia.getInstance().getJdaInstance().getGuilds()){
                embedBuilder.addField(guild.getName(), String.valueOf(guild.getMembers().size()), true);
            }
            embedBuilder.setColor(Arcadia.getInstance().getRole(message.getGuild()).getColor());
            reply(message, embedBuilder.build());
        }
    }
}
