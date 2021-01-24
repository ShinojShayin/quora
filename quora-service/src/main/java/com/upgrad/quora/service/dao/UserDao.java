package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method will save/insert all data in userEntity object to database.
     *
     * @param userEntity
     * @return UserEntity
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * For given userName parameter it will fetches corresponding userEntity object from database.
     *
     * @param userName
     * @return UserEntity
     */
    public UserEntity getUserByUserName(final String userName) {
        UserEntity userEntity = null;

        try {

            userEntity = entityManager.createNamedQuery("userByUserName", UserEntity.class)
                    .setParameter("userName", userName)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.toString());
        }

        return userEntity;
    }

    /**
     * For given email id it will fetches corresponding userEntity object from database.
     *
     * @param email
     * @return UserEntity
     */
    public UserEntity getUserByEmail(final String email) {
        UserEntity userEntity = null;

        try {

            userEntity = entityManager
                    .createNamedQuery("userByEmail", UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.toString());
        }

        return userEntity;
    }

    /**
     * This method fetch UserEntity object for giver uuid/userId
     *
     * @param userId
     * @return UserEntity
     */
    public UserEntity getUserById(final String userId) {
        UserEntity userEntity = null;

        try {
            userEntity = entityManager
                    .createNamedQuery("userByUuid", UserEntity.class)
                    .setParameter("uuid", userId)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.err.println(e.toString());
        }

        return userEntity;
    }

    /**
     * The method delete given UserEntity Object.
     *
     * @param deleteUser
     * @return
     */
    public UserEntity deleteUser(UserEntity deleteUser) {
        if (deleteUser != null) {
            entityManager.remove(deleteUser);
        }
        return deleteUser;
    }

}
