package ru.innopolis.stc.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws InterruptedException {
        try (ServerSocket server = new ServerSocket(8888)) {
            Socket client = server.accept();
            DataOutputStream dataOutputStream = new DataOutputStream(client.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(client.getInputStream());
            while (!client.isClosed()) {

                String entry = dataInputStream.readUTF();

                if (!entry.isEmpty()) {
                    dataOutputStream.writeUTF("Server reply - " + entry + " - OK");
                    dataOutputStream.flush();
                    //Thread.sleep(3000);
                    //break;
                }
                //dataOutputStream.writeUTF("Server reply - "+entry + " - OK");

                //dataOutputStream.flush();
            }

        } catch (IOException ex) {

        }
    }
}
