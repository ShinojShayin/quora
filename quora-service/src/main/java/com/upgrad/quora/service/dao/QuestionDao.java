package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method will save/insert all data in questionEntity object to database.
     *
     * @param questionEntity
     * @return QuestionEntity
     */
    public QuestionEntity createUser(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionByUuid(String questionId) {
        QuestionEntity questionEntity = null;

        try {
            questionEntity = entityManager
                    .createNamedQuery("questionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionId)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.err.println(e.toString());
        }

        return questionEntity;
    }

}
