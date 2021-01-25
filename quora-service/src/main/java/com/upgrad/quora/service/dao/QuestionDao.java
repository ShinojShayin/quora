package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class QuestionDao {
    @PersistenceContext
    private EntityManager entityManager;


    public QuestionEntity getQuestionById(final String questionId) {
        QuestionEntity questionEntity = null;

        try {
            questionEntity = entityManager
                    .createNamedQuery("questionByUuid", QuestionEntity.class)
                    .setParameter("uuid", questionId)
                    .getSingleResult();

        } catch (NoResultException e) {
            System.out.println(e.toString());
        }

        return questionEntity;
    }

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) throws InvalidQuestionException {

        entityManager.remove(questionEntity);
        return questionEntity;
    }


    public List<QuestionEntity> getAllQuestions() {
        List<QuestionEntity> questionEntity = null;

        try {
            questionEntity = entityManager
                    .createNamedQuery("getAllQuestions", QuestionEntity.class)
                    .getResultList();

        } catch (NoResultException e) {
            System.out.println(e.toString());
        }

        return questionEntity;

    }

    public List<QuestionEntity> getAllQuestionsbyUser(String userId) {
        List<QuestionEntity> questionEntity = null;

        try {
            questionEntity = entityManager
                    .createNamedQuery("getAllQuestionsById", QuestionEntity.class)
                    .getResultList();

        } catch (NoResultException e) {
            System.out.println(e.toString());
        }

        return questionEntity;

    }

    public QuestionEntity editQuestion(QuestionEntity questionEntity){

        entityManager.merge(questionEntity);
        return questionEntity;

    }
}
