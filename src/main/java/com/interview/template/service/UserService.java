package com.interview.template.service;

import com.interview.template.configuration.ApplicationConfiguration;
import com.interview.template.dao.UserDao;
import com.interview.template.exceptions.DuplicateUserException;
import com.interview.template.exceptions.ReservedUsernameException;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class UserService {

    private final UserDao userDao;

    private ApplicationConfiguration applicationConfiguration;

    public List<UserEntity> getAllUsers() {
        return userDao.findAll();
    }

    public UserEntity getUser(long id) throws UserNotFoundException {
        return userDao.findOrDie(id);
    }

    public List<UserEntity> searchUsers(String searchText) {
        return userDao.searchByUserName(searchText);
    }

    public void checkUserExists(long id) throws UserNotFoundException {
        userDao.checkExists(id);
    }

    public UserEntity createUser(UserEntity userEntity) throws DuplicateUserException,ReservedUsernameException {
        try {
            if(applicationConfiguration.getBlacklist().contains(userEntity.getUsername()))
                throw new ReservedUsernameException("attempt to create user with a reserved username");

            return userDao.create(userEntity);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DuplicateUserException("user with provided username or email already exists");
        }
    }

    public void delete(long userId) throws UserNotFoundException{
        userDao.delete(userId);
    }
}
