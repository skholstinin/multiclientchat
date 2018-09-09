package ru.innopolis.stc.Client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientNew {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

// запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента      
        try (Socket socket = new Socket("localhost", 8888);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
             DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {

            if (!socket.isOutputShutdown()) {
                if (bufferedReader.ready()) {
                    String inputData = bufferedReader.readLine();
                    dataOutputStream.writeUTF(inputData);
                    dataOutputStream.flush();
                    Thread.sleep(1000);
                }
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}