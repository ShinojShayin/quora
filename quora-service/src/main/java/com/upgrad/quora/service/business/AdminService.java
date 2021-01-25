package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.AuthErrorCode;
import com.upgrad.quora.service.common.UserRole;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AdminService {

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private UserDao userDao;

    /**
     * This method check whether the authoriztion/accessToken exist in database if exist and relevant validation are correct
     * then it will return UserAuthEntity object else it will throw exception.
     *
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity validateUserForDelete(String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);

        if (userAuthEntity == null)
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_001.getCode(), AuthErrorCode.ATHR_001.getDefaultMessage());

        if (!userAuthEntity.getUserEntity().getRole().equals(UserRole.ADMIN.getName()))
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_003.getCode(), AuthErrorCode.ATHR_003.getDefaultMessage());

        ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();

        // if logoutAt is not null then it means user has signed out.
        if(logoutAt!=null){
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_002.getCode(), AuthErrorCode.ATHR_002.getDefaultMessage());
        }
        else{
            return userAuthEntity;
        }
    }

    /**
     * This method delete user for given userId if it exist in the database else it will throw exception.
     *
     * @param userId
     * @return
     * @throws UserNotFoundException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(final String userId) throws UserNotFoundException {
        UserEntity user = userDao.getUserById(userId);

        if (user == null)
            throw new UserNotFoundException(AuthErrorCode.USR_001_DELETED.getCode(), AuthErrorCode.USR_001_DELETED.getDefaultMessage());

        UserEntity deletedUser = userDao.deleteUser(user);

        return deletedUser;
    }
}
