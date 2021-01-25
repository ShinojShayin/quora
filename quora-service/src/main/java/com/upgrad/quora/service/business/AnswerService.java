package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.AnswerCreationErrorCode;
import com.upgrad.quora.service.common.AnswerEditErrorCode;
import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

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
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(reqAnswerEntity.getQuestionEntity().getUuid());
        if (questionEntity == null){
            throw new InvalidQuestionException(AnswerCreationErrorCode.QUES_001.getCode(), AnswerCreationErrorCode.QUES_001.getDefaultMessage());
        }
        reqAnswerEntity.setUuid(UUID.randomUUID().toString());
        reqAnswerEntity.setQuestionEntity(questionEntity);
        ZonedDateTime now = ZonedDateTime.now();
        reqAnswerEntity.setDate(now);
        return answerDao.createAnswer(reqAnswerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(AnswerEntity answerEntity) throws AnswerNotFoundException, AuthorizationFailedException {

        // Check whether answer for edit content is valid or not
        AnswerEntity dbAnswerEntity = answerDao.getAnswerById(answerEntity.getUuid());
        if (dbAnswerEntity == null){
            throw new AnswerNotFoundException(AnswerEditErrorCode.ANS_001.getCode(), AnswerEditErrorCode.ANS_001.getDefaultMessage());
        }

        // Enforce the authorization policy for edit answer as only the creator or answer should be able to modiy it
        UserEntity userEntity = userDao.getUserById(answerEntity.getUserEntity().getUuid());
        if (!dbAnswerEntity.getUserEntity().getUuid().equals(userEntity.getUuid())){
            throw new AuthorizationFailedException(AnswerEditErrorCode.ATHR_003.getCode(), AnswerEditErrorCode.ATHR_003.getDefaultMessage());
        }

        answerEntity.setQuestionEntity(dbAnswerEntity.getQuestionEntity());
        ZonedDateTime now = ZonedDateTime.now();
        answerEntity.setDate(now);

        return answerDao.editAnswer(answerEntity);
    }
}
