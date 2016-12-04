package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.entities.EmbedType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class GuildEmbed extends MessageEmbedImpl{

    private BufferedImage icon = null;
    private Guild guild;

    public GuildEmbed(Guild guild){
        this.guild = guild;
        try {
            if (guild.getIconUrl() != null){
                final URL url = new URL(guild.getIconUrl());
                final HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestProperty(
                        "User-Agent",
                        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
                icon = ImageIO.read(connection.getInputStream());
                icon = toBufferedImage(icon.getScaledInstance(32,32,Image.SCALE_SMOOTH));
            }
            setFields(getFields());
            setTitle(getTitle());
            setImage(getImage());
            setColor(getColor());
            setTimestamp(getTimestamp());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    @Override
    public String getUrl() {
        return null;
    }

    @Override
    public String getTitle() {
        return guild.getName();
    }

    @Override
    public String getDescription() {
        /*return String.join("\n", new String[]{
                "Owner: " + guild.getOwner().getEffectiveName(),
                "Member Count: " + guild.getMembers().size(),
                "Main Channel: #" + guild.getPublicChannel().getName(),
                "Roles: " + String.join(", ", guild.getRoles().stream().map(role -> role.toString()).collect(Collectors.toList()))
        });*/
        return null;
    }

    @Override
    public EmbedType getType() {
        return EmbedType.RICH;
    }

    @Override
    public Thumbnail getThumbnail() {
        return null;
    }

    @Override
    public Provider getSiteProvider() {
        return null;
    }

    @Override
    public AuthorInfo getAuthor() {
        return null;
    }

    @Override
    public VideoInfo getVideoInfo() {
        return null;
    }

    @Override
    public Footer getFooter() {
        return null;
    }

    @Override
    public ImageInfo getImage() {
        if (icon == null){
            return null;
        }
        //return new ImageInfo(guild.getIconUrl(), guild.getIconUrl(), 32, 32);
        return null;
    }

    @Override
    public List<Field> getFields() {
        ArrayList<Field> fields = new ArrayList<>();
        fields.add(new Field("Owner", guild.getOwner().getEffectiveName(), true));
        fields.add(new Field("Member Count", String.valueOf(guild.getMembers().size()), true));
        fields.add(new Field("Main Channel", "#" + guild.getPublicChannel().getName(), true));
        //fields.add(new Field("Roles", String.join(", ", guild.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList())), true));
        return fields;
    }

    public static Color commonColor(BufferedImage img){
        BufferedImage resized = toBufferedImage(img.getScaledInstance(1, 1, Image.SCALE_AREA_AVERAGING));
        Integer clr = resized.getRGB(0,0);
        int  red   = (clr & 0x00ff0000) >> 16;
        int  green = (clr & 0x0000ff00) >> 8;
        int  blue  =  clr & 0x000000ff;
        return new Color(red, green, blue);
    }
    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }
    @Override
    public Color getColor() {
        if (icon == null){
            return Color.WHITE;
        }
        Color common = commonColor(icon);
        //System.out.println(average.toString());
        return common;
    }

    @Override
    public OffsetDateTime getTimestamp() {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(System.currentTimeMillis()), ZoneId.systemDefault());
    }
}
