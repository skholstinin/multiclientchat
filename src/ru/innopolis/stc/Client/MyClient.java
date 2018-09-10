package ru.innopolis.stc;

import java.io.*;
import java.net.Socket;

public class MyClient {
    final Socket socket;  // это будет сокет для сервера
    final BufferedReader socketReader; // буферизированный читатель с сервера
    final BufferedWriter socketWriter; // буферизированный писатель на сервер
    final BufferedReader userInput; // буферизированный читатель пользовательского ввода с консоли

    public MyClient(String host, int port) throws IOException {
        socket = new Socket(host, port); // создаем сокет
        // создаем читателя и писателя в сокет с дефолной кодировкой UTF-8
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        // создаем читателя с консоли (от пользователя)
        userInput = new BufferedReader(new InputStreamReader(System.in));
        new Thread(new Receiver()).start();// создаем и запускаем нить асинхронного чтения из сокета
    }


    public void run() {
        System.out.println("Client running");
        while (true) {
            String userString = null;
            try {
                userString = userInput.readLine(); // читаем строку от пользователя
            } catch (IOException ignored) {
            } // с консоли эксепшена не может быть в принципе, игнорируем
            //если что-то не так или пользователь просто нажал Enter...
            if (userString == null || userString.length() == 0 || socket.isClosed()) {
                close(); // ...закрываем коннект.
                break; // до этого break мы не дойдем, но стоит он, чтобы компилятор не ругался
            } else { //...иначе...
                try {
                    socketWriter.write(userString); //пишем строку пользователя
                    socketWriter.write("\n"); //добавляем "новою строку", дабы readLine() сервера сработал
                    socketWriter.flush(); // отправляем
                } catch (IOException e) {
                    close(); // в любой ошибке - закрываем.
                }
            }
        }
    }

    public synchronized void close() {//метод синхронизирован, чтобы исключить двойное закрытие.
        if (!socket.isClosed()) { // проверяем, что сокет не закрыт...
            try {
                socket.close(); // закрываем...
                System.exit(0); // выходим!
            } catch (IOException ignored) {
                ignored.printStackTrace();
            }
        }
    }

    private class Receiver implements Runnable {
        public void run() {
            while (!socket.isClosed()) { //сходу проверяем коннект.
                String line = null;
                try {
                    line = socketReader.readLine(); // пробуем прочесть
                } catch (IOException e) { // если в момент чтения ошибка, то...
                    // проверим, что это не банальное штатное закрытие сокета сервером
                    if ("Socket closed".equals(e.getMessage())) {
                        break;
                    }
                    System.out.println("Connection lost"); // а сюда мы попадем в случае ошибок сети.
                    close(); // ну и закрываем сокет (кстати, вызвается метод класса ChatClient, есть доступ)
                }
                if (line == null) {  // строка будет null если сервер прикрыл коннект по своей инициативе, сеть работает
                    System.out.println("Server has closed connection");
                    close(); // ...закрываемся
                } else { // иначе печатаем то, что прислал сервер.
                    System.out.println("Server:" + line);
                }
            }
        }
    }
}
