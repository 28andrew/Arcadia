package me.andrew28.arcadia.types;

import net.dv8tion.jda.core.entities.Message;

import java.util.function.Consumer;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class ArcadiaConsumers {
    public static Consumer<Message> SUCCESS_MESSAGE = new ArcadiaConsumer<>();
    public static Consumer<Throwable> ERROR = new ArcadiaConsumer<>();

    public static class ArcadiaConsumer<T> implements Consumer<T> {
        @Override
        public void accept(T t) {
            if (t instanceof Throwable){
                ((Throwable) t).printStackTrace();
            }
        }
    }
}
