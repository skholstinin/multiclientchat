package ru.innopolis.stc.Client;

import ru.innopolis.stc.Server.Server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Socket socket = null;
        try {
            socket = new Socket("localhost", Server.SERVER_PORT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            OutputStreamWriter outputStreamServerWriter = new OutputStreamWriter(socket.getOutputStream());
            InputStreamReader inputStreamServerReader = new InputStreamReader(socket.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamServerReader);
            Scanner scaner = new Scanner(System.in);
            String message = null;
            while ((message = scaner.nextLine()) != "") {
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamServerWriter);
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                System.out.println("Echo " + bufferedReader.readLine());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}