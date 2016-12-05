package me.andrew28.arcadia.types.commands;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public enum CommandType {
    EXACT(ExactMatchCommand.class), STARTS_WITH(StartsWithMatchCommand.class), REGEX(RegexMatchCommand.class);
    Class clazz;
    CommandType(Class clazz){
        this.clazz = clazz;
    }
    public Class getCommandClass(){
        return clazz;
    }
    public static CommandType fromObject(Object object){
        if (object instanceof ExactMatchCommand){
            return EXACT;
        }else if (object instanceof StartsWithMatchCommand){
            return STARTS_WITH;
        }else if (object instanceof RegexMatchCommand){
            return REGEX;
        }
        return null;
    }
}
