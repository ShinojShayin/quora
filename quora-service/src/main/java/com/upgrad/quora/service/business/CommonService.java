package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.AuthErrorCode;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CommonService {

    @Autowired
    UserAuthDao userAuthDao;

    @Autowired
    UserDao userDao;

    /**
     * For the given authorization i.e accesstoken, it will query the user_auth table
     * and checks whether its exist or not along with if token is logout or not
     * After these given condition if token is valid then it will return proper UserAuthEntity
     * object else it will throw relevant exception
     *
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity validateUser(String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);

        if (userAuthEntity == null)
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_001.getCode(), AuthErrorCode.ATHR_001.getDefaultMessage());

        ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();

        // if logoutAt is not null then it means user has signed out.
        if(logoutAt!=null){
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_002_RELOGIN_PROMPT.getCode(), AuthErrorCode.ATHR_002_RELOGIN_PROMPT.getDefaultMessage());
        }
        else{
            return userAuthEntity;
        }
    }

    /**
     * For given uuid or userid it will return corresponding UserEntity object from database
     *
     * @param uuid
     * @return
     * @throws UserNotFoundException
     */
    public UserEntity getUserByUuid(final String uuid) throws UserNotFoundException {

        UserEntity user = userDao.getUserById(uuid);

        if (user == null)
            throw new UserNotFoundException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());

        return user;
    }
}
