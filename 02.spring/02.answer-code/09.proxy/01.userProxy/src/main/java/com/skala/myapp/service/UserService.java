package com.skala.myapp.service;

import java.util.List;
import java.util.Optional;

import com.skala.myapp.domain.User;

public interface UserService {

    public List<User> findAll(Optional<String> name);

    public Optional<User> findById(long id) ;

    public User create(User user) ;

    public Optional<User> update(long id, User updated) ;

    public boolean delete(long id);
}


