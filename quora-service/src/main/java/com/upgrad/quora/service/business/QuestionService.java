package com.upgrad.quora.service.business;

import com.upgrad.quora.service.common.AuthErrorCode;
import com.upgrad.quora.service.common.QuestionDeleteErrorCode;
import com.upgrad.quora.service.common.QuestionEditErrorCode;
import com.upgrad.quora.service.common.UserRole;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserAuthDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserAuthDao userAuthDao;

    /**
     * This method will accept QuestionEntity object
     * and  return QuestionEntity along with uuid.
     *
     * @param questionEntity
     * @return questionEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        return questionDao.createQuestion(questionEntity);
    }

    /**
     * This method will accept QuestionID object and it returns the uuid of the deleted question
     *
     * @param questionId
     * @return QuestionEntity
     * @throws InvalidQuestionException
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questionId) throws InvalidQuestionException {
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        try {
            if (questionEntity != null) {
                return questionDao.deleteQuestion(questionEntity);
            }
            return questionEntity;
        } catch (InvalidQuestionException invalidQuestionException) {
            if (invalidQuestionException.getCode().equals(QuestionDeleteErrorCode.QUES_001_DELETEQUESTION_ACCESS.getCode())) {
                throw new InvalidQuestionException(QuestionDeleteErrorCode.QUES_001_DELETEQUESTION_ACCESS.getCode(), QuestionDeleteErrorCode.QUES_001_DELETEQUESTION_ACCESS.getDefaultMessage());
            }

        }
        return questionEntity;
    }

    /**
     * This method check whether the authoriztion/accessToken exist in database if exist and
     * relevant validation are correct
     * then it will return UserAuthEntity object else it will throw exception.
     *
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity validateUserForDelete(String authorization) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);

        if (userAuthEntity == null)
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_001.getCode(), AuthErrorCode.ATHR_001.getDefaultMessage());

        if (!userAuthEntity.getUserEntity().getRole().equals(UserRole.ADMIN.getName()))
            throw new AuthorizationFailedException(QuestionDeleteErrorCode.ATHR_003_DELETEQUESTION_ACCESS.getCode(), QuestionDeleteErrorCode.ATHR_003_DELETEQUESTION_ACCESS.getDefaultMessage());

        ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();

        // if logoutAt is not null then it means user has signed out.
        if (logoutAt != null) {
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_002.getCode(), AuthErrorCode.ATHR_002.getDefaultMessage());
        } else {
            return userAuthEntity;
        }
    }

    /**
     * This method check whether the authoriztion/accessToken exist in database if exist
     * and relevant validation are correct
     * then it will return UserAuthEntity object else it will throw exception.
     *
     * @param authorization
     * @return UserAuthEntity
     * @throws AuthorizationFailedException
     */
    public UserAuthEntity validateUserForEdit(String authorization, QuestionEntity questionEntity) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = userAuthDao.getUserAuthByAccessToken(authorization);

        if (userAuthEntity == null)
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_001.getCode(), AuthErrorCode.ATHR_001.getDefaultMessage());

        ZonedDateTime logoutAt = userAuthEntity.getLogoutAt();

        // if logoutAt is not null then it means user has signed out.
        if (logoutAt != null) {
            throw new AuthorizationFailedException(AuthErrorCode.ATHR_002.getCode(), AuthErrorCode.ATHR_002.getDefaultMessage());
        }

        if (!userAuthEntity.getUserEntity().getRole().equals(UserRole.ADMIN.getName()) &&
                !userAuthEntity.getUserEntity().getUuid().equals(questionEntity.getUserEntity().getUuid()))
            throw new AuthorizationFailedException(QuestionEditErrorCode.ATHR_003_EDITQUESTION_ACCESS.getCode(), QuestionEditErrorCode.ATHR_003_EDITQUESTION_ACCESS.getDefaultMessage());
        else {
            return userAuthEntity;
        }
    }

    /**
     * This method will accept Edited QuestionEntity object
     * and finally return QuestionEntity along with uuid.
     *
     * @param questionEntity
     * @return questionEntity
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestion(String questionId, QuestionEntity questionEntity) throws InvalidQuestionException  {
        QuestionEntity question = questionDao.getQuestionById(questionId);

            if (question != null) {
                questionEntity.setUuid(question.getUuid());
                questionEntity.setId(question.getId());
                return questionDao.editQuestion(questionEntity);
            }
            else{
                throw new InvalidQuestionException(QuestionEditErrorCode.QUES_001_EDITQUESTION_ACCESS.getCode(), QuestionEditErrorCode.QUES_001_EDITQUESTION_ACCESS.getDefaultMessage());

            }

    }
    /**
     * This method will accept QuestionEntity object and it generated new uuid for question
     * and finally return QuestionEntity along with uuid.
     *
     * @return questionEntity
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestions() {
        return questionDao.getAllQuestions();
    }
    /**
     * This method will accept QuestionEntity object and it generated new uuid for question
     * and finally return QuestionEntity along with uuid.
     *
     * @return questionEntity
     * @param userId
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public List<QuestionEntity> getAllQuestionsByUser(String userId) {
        return questionDao.getAllQuestionsbyUser(userId);
    }
}