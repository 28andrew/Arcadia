package me.andrew28.arcadia;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public class MessageListener extends ListenerAdapter{
    public MessageListener(JDA jda){
        jda.addEventListener(this);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event){

    }
}
