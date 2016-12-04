package me.andrew28.arcadia.types;

import me.andrew28.arcadia.Arcadia;
import me.andrew28.arcadia.types.annotations.Command;

/**
 * Created by Andrew Tran on 12/3/2016
 */
public class BotCommand {
    CommandType commandType;
    Command annotation;
    ICommand commandClass;
    public BotCommand(CommandType commandType, Command annotation, ICommand commandClass){
        this.commandType = commandType;
        this.annotation = annotation;
        this.commandClass = commandClass;
    }

    public String getUsage(){
        return variables(annotation.usage());
    }

    public String getDescription(){
        return annotation.description();
    }

    public boolean usesPrefix(){
        return annotation.usePrefix();
    }

    public boolean isCaseSensitive(){
        return annotation.caseSensitive();
    }


    public String variables(String s){
        s = s.replaceAll("<BOT NAME>", Arcadia.getInstance().getBotUsername());
        return s;
    }

    public String getCommandTrigger(){
        return variables((usesPrefix() ? Arcadia.PREFIX : "") + annotation.command());
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandType commandType) {
        this.commandType = commandType;
    }

    public Command getAnnotation() {
        return annotation;
    }

    public void setAnnotation(Command annotation) {
        this.annotation = annotation;
    }

    public ICommand getCommandClass() {
        return commandClass;
    }

    public void setCommandClass(ICommand commandClass) {
        this.commandClass = commandClass;
    }
}
