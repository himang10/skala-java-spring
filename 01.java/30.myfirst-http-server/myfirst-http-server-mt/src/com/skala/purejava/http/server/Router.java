package com.skala.purejava.http.server;

import java.util.*;
import java.util.regex.*;

public class Router {

    static class Route {
        final String method;
        final String template;     // "/users/{id}"
        final Pattern pattern;     // 정규식
        final List<String> params; // ["id"]
        final RouteHandler handler;

        Route(String method, String template, RouteHandler handler) {
            this.method = method.toUpperCase(Locale.ROOT);
            this.template = template;
            var compiled = compileTemplate(template);
            this.pattern = compiled.pattern;
            this.params  = compiled.params;
            this.handler = handler;
        }

        static class Compiled {
            Pattern pattern; List<String> params;
        }

        static Compiled compileTemplate(String t) {
            // "/users/{id}" -> "^/users/(?<id>[^/]+)$"
            List<String> names = new ArrayList<>();
            StringBuilder sb = new StringBuilder("^");
            for (String seg : t.split("/")) {
                if (seg.isEmpty()) continue;
                sb.append("/");
                if (seg.startsWith("{") && seg.endsWith("}")) {
                    String name = seg.substring(1, seg.length() - 1);
                    names.add(name);
                    sb.append("(?<").append(name).append(">").append("[^/]+").append(")");
                } else {
                    sb.append(Pattern.quote(seg));
                }
            }
            sb.append("$");
            Compiled c = new Compiled();
            c.pattern = Pattern.compile(sb.toString());
            c.params = names;
            return c;
        }

        boolean matches(String method, String path, Map<String,String> pathVarsOut) {
            if (!this.method.equalsIgnoreCase(method)) return false;
            Matcher m = pattern.matcher(path);
            if (!m.matches()) return false;
            for (String p : params) {
                pathVarsOut.put(p, m.group(p));
            }
            return true;
        }
    }

    private final List<Route> routes = new ArrayList<>();

    public void add(String method, String template, RouteHandler handler) {
        routes.add(new Route(method, template, handler));
    }

    public Optional<Matched> match(String method, String path) {
        Map<String,String> vars = new HashMap<>();
        for (Route r : routes) {
            vars.clear();
            if (r.matches(method, path, vars)) {
                return Optional.of(new Matched(r.handler, new HashMap<>(vars)));
            }
        }
        return Optional.empty();
    }

    public record Matched(RouteHandler handler, Map<String,String> pathVars) {}
}

