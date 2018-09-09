package ru.innopolis.stc.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static Integer SERVER_PORT = 8881;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket socket = serverSocket.accept();
            InputStream inputStreamFromClient = socket.getInputStream();
            OutputStream outputStreamToClient = socket.getOutputStream();
            BufferedReader bufferedClientReader = new BufferedReader(new InputStreamReader(inputStreamFromClient));
            BufferedWriter bufferedClientWriter = new BufferedWriter(new OutputStreamWriter(outputStreamToClient));
            String message = null;
            while ((message = bufferedClientReader.readLine()) != null) {
                System.out.println(message);
                bufferedClientWriter.write(message + " echo\r\n");
                bufferedClientWriter.flush();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

/*public class Server {
    public static Integer SERVER_PORT = 4999;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            Socket socket = serverSocket.accept();
            InputStream fromClient = socket.getInputStream();
            OutputStream toClient = socket.getOutputStream();
            BufferedReader clientReader = new BufferedReader(new InputStreamReader(fromClient));
            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(toClient));
            String message = null;
            while ((message = clientReader.readLine()) != null) {
                System.out.println(message);
                clientWriter.write("\"" + message + "\" received \n");
                clientWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/