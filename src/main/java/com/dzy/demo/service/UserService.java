package com.dzy.demo.service;

import com.dzy.demo.dao.UserDao;
import com.dzy.demo.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx() {
        User user =new User();
        user.setId(3);
        user.setName("333abc");
        userDao.insertUser(user);

        User u2 =new User();
        u2.setId(1);
        u2.setName("111dfg");
        userDao.insertUser(u2);
        return true;
    }
}
