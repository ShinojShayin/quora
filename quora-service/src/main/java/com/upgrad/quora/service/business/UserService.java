package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.SigninErrorCode;
import com.upgrad.quora.service.common.SignoutErrorCode;
import com.upgrad.quora.service.common.SignupErrorCode;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserAuthDao userAuthDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * This method will accept user input from reqUserEntity object and it generated new uuid for user
     * and encrypt the password using PasswordCryptographyProvider and store it in object and send to
     * dao for persisting in database and finally return UserEntity along with uuid.
     * <p>
     * This method will throw SignUpRestrictedException Exception if username or emailid provided by user
     * already exist in the database
     *
     * @param reqUserEntity
     * @return UserEntity
     * @throws SignUpRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signUp(UserEntity reqUserEntity) throws SignUpRestrictedException {

        if (isUserNameExist(reqUserEntity.getUserName()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_001.getCode(), SignupErrorCode.SGR_001.getDefaultMessage());

        if (isEmailExist(reqUserEntity.getEmail()))
            throw new SignUpRestrictedException(SignupErrorCode.SGR_002.getCode(), SignupErrorCode.SGR_002.getDefaultMessage());

        reqUserEntity.setUuid(UUID.randomUUID().toString());
        String[] encryptedText = passwordCryptographyProvider.encrypt(reqUserEntity.getPassword());
        reqUserEntity.setSalt(encryptedText[0]);
        reqUserEntity.setPassword(encryptedText[1]);

        return userDao.createUser(reqUserEntity);
    }

    /**
     * Check whether for provided userName does data exist in database
     * if data found method will return true else false.
     *
     * @param userName
     * @return boolean
     */
    private boolean isUserNameExist(final String userName) {
        boolean value = userDao.getUserByUserName(userName) != null;
        return value;
    }

    /**
     * Check whether for provided email id does data exist in database
     * if data found method will return true else false.
     *
     * @param email
     * @return boolean
     */
    private boolean isEmailExist(final String email) {
        boolean value = userDao.getUserByEmail(email) != null;
        return value;
    }

    /**
     * This method encrypt the user provided password and compare that password with already existing encrypted password
     * in the database, then it will generate a token with the help of JwtTokenProvider and create UserAuthEntity
     * object save take along with other details in the user_auth table in database.
     *
     * @param username
     * @param password
     * @return UserAuthEntity
     * @throws AuthenticationFailedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signIn(final String username, final String password) throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUserName(username);

        if (userEntity == null)
            throw new AuthenticationFailedException(SigninErrorCode.ATH_001.getCode(), SigninErrorCode.ATH_001.getDefaultMessage());

        final String encryptedPassword = PasswordCryptographyProvider.encrypt(password, userEntity.getSalt());

        if (!userEntity.getPassword().equals(encryptedPassword))
            throw new AuthenticationFailedException(SigninErrorCode.ATH_002.getCode(), SigninErrorCode.ATH_002.getDefaultMessage());

        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
        UserAuthEntity userAuthEntity = new UserAuthEntity();

        userAuthEntity.setUuid(UUID.randomUUID().toString());
        userAuthEntity.setUserEntity(userEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        userAuthEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));
        userAuthEntity.setLoginAt(now);
        userAuthEntity.setExpiresAt(expiresAt);

        userAuthDao.createUserAuth(userAuthEntity);

        return userAuthEntity;
    }

    /**
     * This method initially verfies the token by searching for it in database if found, it will invalidate the token
     * by setting logoutAt DateTime. If related UserAuthEntity object not found in database then it will throw exception
     *
     * @param accessToken
     * @return UserEntity
     * @throws SignOutRestrictedException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signOut(String accessToken) throws SignOutRestrictedException {
        UserAuthEntity userAuth = userAuthDao.getUserAuthByAccessToken(accessToken);

        if (userAuth == null)
            throw new SignOutRestrictedException(SignoutErrorCode.SGR_001.getCode(), SignoutErrorCode.SGR_001.getDefaultMessage());

        userAuth.setLogoutAt(ZonedDateTime.now());
        userAuthDao.updateUserAuth(userAuth);

        return userAuth.getUserEntity();
    }
}
