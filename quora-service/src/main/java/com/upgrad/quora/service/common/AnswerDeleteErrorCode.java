package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AnswerDeleteErrorCode implements ErrorCode {

  /**
   * Error message: <b>This user has already logged out, try with login again</b><br>
   * <b>Cause:</b> If the authorize token provided by the user expired with some logged out date value in the current database.<br>
   * <b>Action: Try adding answer after the login </b><br>
   */
  ATHR_002("ATHR-002", "User is signed out.Sign in first to delete an answer"),

  /**
   * Error message: <b>This user is unauthorized to edit the answer</b><br>
   * <b>Cause:</b> If the authorize token provided by the user is different from the user mapped to answer entity in the current database.<br>
   * <b>Action: Not authorize to edit this answer </b><br>
   */
  ATHR_003("ATHR-003", "Only the answer owner or admin can delete the answer"),

  /**
   * Error message: <b>This answer does not exist for edit operation</b><br>
   * <b>Cause:</b> If the answer with uuid which is to be edited does not exist in the database.<br>
   * <b>Action: Incorrect answer reference to edit. Try with valid answer </b><br>
   */
  ANS_001("ANS-001", "Entered answer uuid does not exist");

  private static final Map<String, AnswerDeleteErrorCode> LOOKUP =
      new HashMap<String, AnswerDeleteErrorCode>();

  static {
    for (final AnswerDeleteErrorCode enumeration : AnswerDeleteErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AnswerDeleteErrorCode(final String code, final String defaultMessage) {
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
