package com.skala.purejava.http.repo;

import com.skala.purejava.http.model.User;
import com.skala.purejava.http.util.JsonUtil;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileUserRepository implements UserRepository {

    private final Path dataFile;
    private final Map<Long, User> userMap = new HashMap<>();
    private long idSeq = 1L;

    public FileUserRepository(Path dataFile) throws IOException {
        this.dataFile = dataFile;
        loadFromFile();
    }

    /**
     * 파일에서 사용자 데이터를 로드합니다
     */
    private void loadFromFile() throws IOException {
        // 데이터 디렉토리 생성
        if (dataFile.getParent() != null) {
            Files.createDirectories(dataFile.getParent());
        }

        // 파일이 없으면 빈 파일 생성
        if (!Files.exists(dataFile)) {
            saveToFile();
            return;
        }

        // 파일이 비어있으면 건너뛰기
        if (Files.size(dataFile) == 0) {
            return;
        }

        // JSON 파일 읽기
        byte[] bytes = Files.readAllBytes(dataFile);
        List<User> users = JsonUtil.mapper().readValue(bytes, 
            JsonUtil.mapper().getTypeFactory().constructCollectionType(List.class, User.class));
        
        // 메모리에 로드
        for (User user : users) {
            if (user != null && user.getId() != null) {
                userMap.put(user.getId(), user);
            }
        }
        
        // 다음 ID 설정
        if (!userMap.isEmpty()) {
            idSeq = userMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0L) + 1;
        }
    }

    /**
     * 메모리 데이터를 파일에 저장합니다
     */
    private void saveToFile() throws IOException {
        List<User> users = new ArrayList<>(userMap.values());
        byte[] json = JsonUtil.mapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(users);
        Files.write(dataFile, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Collection<User> findAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public User create(User user) {
        // ID가 없으면 자동 생성
        Long id = (user.getId() == null) ? idSeq++ : user.getId();
        
        // 중복 ID 체크
        if (userMap.containsKey(id)) {
            throw new IllegalStateException("이미 존재하는 사용자 ID입니다: " + id);
        }
        
        // 새 사용자 생성
        User newUser = new User(id, user.getName(), user.getEmail(), user.getHobbies());
        userMap.put(id, newUser);
        
        // 파일에 저장
        try {
            saveToFile();
        } catch (IOException e) {
            // 저장 실패시 메모리에서 제거하고 예외 발생
            userMap.remove(id);
            throw new RuntimeException("파일 저장에 실패했습니다", e);
        }
        
        return newUser;
    }

    @Override
    public User update(Long id, User user) {
        // 사용자 존재 확인
        if (!userMap.containsKey(id)) {
            throw new NoSuchElementException("사용자를 찾을 수 없습니다: " + id);
        }
        
        // 사용자 정보 업데이트
        User updatedUser = new User(id, user.getName(), user.getEmail(), user.getHobbies());
        userMap.put(id, updatedUser);
        
        // 파일에 저장
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다", e);
        }
        
        return updatedUser;
    }

    @Override
    public void delete(Long id) {
        userMap.remove(id);
        
        // 파일에 저장
        try {
            saveToFile();
        } catch (IOException e) {
            throw new RuntimeException("파일 저장에 실패했습니다", e);
        }
    }
}