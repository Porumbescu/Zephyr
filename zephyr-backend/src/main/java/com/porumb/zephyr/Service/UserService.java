package com.porumb.zephyr.Service;

import com.porumb.zephyr.Entity.User;
import com.porumb.zephyr.Exception.UserNotFoundException;
import com.porumb.zephyr.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public void updateStreak(Long userId, int streak) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStreak(streak);
            userRepository.save(user);
        }
        else {
            throw new UserNotFoundException(userId);
        }
    }


}
