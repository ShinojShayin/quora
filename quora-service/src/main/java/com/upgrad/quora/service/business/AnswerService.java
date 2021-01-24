package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.AnswerCreationErrorCode;
import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerDao answerDao;

    /**
     * This method will accept user input from reqAnswerEntity object and it generated new uuid for answer
     * and store it in object and send to dao for persisting in database and finally return AnswerEntity along with uuid.
     * <p>
     * This method will throw InvalidQuestionException Exception if question provided by user does not exist in the database
     *
     * @param reqAnswerEntity
     * @return AnswerEntity
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity addAnswer(AnswerEntity reqAnswerEntity) throws InvalidQuestionException {
        // Validate UUID of the question using Question DAO
        if (!isQuestionExist(reqAnswerEntity.getQuestionEntity().getUuid())){
            throw new InvalidQuestionException(AnswerCreationErrorCode.QUES_001.getCode(), AnswerCreationErrorCode.QUES_001.getDefaultMessage());
        }
        reqAnswerEntity.setUuid(UUID.randomUUID().toString());
        return answerDao.createAnswer(reqAnswerEntity);
    }

    /**
     * Check whether for provided question does exist in database
     * if data found method will return true else false.
     *
     * @param questionId
     * @return boolean
     */
    private boolean isQuestionExist(final String questionId) {
        return questionDao.getQuestionByUuid(questionId) != null;
    }

}
