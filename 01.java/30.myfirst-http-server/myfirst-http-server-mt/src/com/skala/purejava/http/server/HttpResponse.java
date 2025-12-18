package com.skala.purejava.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpResponse {

    public static void writeJson(OutputStream out, int status, String json) throws IOException {
        byte[] body = json.getBytes(StandardCharsets.UTF_8);
        writeHeaders(out, status, "application/json; charset=utf-8", body.length);
        out.write(body);
        out.flush();
    }

    public static void writeText(OutputStream out, int status, String text) throws IOException {
        byte[] body = text.getBytes(StandardCharsets.UTF_8);
        writeHeaders(out, status, "text/plain; charset=utf-8", body.length);
        out.write(body);
        out.flush();
    }

    private static void writeHeaders(OutputStream out, int status, String contentType, int length) throws IOException {
        String statusText = switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 405 -> "Method Not Allowed";
            case 500 -> "Internal Server Error";
            default -> "OK";
        };
        String headers =
            "HTTP/1.1 " + status + " " + statusText + "\r\n" +
            "Content-Type: " + contentType + "\r\n" +
            "Content-Length: " + length + "\r\n" +
            "Connection: close\r\n" +
            "\r\n";
        out.write(headers.getBytes(StandardCharsets.UTF_8));
    }
}

