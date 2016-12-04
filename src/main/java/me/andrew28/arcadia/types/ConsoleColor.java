package me.andrew28.arcadia.types;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public enum ConsoleColor {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    PURPLE("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m");
    String s;
    ConsoleColor(String s){
        this.s = s;
    }
    public String getTerminalRepresentation(){
        return ""; //Temporary
    }
    @Override
    public String toString(){
        return getTerminalRepresentation();
    }
}
