package com.skala.purejava.http.repo;

import com.skala.purejava.http.model.User;
import com.skala.purejava.http.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.nio.file.StandardCopyOption.ATOMIC_MOVE;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class FileUserRepository implements UserRepository {

    private final Path dataFile;
    private final Map<Long, User> userMap = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rw = new ReentrantReadWriteLock(true);
    private final AtomicLong idSeq = new AtomicLong(1);

    public FileUserRepository(Path dataFile) throws IOException {
        this.dataFile = dataFile;
        initFromFile();
    }

    private void initFromFile() throws IOException {
        // data 디렉토리 보장
        if (dataFile.getParent() != null) {
            Files.createDirectories(dataFile.getParent());
        }

        if (!Files.exists(dataFile)) {
            // 빈 배열로 초기화
            persistSnapshot(Collections.emptyList());
            return;
        }

        byte[] bytes = Files.readAllBytes(dataFile);
        if (bytes.length == 0) {
            persistSnapshot(Collections.emptyList());
            return;
        }

        List<User> list = JsonUtil.mapper().readValue(bytes, new TypeReference<List<User>>() {});
        for (User u : list) {
            if (u.getId() == null) continue;
            userMap.put(u.getId(), u);
        }
        // id 시퀀스 초기화
        long maxId = userMap.keySet().stream().mapToLong(Long::longValue).max().orElse(0L);
        idSeq.set(maxId + 1);
    }

    @Override
    public Collection<User> findAll() {
        rw.readLock().lock();
        try {
            return new ArrayList<>(userMap.values());
        } finally {
            rw.readLock().unlock();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        rw.readLock().lock();
        try {
            return Optional.ofNullable(userMap.get(id));
        } finally {
            rw.readLock().unlock();
        }
    }

    @Override
    public User create(User user) {
        rw.writeLock().lock();
        // 트랜잭션: 기존 스냅샷
        Map<Long, User> before = new HashMap<>(userMap);
        try {
            Long id = (user.getId() == null) ? idSeq.getAndIncrement() : user.getId();
            if (userMap.containsKey(id)) {
                throw new IllegalStateException("User already exists with id=" + id);
            }
            User toSave = new User(id, user.getName(), user.getEmail(), user.getHobbies());
            userMap.put(id, toSave);

            // 파일 반영
            persistSnapshot(new ArrayList<>(userMap.values()));

            return toSave;
        } catch (IOException ioe) {
            // 롤백
            userMap.clear();
            userMap.putAll(before);
            throw new RuntimeException("Failed to persist users.json", ioe);
        } finally {
            rw.writeLock().unlock();
        }
    }

    @Override
    public User update(Long id, User user) {
        rw.writeLock().lock();
        Map<Long, User> before = new HashMap<>(userMap);
        try {
            if (!userMap.containsKey(id)) {
                throw new NoSuchElementException("User not found: id=" + id);
            }
            User toSave = new User(id, user.getName(), user.getEmail(), user.getHobbies());
            userMap.put(id, toSave);

            persistSnapshot(new ArrayList<>(userMap.values()));
            return toSave;
        } catch (IOException ioe) {
            userMap.clear();
            userMap.putAll(before);
            throw new RuntimeException("Failed to persist users.json", ioe);
        } finally {
            rw.writeLock().unlock();
        }
    }

    @Override
    public void delete(Long id) {
        rw.writeLock().lock();
        Map<Long, User> before = new HashMap<>(userMap);
        try {
            userMap.remove(id);
            persistSnapshot(new ArrayList<>(userMap.values()));
        } catch (IOException ioe) {
            userMap.clear();
            userMap.putAll(before);
            throw new RuntimeException("Failed to persist users.json", ioe);
        } finally {
            rw.writeLock().unlock();
        }
    }

    private void persistSnapshot(List<User> snapshot) throws IOException {
        byte[] json = JsonUtil.mapper().writerWithDefaultPrettyPrinter().writeValueAsBytes(snapshot);

        // 임시 파일에 먼저 쓰고 원자적 교체
        Path tmp = dataFile.resolveSibling(dataFile.getFileName() + ".tmp");
        Files.write(tmp, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        Files.move(tmp, dataFile, REPLACE_EXISTING, ATOMIC_MOVE);
    }
}

