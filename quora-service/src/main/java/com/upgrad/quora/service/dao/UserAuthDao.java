package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserAuthDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method save/insert UserAuthEntity object in database
     * @param userAuthEntity
     * @return UserAuthEntity
     */
    public UserAuthEntity createUserAuth(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    /**
     * This method fetch UserAuth Object based on given access_token/authToken
     *
     * @param authToken
     * @return
     */
    public UserAuthEntity getUserAuthByAccessToken(String authToken){

        UserAuthEntity userAuthEntity = null;
        try {
            userAuthEntity = entityManager
                    .createNamedQuery("userAuthByAccessToken", UserAuthEntity.class)
                    .setParameter("accessToken", authToken)
                    .getSingleResult();
        } catch (NoResultException e) {
           System.out.println(e.toString());
        }

        return userAuthEntity;
    }

    /**
     * This method update UserAuthEntity object in database.
     *
     * @param userAuth
     */
    public void updateUserAuth(UserAuthEntity userAuth){
        entityManager.merge(userAuth);
    }
}
