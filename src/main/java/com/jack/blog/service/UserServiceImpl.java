package com.jack.blog.service;

import com.jack.blog.dao.UserRepository;
import com.jack.blog.po.User;
import com.jack.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public User checkUser(String username, String password) {
        System.out.println("*****************--------------****************");
        System.out.println(userRepository);
        System.out.println("*****************--------------****************");
        User user = userRepository.findByUsernameAndPassword(username,MD5Utils.code(password));
        return user;
    }
}
