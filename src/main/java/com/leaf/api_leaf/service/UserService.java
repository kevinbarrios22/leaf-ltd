package com.leaf.api_leaf.service;

import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Page<AppUser> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public AppUser getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void toggleEnabled(Long id) {
        AppUser user = getById(id);
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
