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
    public ResponseEntity<QuestionResponse> deleteQuestion(@PathVariable("uuid") String questionId,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException  {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getCode())){
                throw new AuthorizationFailedException(QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getCode(), QuestionDeleteErrorCode.ATHR_002_DELETEQUESTION_PROMPT.getDefaultMessage());
            }
            if ( authorizationFailedException.getCode().equals(QuestionDeleteErrorCode.ATHR_003_DELETEQUESTION_ACCESS.getCode())){
                throw new AuthorizationFailedException(QuestionDeleteErrorCode.ATHR_003_DELETEQUESTION_ACCESS.getCode(), QuestionDeleteErrorCode.ATHR_003_DELETEQUESTION_ACCESS.getDefaultMessage());
            }
            else if ( authorizationFailedException.getCode().equals(AuthErrorCode.USR_001.getCode())){
                throw new AuthorizationFailedException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());
            }
            throw authorizationFailedException;
        }
        questionService.validateUserForDelete(authorization);
        QuestionEntity respQuestionEntity = questionService.deleteQuestion(questionId);

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
//            if ( authorizationFailedException.getCode().equals(QuestionEditErrorCode.ATHR_003_EDITQUESTION_ACCESS.getCode())){
//                throw new AuthorizationFailedException(QuestionEditErrorCode.ATHR_003_EDITQUESTION_ACCESS.getCode(), QuestionEditErrorCode.ATHR_003_EDITQUESTION_ACCESS.getDefaultMessage());
//            }
//            else if ( authorizationFailedException.getCode().equals(AuthErrorCode.USR_001.getCode())){
//                throw new AuthorizationFailedException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());
//            }
            throw authorizationFailedException;
        }


        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setContent(questionEditRequest.getContent());
        questionEntity.setUserEntity(userAuth.getUserEntity());
        questionEntity.setDate(ZonedDateTime.now());
     //   questionEntity.setUuid(UUID.randomUUID().toString());
        questionService.validateUserForEdit(authorization, questionEntity);

        QuestionEntity respQuestionEntity = questionService.editQuestion(questionId, questionEntity);

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
    public List<ResponseEntity<QuestionDetailsResponse>> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException,InvalidQuestionException  {
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

        List<ResponseEntity<QuestionDetailsResponse>> questionResponse =new ArrayList<ResponseEntity<QuestionDetailsResponse>>() ;
  for(QuestionEntity res : respQuestionEntity){

      QuestionDetailsResponse response =
              new QuestionDetailsResponse()
                      .id(res.getUuid())
              .content(res.getContent());
      questionResponse.add(new ResponseEntity<QuestionDetailsResponse>(response, HttpStatus.OK));
  }

        return questionResponse;

    }

    /**
     * Endpoint to fetch all questions created by user
     * @param authorization
     * @param userId
     * @return QuestionResponse
     * @throws AuthenticationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<ResponseEntity<QuestionDetailsResponse>> getAllQuestionsByUser(@RequestHeader("authorization") final String authorization, @PathVariable("user_id") String userId ) throws AuthorizationFailedException,InvalidQuestionException  {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getCode())){
                throw new AuthorizationFailedException(GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getCode(), GellAllQuestionByUserErrorCode.ATHR_002_GETALLQUESTIONBYUSER_PROMPT.getDefaultMessage());
            }
            else if ( authorizationFailedException.getCode().equals(AuthErrorCode.USR_001.getCode())){
                throw new AuthorizationFailedException(AuthErrorCode.USR_001.getCode(), AuthErrorCode.USR_001.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        List<QuestionEntity> respQuestionEntity = questionService.getAllQuestionsByUser(userId);

        List<ResponseEntity<QuestionDetailsResponse>> questionResponse =new ArrayList<ResponseEntity<QuestionDetailsResponse>>() ;
        for(QuestionEntity res : respQuestionEntity){

            QuestionDetailsResponse response =
                    new QuestionDetailsResponse()
                            .id(res.getUuid())
                            .content(res.getContent());
            questionResponse.add(new ResponseEntity<QuestionDetailsResponse>(response, HttpStatus.OK));
        }

        return questionResponse;

    }


}

