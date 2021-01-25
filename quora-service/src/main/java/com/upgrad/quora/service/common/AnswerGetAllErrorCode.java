package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AnswerGetAllErrorCode implements ErrorCode {

  /**
   * Error message: <b>Provided Question id does not exists</b><br>
   * <b>Cause:</b> Incorrect question uuid as per current database.<br>
   * <b>Action: Try again with valid question id</b><br>
   */
  QUES_001("QUES-001", "The question with entered uuid whose details are to be seen does not exist"),
  /**
   * Error message: <b>This user has already logged out, try with login again</b><br>
   * <b>Cause:</b> If the authorize token provided by the user expired with some logged out date value in the current database.<br>
   * <b>Action: Try adding answer after the login </b><br>
   */
  ATHR_002("ATHR-002", "User is signed out.Sign in first to get the answers");

  private static final Map<String, AnswerGetAllErrorCode> LOOKUP =
      new HashMap<String, AnswerGetAllErrorCode>();

  static {
    for (final AnswerGetAllErrorCode enumeration : AnswerGetAllErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AnswerGetAllErrorCode(final String code, final String defaultMessage) {
    this.code = code;
    this.defaultMessage = defaultMessage;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getDefaultMessage() {
    return defaultMessage;
  }
}
