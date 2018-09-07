package ru.innopolis.stc.Client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        try {
            try (Socket socket = new Socket("127.0.0.1", 8888);
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
                 DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                 DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {

                while (socket.isOutputShutdown()) {
                    if (bufferedReader.ready()) {
                        System.out.println("client wait data");
                        Thread.sleep(1000);
                        String inputData = bufferedReader.readLine();
                        dataOutputStream.writeUTF(inputData);
                        dataOutputStream.flush();
                        Thread.sleep(1000);
                        if (dataInputStream.read() > -1) {
                            String inString = dataInputStream.readUTF();
                            System.out.println(inString);
                        }
                        break;
                    }
                    System.out.println("Client send and waiting...");
                    Thread.sleep(2000);
                    if (dataInputStream.read() > -1) {
                        String inString = dataInputStream.readUTF();
                        System.out.println(inString);
                    }
                    System.out.println("Close connection");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
