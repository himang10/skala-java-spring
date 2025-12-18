package com.skala.myapp.service;

import java.util.List;
import java.util.Optional;

import com.skala.myapp.domain.User;

public class UserServiceProxy implements UserService {

    private final UserService target;

    public UserServiceProxy(UserService target) {
        this.target = target;
    }

    @Override
    public List<User> findAll(Optional<String> name) {
        String method = "getAllUsers";
        long start = before(method);
        try {
            return target.findAll(name);
        } finally {
            after(method, start);
        }
    }

    @Override
    public Optional<User> findById(long id) {
        String method = "getUserById";
        long start = before(method);
        try {
            return target.findById(id);
        } finally {
            after(method, start);
        }
    }

    @Override
    public User create(User user) {
        String method = "createUser";
        long start = before(method);
        try {
            return target.create(user);
        } finally {
            after(method, start);
        }
    }
    @Override
    public Optional<User> update(long id, User updated) {
        String method = "updateUser";
        long start = before(method);
        try {
            return target.update(id, updated);
        } finally {
            after(method, start);
        }
    }
    
    @Override
    public boolean delete(long id) {
        String method = "deleteUser";
        long start = before(method);
        try {
            return target.delete(id);
        } finally {
            after(method, start);
        }
    }

    // 공통 before/after 로직
    private long before(String method) {
        System.out.println("[Proxy BEFORE] " + method);
        return System.nanoTime();
    }

    private void after(String method, long startNs) {
        long tookMs = (System.nanoTime() - startNs) / 1_000_000;
        System.out.println("[Proxy AFTER ] " + method + " took " + tookMs + " ms");
    }
}
