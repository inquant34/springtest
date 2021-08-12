package springbootfinal.service;

import java.util.*;
import springbootfinal.model.User.Gender;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import springbootfinal.model.*;
import springbootfinal.dao.*;

@Service
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers(Optional<String> gender) throws IllegalArgumentException {
        // we will select all the users and then check if the users fulfil
        // a certain gender
        List<User> users = userDao.selectAllUsers();

        // that there is something in the Optional type
        if (!gender.isPresent()) {
            return users;
        }
        // or else I will filter from the lists of Users
        try {
            // the string might not be a Gender type of which an exception will be passed
            Gender theGender = Gender.valueOf(gender.get().toUpperCase());
            return users.stream().filter(user -> user.getGender().equals(theGender)).collect(Collectors.toList());
        } catch (Exception e) {
            // IllegalStateException is for when the argument passed in is not the same type
            // as the one wanted
            throw new IllegalStateException("Invalid gender", e);
        }

    }

    public Optional<User> getUser(UUID userUid) {
        return userDao.selectUserByUserUid(userUid);
    }

    public int updateUser(User user) {
        // so user must know of userUUID and then create a new user object
        // witht he same same UUid and the details to update
        Optional<User> optionalUser = getUser(user.getUserUid());
        if (optionalUser.isPresent()) {
            userDao.updateUser(user);
            return 1;
        } else {
            return -1;
        }
    }

    public int removeUser(UUID userUid) {
        Optional<User> optionalUser = getUser(userUid);
        if (optionalUser.isPresent()) {
            userDao.deleteUserByUserUid(userUid);
            return 1;
        }
        return -1;
    }

    public int insertUser(User user) {
        UUID randUid = UUID.randomUUID();
        user.setUserUid(randUid);
        userDao.insertUser(randUid, user);
        return 1;
    }
}
