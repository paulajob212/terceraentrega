package com.bank.accountSystem.service;

import com.bank.accountSystem.model.Role;
import com.bank.accountSystem.model.User;
import com.bank.accountSystem.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final IUserRepository userRepository;

    public List<User> userAll(){
        return userRepository.findAll();
    }

    public User userid(int id){
        return userRepository.findById(id).orElse(null);
    }

    public User userUpdate(int id, User updated) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setUsername(updated.getUsername());
            existingUser.setEmail(updated.getEmail());
            existingUser.setPassword(updated.getPassword());
            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public User updateRole(Integer Id, Role newRole) {
        Optional<User> optionalUser = userRepository.findById(Id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            existingUser.setRole(newRole);

            return userRepository.save(existingUser);
        } else {
            return null;
        }
    }

    public String userDelete(int id){
        userRepository.deleteById(id);
        return "User removed !! " + id;
    }
}
