package com.upgrad.quora.api.exception;

import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.common.DatabaseErrorCode;
import com.upgrad.quora.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

/**
 * This class will intercept any exception mentioned and send response will relevant error code and error message
 */
@ControllerAdvice
public class RestExceptionHandler {

  /**
   * This method is invoked when SignUpRestrictedException thrown and relevant error code and error message
   *
   * @param exception
   * @param request
   * @return ErrorResponse
   */
  @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signUpRestrictionException(SignUpRestrictedException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.CONFLICT);
    }

  /**
   * This method is invoked when AuthenticationFailedException thrown and relevant error code and error message
   *
   * @param exception
   * @param request
   * @return ErrorResponse
   */
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(
            AuthenticationFailedException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.UNAUTHORIZED);
    }

    /**
     * This method is invoked when UserNotFoundException thrown and relevant error code and error message
     *
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFoundException(
            UserNotFoundException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.NOT_FOUND);
    }

    /**
     * This method is invoked when AuthorizationFailedException thrown and relevant error code and error message
     *
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(
            AuthorizationFailedException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.FORBIDDEN);
    }

    /**
     * This method is invoked when SignOutRestrictedException thrown and relevant error code and error message
     *
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(
                    SignOutRestrictedException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.FORBIDDEN);
    }

    /**
     * This method is invoked when InvalidQuestionException thrown and relevant error code and error message
     *
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(InvalidQuestionException.class)
    public ResponseEntity<ErrorResponse> invalidQuestionException(
            InvalidQuestionException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.NOT_FOUND);
    }

    /**
     * This method is invoked when AnswerNotFoundException thrown and relevant error code and error message
     *
     * @param exception
     * @param request
     * @return ErrorResponse
     */
    @ExceptionHandler(AnswerNotFoundException.class)
    public ResponseEntity<ErrorResponse> answerNotFoundException(
            AnswerNotFoundException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exception.getCode()).message(exception.getErrorMessage()).rootCause(getClassName(exception.toString())),
                HttpStatus.NOT_FOUND);
    }

    /**
     * This method handles database constraint exceptions
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(
            ConstraintViolationException exception, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(DatabaseErrorCode.DB_001.getCode()).message(DatabaseErrorCode.DB_001.getDefaultMessage()).rootCause(exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    /**
     * This method will extract class name from the exception string to avoid exposing entire package name
     * in rest api response
     *
     * @param classname
     * @return
     */
    private String getClassName(String classname) {
        String[] path = classname.split("[.]");
        return path[path.length - 1];
    }
}
