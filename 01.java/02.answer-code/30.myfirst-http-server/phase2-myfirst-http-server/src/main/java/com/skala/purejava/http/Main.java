package com.skala.purejava.http;

import com.skala.purejava.http.repo.FileUserRepository;
import com.skala.purejava.http.repo.UserRepository;
import com.skala.purejava.http.server.*;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        int threads = 16;

        // 파일 저장소 준비 (부팅 시 파일→Map 로드 포함)
        Path dataFile = Path.of("data", "users.json");
        UserRepository repo = new FileUserRepository(dataFile);

        // 라우팅 내장 서버 생성 및 시작 (Socket 기반)
        HttpUserHandler server = new HttpUserHandler(port, threads, repo);
        System.out.println("SKALA 2 First HTTP Server (socket) at http://localhost:" + port);
        server.start(); // 블로킹
    }
}
