package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Request implements AutoCloseable {
    private String method;
    private String path;
    private Map<String, String> headers;
    private String body;

    private Request() {
        this.headers = new HashMap<>();
    }

    // Парсит входной поток и создает объект запроса
    public static Request parse(InputStream inputStream) throws IOException {
        Request request = new Request();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Парсинг строки запроса (первая строка запроса)
            String requestLine = reader.readLine();
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length == 3) {
                request.method = requestParts[0];
                request.path = requestParts[1];
            }

            // Парсинг заголовков запроса
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerParts = headerLine.split(": ");
                if (headerParts.length == 2) {
                    request.headers.put(headerParts[0], headerParts[1]);
                }
            }

            // Парсинг тела запроса (если оно есть)
            StringBuilder bodyBuilder = new StringBuilder();
            while (reader.ready()) {
                bodyBuilder.append((char) reader.read());
            }
            request.body = bodyBuilder.toString();
        }
        return request;
    }

    // Получает метод запроса
    public String getMethod() {
        return method;
    }

    // Получает путь запроса
    public String getPath() {
        return path;
    }

    // Получает заголовки запроса
    public Map<String, String> getHeaders() {
        return headers;
    }

    // Получает тело запроса
    public String getBody() {
        return body;
    }

    @Override
    public void close() throws Exception {
        // Реализация закрытия ресурсов
    }
}