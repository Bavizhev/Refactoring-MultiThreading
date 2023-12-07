package org.example;

import java.io.BufferedOutputStream;
import java.io.IOException;

// Функциональный интерфейс с методом для обработки запроса
@FunctionalInterface
public interface Handler {
    void handle(Request request, BufferedOutputStream responseStream) throws IOException;
}