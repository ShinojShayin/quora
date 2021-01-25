package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This method will create answer answerEntity object to database.
     * @param answerEntity
     * @return AnswerEntity
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String answerId) {
        AnswerEntity answerEntity = null;

        try {
            answerEntity = entityManager
                    .createNamedQuery("answerByUuid", AnswerEntity.class)
                    .setParameter("uuid", answerId)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.err.println(e.toString());
        }
        return answerEntity;
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity) {
        return  entityManager.merge(answerEntity);
    }

    public AnswerEntity deleteAnswer(AnswerEntity answerEntity) {
        if (answerEntity != null) {
            entityManager.remove(answerEntity);
        }
        return answerEntity;
    }
}
