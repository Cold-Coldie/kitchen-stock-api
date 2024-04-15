package com.coldie.kitchenstocks.user.service;

import com.coldie.kitchenstocks.user.model.User;

public interface UserService {
    User getUserById(Long id);

    User createUser(User user);
}
