package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.QuestionDetailsResponse;
import com.upgrad.quora.api.model.QuestionEditRequest;
import com.upgrad.quora.api.model.QuestionRequest;
import com.upgrad.quora.api.model.QuestionResponse;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.common.*;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
//@RequestMapping("/")
@RequestMapping(value = "/")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    CommonService commonService;
    /**
     * Endpoint to create question
     * @param authorization
     * @param questionRequest
     * @return QuestionResponse
     * @throws AuthenticationFailedException
     */

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, QuestionRequest questionRequest) throws AuthorizationFailedException {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(QuestionCreationErrorCode.ATHR_002_CREATEQUESTION_PROMPT.getCode())){
                throw new AuthorizationFailedException(QuestionCreationErrorCode.ATHR_002_CREATEQUESTION_PROMPT.getCode(), QuestionCreationErrorCode.ATHR_002_CREATEQUESTION_PROMPT.getDefaultMessage());
            }
            else if ( authorizationFailedException.getCode().equals(AuthErrorCode.USR_001.getCode())){
                throw new AuthorizationFailedException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUserEntity(userAuth.getUserEntity());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUuid(UUID.randomUUID().toString());


        QuestionEntity respQuestionEntity = questionService.createQuestion(questionEntity);

        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.id(respQuestionEntity.getUuid());
        questionResponse.status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint to delete question
     * @param authorization
     * * @param Question ID
     * @return QuestionResponse
     * @throws AuthenticationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> deleteQuestion(@PathVariable("questionId") String questionId,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException  {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getCode())){
                throw new AuthorizationFailedException(QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getCode(), QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        QuestionEntity respQuestionEntity = questionService.deleteQuestion(questionId, authorization);

        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.id(respQuestionEntity.getUuid());
        questionResponse.status("QUESTION DELETED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }

    /**
     * Endpoint to edit question
     * @param authorization
     * @param questionEditRequest
     * @param questionId
     * @return QuestionResponse
     * @throws AuthenticationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}",consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> editQuestionContent(@PathVariable("questionId") String questionId, @RequestHeader("authorization") final String authorization, QuestionEditRequest questionEditRequest) throws AuthorizationFailedException,InvalidQuestionException  {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(QuestionEditErrorCode.ATHR_002_EDITQUESTION_PROMPT.getCode())){
                throw new AuthorizationFailedException(QuestionEditErrorCode.ATHR_002_EDITQUESTION_PROMPT.getCode(), QuestionEditErrorCode.ATHR_002_EDITQUESTION_PROMPT.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        QuestionEntity respQuestionEntity = questionService.editQuestion(questionId, questionEditRequest.getContent(),
                authorization);

        QuestionResponse questionResponse = new QuestionResponse();
        questionResponse.id(respQuestionEntity.getUuid());
        questionResponse.status("QUESTION EDITED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);

    }

    /**
     * Endpoint to fetch all questions
     * @param authorization
     * @return QuestionResponse
     * @throws AuthenticationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/question/all",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException  {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(GetAllQuestionErrorCode.ATHR_002_GETALLQUESTION_PROMPT.getCode())){
                throw new AuthorizationFailedException(GetAllQuestionErrorCode.ATHR_002_GETALLQUESTION_PROMPT.getCode(), GetAllQuestionErrorCode.ATHR_002_GETALLQUESTION_PROMPT.getDefaultMessage());
            }
            else if ( authorizationFailedException.getCode().equals(AuthErrorCode.USR_001.getCode())){
                throw new AuthorizationFailedException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        List<QuestionEntity> respQuestionEntity = questionService.getAllQuestions();
        final List<QuestionDetailsResponse> questionResponseList = new ArrayList<>();

        for (QuestionEntity question : respQuestionEntity) {
            String uuid = question.getUuid();
            String content = question.getContent();
            questionResponseList.add(new QuestionDetailsResponse().id(uuid).content(content));
        }
        return new ResponseEntity<List<QuestionDetailsResponse>>(questionResponseList, HttpStatus.OK);

    }

    /**
     * Endpoint to fetch all questions created by user
     *
     * @param userId
     * @param authorization
     * @return QuestionDetailsResponse
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<QuestionDetailsResponse>> getAllQuestionsByUser(@PathVariable("userId") String userId, @RequestHeader("authorization") final String authorization)
            throws AuthorizationFailedException, InvalidQuestionException, UserNotFoundException {
        UserAuthEntity userAuth = null;
        System.out.println(userId);
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getCode())){
                throw new AuthorizationFailedException(GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getCode(), GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getDefaultMessage());
            }

            throw authorizationFailedException;
        }

        List<QuestionEntity> respQuestionEntity = questionService.getAllQuestionsByUser(userId);

        List<QuestionDetailsResponse> questionDetailResponses = new ArrayList<>();
        for (QuestionEntity questionEntity : respQuestionEntity) {
            QuestionDetailsResponse questionDetailResponse = new QuestionDetailsResponse();
            questionDetailResponse.setId(questionEntity.getUuid());
            questionDetailResponse.setContent(questionEntity.getContent());
            questionDetailResponses.add(questionDetailResponse);
        }

        return new ResponseEntity<List<QuestionDetailsResponse>>(
                questionDetailResponses, HttpStatus.OK);

    }


}

