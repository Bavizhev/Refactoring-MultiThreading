package org.example;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final int port;
    private final ExecutorService threadPool;
    private final Map<String, Map<String, Handler>> handlers;

    // Конструктор, инициализирующий сервер с указанным портом и пулом потоков
    public Server(int port, int poolSize) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(poolSize);
        this.handlers = new HashMap<>();
    }

    // Метод для добавления обработчика для указанного метода и пути
    public void addHandler(String method, String path, Handler handler) {
        // Используем computeIfAbsent для безопасного добавления вложенной Map
        handlers.computeIfAbsent(method, k -> new HashMap<>()).put(path, handler);
    }

    // Метод для запуска сервера и прослушивания подключений
    public void listen() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Каждое подключение обрабатываем в отдельном потоке из пула
                threadPool.execute(() -> handleConnection(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для обработки подключения от клиента
    private void handleConnection(Socket clientSocket) {
        try (
                BufferedOutputStream responseStream = new BufferedOutputStream(clientSocket.getOutputStream());
                Request request = Request.parse(clientSocket.getInputStream())
        ) {
            Handler handler = findHandler(request.getMethod(), request.getPath());
            if (handler != null) {
                handler.handle(request, responseStream);
            } else {
                // Если не найден обработчик, возвращаем 404 Not Found
                responseStream.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                responseStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Метод для поиска обработчика по методу и пути
    private Handler findHandler(String method, String path) {
        return handlers.getOrDefault(method, new HashMap<>()).get(path);
    }

    // Точка входа в программу
    public static void main(String[] args) {
        // Пример использования:
        final var server = new Server(9999, 64);

        // Добавление хендлеров (обработчиков)
        server.addHandler("GET", "/messages", (request, responseStream) -> {
            // Обработка GET запроса на путь "/messages"
            // TODO: handlers code
        });

        server.addHandler("POST", "/messages", (request, responseStream) -> {
            // Обработка POST запроса на путь "/messages"
            // TODO: handlers code
        });

        // Запуск сервера
        server.listen();
    }
}