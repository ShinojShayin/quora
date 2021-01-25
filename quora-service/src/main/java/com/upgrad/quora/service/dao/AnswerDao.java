package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

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

    public List<AnswerEntity> getAnswersByQuestionId(QuestionEntity questionEntity) {

        List<AnswerEntity> answerEntity = null;
        System.out.println("questionEntity \n" + questionEntity);
        try {
            TypedQuery<AnswerEntity> queryObject = entityManager
                    .createNamedQuery("answerByQuestionId", AnswerEntity.class)
                    .setParameter("questionId", questionEntity.getId());
            System.out.println(queryObject);
            answerEntity = queryObject.getResultList();

//            answerEntity = entityManager.createNativeQuery("SELECT * FROM answer where question_id = " + questionEntity.getId(), AnswerEntity.class)
//                    .getResultList();

        } catch (NoResultException e) {
            System.err.println(e.toString());
        }
        return answerEntity;
    }
}
