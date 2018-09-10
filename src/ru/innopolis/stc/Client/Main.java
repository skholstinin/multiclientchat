package ru.innopolis.stc.Client;

import ru.innopolis.stc.MyClient;

import java.io.IOException;

public class Main {
    public static final int LOCAL_PORT = 37370;

    public static void main(String[] args) {
        try {
            new MyClient("localhost", LOCAL_PORT).run(); // Пробуем приконнетиться...
        } catch (IOException e) { // если объект не создан...
            System.out.println("Unable to connect. Server not running");
        }
    }
}
