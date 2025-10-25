package com.example.busreservation.service;

import com.example.busreservation.model.User;
import com.example.busreservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("Passenger");
        }
        return userRepository.save(user);
    }

    public Optional<User> loginUser(String email, String password) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent() && u.get().getPassword().equals(password)) {
            return u; // contains role info
        }
        return Optional.empty();
    }

    public boolean resetPassword(String email, String newPassword) {
        Optional<User> u = userRepository.findByEmail(email);
        if (u.isPresent()) {
            User user = u.get();
            user.setPassword(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> updateUser(Long id, User updates) {
        return userRepository.findById(id).map(existing -> {
            if (updates.getFirstName() != null) existing.setFirstName(updates.getFirstName());
            if (updates.getLastName() != null) existing.setLastName(updates.getLastName());
            if (updates.getPhone() != null) existing.setPhone(updates.getPhone());
            if (updates.getEmail() != null) existing.setEmail(updates.getEmail());
            if (updates.getNic() != null) existing.setNic(updates.getNic());
            if (updates.getPassword() != null) existing.setPassword(updates.getPassword());
            if (updates.getRole() != null) existing.setRole(updates.getRole());
            return userRepository.save(existing);
        });
    }

    public boolean deleteUser(Long id) {
        return userRepository.findById(id).map(u -> {
            userRepository.delete(u);
            return true;
        }).orElse(false);
    }
}
