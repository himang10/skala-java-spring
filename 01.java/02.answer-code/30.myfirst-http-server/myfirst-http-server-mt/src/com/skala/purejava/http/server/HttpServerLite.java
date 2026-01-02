package com.skala.purejava.http.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class HttpServerLite {

    private final int port;
    private final Router router;
    private final ExecutorService pool;

    public HttpServerLite(int port, Router router, int threads) {
        this.port = port;
        this.router = router;
        this.pool = Executors.newFixedThreadPool(threads);
    }

    public void start() throws IOException {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket client = server.accept();
                System.out.println("Client connected: " + client.getInetAddress());
                pool.submit(() -> handleClient(client));
            }
        }
    }

    private void handleClient(Socket client) {
        try (client;
             InputStream in = client.getInputStream();
             OutputStream out = client.getOutputStream()) {

            HttpRequest req = HttpRequest.parse(in);
            Optional<Router.Matched> m = router.match(req.method, req.path);

            if (m.isEmpty()) {
                HttpResponse.writeJson(out, 404, "{\"error\":\"Not Found\"}");
                return;
            }

            // pathVars 주입
            req.pathVars.putAll(m.get().pathVars());

            try {
                m.get().handler().handle(req, out);
            } catch (Exception e) {
                e.printStackTrace();
                HttpResponse.writeJson(out, 500, "{\"error\":\"Internal Server Error\"}");
            }
        } catch (Exception bad) {
            try {
                OutputStream out = client.getOutputStream();
                HttpResponse.writeJson(out, 400, "{\"error\":\"Bad Request\"}");
            } catch (Exception ignore) {}
        }
    }
}

