package com.hexaware.maverickBank.service.implementations;

import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Implement additional methods for user management
}