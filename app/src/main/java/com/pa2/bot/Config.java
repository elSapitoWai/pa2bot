package com.pa2.bot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure().directory("app/src/main").filename("env").load();

    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
