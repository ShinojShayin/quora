package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.CommonService;
import com.upgrad.quora.service.common.AnswerCreationErrorCode;
import com.upgrad.quora.service.common.AuthErrorCode;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    CommonService commonService;

    @Autowired
    AnswerService answerService;


    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId,
                                                           @RequestHeader("authorization") final String authorization, AnswerRequest answerRequest) throws InvalidQuestionException, AuthorizationFailedException, SignUpRestrictedException {
        UserAuthEntity userAuth = null;
        try {
            // Verify 'authorization' in header is valid or not, if not valid it will throw exception
            userAuth = commonService.validateUser(authorization);
        } catch (AuthorizationFailedException authorizationFailedException) {
            // Use existing exception and modify the message as per the requirement
            if ( authorizationFailedException.getCode().equals(AuthErrorCode.ATHR_002_RELOGIN_PROMPT.getCode())){
                throw new AuthorizationFailedException(AnswerCreationErrorCode.ATHR_002.getCode(), AnswerCreationErrorCode.ATHR_002.getDefaultMessage());
            }
            throw authorizationFailedException;
        }

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity.setUserEntity(userAuth.getUserEntity());

        // Set UUID of question using input questionId
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(questionId);
        answerEntity.setQuestionEntity(questionEntity);

        AnswerEntity respAnswerEntity = answerService.addAnswer(answerEntity);

        AnswerResponse answerResponse =
                new AnswerResponse()
                        .id(respAnswerEntity.getUuid())
                        .status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    /**
     *
     * @param questionUuid
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    @RequestMapping(method = RequestMethod.GET, path = "/answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AnswerResponse>> getAllAnswersToQuestion (@PathVariable("questionId") final String questionUuid,
            @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        // Verify 'authorization' in header is valid or not, if not valid it will throw exception
        UserAuthEntity userAuth = commonService.validateUser(authorization);

        List<AnswerResponse> allAnswers = new ArrayList<>();

        return new ResponseEntity<List<AnswerResponse>>(allAnswers, HttpStatus.OK);
    }
}
