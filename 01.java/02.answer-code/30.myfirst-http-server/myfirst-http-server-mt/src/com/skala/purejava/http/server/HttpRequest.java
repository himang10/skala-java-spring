package com.skala.purejava.http.server;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpRequest {
    public String method;
    public String path;                 // /users, /users/1
    public String queryString;          // a=1&b=2
    public Map<String,String> query = new HashMap<>();
    public Map<String,String> headers = new HashMap<>();
    public String body = "";
    public Map<String,String> pathVars = new HashMap<>(); // Router가 채움

    public Long pathLong(String name) {
        String v = pathVars.get(name);
        if (v == null) return null;
        try { return Long.parseLong(v); } catch (NumberFormatException e) { return null; }
    }

    static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String requestLine = br.readLine();
        if (requestLine == null || requestLine.isEmpty()) throw new IOException("empty request");
        String[] parts = requestLine.split(" ");
        if (parts.length < 3) throw new IOException("bad request line");

        HttpRequest req = new HttpRequest();
        req.method = parts[0];

        String fullPath = parts[1];
        int idx = fullPath.indexOf('?');
        if (idx >= 0) {
            req.path = fullPath.substring(0, idx);
            req.queryString = fullPath.substring(idx + 1);
            req.query = parseQuery(req.queryString);
        } else {
            req.path = fullPath;
            req.queryString = "";
        }

        String line;
        while ((line = br.readLine()) != null && !line.isEmpty()) {
            int p = line.indexOf(':');
            if (p > 0) {
                String k = line.substring(0, p).trim().toLowerCase(Locale.ROOT);
                String v = line.substring(p + 1).trim();
                req.headers.put(k, v);
            }
        }

        int contentLength = 0;
        if (req.headers.containsKey("content-length")) {
            try { contentLength = Integer.parseInt(req.headers.get("content-length")); } catch (Exception ignore) {}
        }
        if (contentLength > 0) {
            char[] buf = new char[contentLength];
            int read = br.read(buf);
            if (read > 0) req.body = new String(buf, 0, read);
        }
        return req;
    }

    static Map<String,String> parseQuery(String q) {
        Map<String,String> map = new HashMap<>();
        if (q == null || q.isEmpty()) return map;
        for (String pair : q.split("&")) {
            if (pair.isEmpty()) continue;
            String[] kv = pair.split("=", 2);
            String k = urlDecode(kv[0]);
            String v = kv.length > 1 ? urlDecode(kv[1]) : "";
            map.put(k, v);
        }
        return map;
    }

    static String urlDecode(String s) {
        try { return URLDecoder.decode(s, StandardCharsets.UTF_8.name()); }
        catch (Exception e) { return s; }
    }
}

