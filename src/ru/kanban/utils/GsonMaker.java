package ru.kanban.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.Duration;
import java.time.LocalDateTime;

public class GsonMaker {
    public static Gson getGson() {
        return new GsonBuilder().
                registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).
                registerTypeAdapter(Duration.class, new GsonDurationAdapter()).
                create();
    }
}
