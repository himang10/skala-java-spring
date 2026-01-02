package com.skala.purejava.http.server;

import java.io.OutputStream;

@FunctionalInterface
public interface RouteHandler {
    void handle(HttpRequest req, OutputStream res) throws Exception;
}
