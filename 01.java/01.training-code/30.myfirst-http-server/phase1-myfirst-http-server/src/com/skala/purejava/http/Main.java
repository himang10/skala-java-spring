package com.skala.purejava.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skala.purejava.http.model.User;
import com.skala.purejava.http.repo.FileUserRepository;
import com.skala.purejava.http.repo.UserRepository;
import com.skala.purejava.http.util.JsonUtil;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        Path dataFile = Path.of("data", "users.json");

        // 1. Repository 생성 (파일 없으면 자동으로 [] 초기화됨)
        UserRepository repo = new FileUserRepository(dataFile);

        // 2. 코드에서 직접 User 객체 생성
        User u1 = new User(null, "홍길동", "hong@example.com", Arrays.asList("검술", "전략"));
        User u2 = new User(null, "임꺽정", "lim@example.com", List.of("산적질", "도술"));

        // 3. 저장 (id 자동 할당)
        repo.create(u1);
        repo.create(u2);

        // 4. 전체 조회
        var allUsers = repo.findAll();

        // 5. JSON 직렬화
        ObjectMapper mapper = JsonUtil.mapper();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(allUsers);

        // 6. 화면 출력
        System.out.println("=== 현재 저장된 사용자 목록 ===");
        System.out.println(json);
    }
}
