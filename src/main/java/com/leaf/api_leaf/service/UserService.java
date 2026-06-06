package com.leaf.api_leaf.service;

import com.leaf.api_leaf.model.AppUser;
import com.leaf.api_leaf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;

    public List<AppUser> getAll(){
        return userRepository.findAll();
    }

}



