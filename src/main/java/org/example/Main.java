package org.example;


import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        final var server = new Server(8080, Executors.newFixedThreadPool(64));

        server.addHandler("GET", "/messages", (request, out) -> {
            // Обработка GET запроса на /messages
            // TODO: Напишите код обработки запроса
            String responseBody = "This is a GET request to /messages";
            writeResponse(out, responseBody);
        });

        server.addHandler("POST", "/messages", (request, out) -> {
            // Обработка POST запроса на /messages
            // TODO: Напишите код обработки запроса
            String responseBody = "This is a POST request to /messages";
            writeResponse(out, responseBody);
        });

        server.start();
    }

    private static void writeResponse(BufferedOutputStream out, String responseBody) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + responseBody.length() + "\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                responseBody;
        out.write(response.getBytes());
        out.flush();
    }
}
