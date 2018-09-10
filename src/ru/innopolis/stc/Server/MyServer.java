/*
* Разработать клиент-серверный чат

1. Система состоит из двух приложений: клиента и сервера

2. В рантайме может быть любое количество экземпляров клиента. При старте каждому присваивается идентификатор

3. Клиент из консоли отправляет сообщения, которые получают все остальные клиенты и выводят у себя в консоль

4. Сервер отвечает за координацию клиентов и присвоение им идентификаторов

5. Сервер в консоли логирует все сообщения всех клиентов
* */

package ru.innopolis.stc.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class MyServer {
    BlockingQueue<SocketProcessor> processorQueue = new LinkedBlockingQueue<SocketProcessor>();//очередь, где храняться все SocketProcessorы для рассылки
    private ServerSocket serverSocket; // сам сервер-сокет
    private Thread serverThread; // главная нить обработки сервер-сокета
    private int port; // порт сервер сокета.


    public MyServer(int port) throws IOException {
        serverSocket = new ServerSocket(port); // создаем сервер-сокет
        this.port = port; // сохраняем порт.
    }

    public static void main(String[] args) throws IOException {
        new MyServer(37370).run(); // если сервер не создался, программа
        // вылетит по эксепшену, и метод run() не запуститься
    }

    void run() {
        serverThread = Thread.currentThread();
        while (true) {
            Socket s = getNewConn();
            if (serverThread.isInterrupted()) {
                break;
            } else if (s != null) {
                try {
                    final SocketProcessor processor = new SocketProcessor(s); // создаем сокет-процессор
                    final Thread thread = new Thread(processor); // создаем отдельную асинхронную нить чтения из сокета
                    thread.setDaemon(true); //ставим ее в демона (чтобы не ожидать ее закрытия)
                    thread.start(); //запускаем
                    processorQueue.offer(processor); //добавляем в список активных сокет-процессоров
                } catch (IOException ignored) {//оказывается так можно было

                }
            }
        }
    }

    private Socket getNewConn() {
        Socket socket = null;
        try {
            socket = serverSocket.accept();
        } catch (IOException e) {
        }
        return socket;
    }

    private class SocketProcessor implements Runnable {
        Socket socket; // наш сокет
        BufferedReader bufferedReader; // буферизировнный читатель сокета
        BufferedWriter bufferedWriter; // буферизированный писатель в сокет

        SocketProcessor(Socket socketParam) throws IOException {
            socket = socketParam;
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        }

        public void run() {
            while (!socket.isClosed()) { // пока сокет не закрыт...
                String line = null;
                try {
                    line = bufferedReader.readLine(); // пробуем прочесть.
                } catch (IOException e) {
                    close(); // если не получилось - закрываем сокет.
                }
                if (line == null) { // если строка null - клиент отключился в штатном режиме.
                    close(); // то закрываем сокет
                } else { // иначе - банальная рассылка по списку сокет-процессоров
                    for (SocketProcessor sp : processorQueue) {
                        sp.send(line);
                        System.out.println(line);
                    }
                }
            }
        }


        public synchronized void send(String line) {
            try {
                bufferedWriter.write(line); // пишем строку
                bufferedWriter.write("\n"); // пишем перевод строки
                bufferedWriter.flush(); // отправляем
            } catch (IOException e) {
                close(); //если глюк в момент отправки - закрываем данный сокет.
            }
        }


        public synchronized void close() {
            processorQueue.remove(this); //убираем из списка
            if (!socket.isClosed()) {
                try {
                    socket.close(); // закрываем
                } catch (IOException ignored) {
                }
            }
        }
    }
}