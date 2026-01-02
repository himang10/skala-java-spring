package com.skala.purejava.http.server;

import com.skala.purejava.http.model.User;
import com.skala.purejava.http.repo.UserRepository;
import com.skala.purejava.http.util.JsonUtil;

import java.util.NoSuchElementException;

/**
 * HttpServerLite를 상속하여 사용자 라우팅을 내장한 서버 클래스.
 * - 생성자에서 Router를 구성하고, super(port, router, threads)로 서버 초기화
 * - Main에서는 new HttpUserHandler(...).start()만 호출하면 됨
 */
public class HttpUserHandler extends HttpServerLite {

    public HttpUserHandler(int port, int threads, UserRepository repo) {
        super(port, buildRouter(repo), threads);
    }

    private static Router buildRouter(UserRepository repo) {
        Router router = new Router();

        // GET /users (전체 조회)
        router.add("GET", "/users", (req, res) -> {
            var all = repo.findAll();
            String json = JsonUtil.mapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(all);
            HttpResponse.writeJson(res, 200, json);
        });

        // GET /users/{id} (단건 조회)
        router.add("GET", "/users/{id}", (req, res) -> {
            Long id = req.pathLong("id");
            var u = repo.findById(id).orElse(null);
            if (u == null) {
                HttpResponse.writeJson(res, 404, "{\"error\":\"User not found\"}");
                return;
            }
            String json = JsonUtil.mapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(u);
            HttpResponse.writeJson(res, 200, json);
        });

        // POST /users (생성)
        router.add("POST", "/users", (req, res) -> {
            User incoming = JsonUtil.mapper().readValue(req.body, User.class);
            User created = repo.create(incoming);
            String json = JsonUtil.mapper()
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(created);
            HttpResponse.writeJson(res, 201, json);
        });

        // PUT /users/{id} (수정)
        router.add("PUT", "/users/{id}", (req, res) -> {
            Long id = req.pathLong("id");
            User incoming = JsonUtil.mapper().readValue(req.body, User.class);
            try {
                User updated = repo.update(id, incoming);
                String json = JsonUtil.mapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(updated);
                HttpResponse.writeJson(res, 200, json);
            } catch (NoSuchElementException e) {
                HttpResponse.writeJson(res, 404, "{\"error\":\"User not found\"}");
            }
        });

        // DELETE /users/{id} (삭제)
        router.add("DELETE", "/users/{id}", (req, res) -> {
            Long id = req.pathLong("id");
            repo.delete(id);
            HttpResponse.writeText(res, 204, "");
        });

        // (선택) 405 대응: 필요시 특정 경로에 대해 허용 메서드 제한 로직 추가 가능
        // 예: router.add("PUT", "/users", (req,res)-> HttpResponse.writeJson(res,405,"..."));

        return router;
    }
}

