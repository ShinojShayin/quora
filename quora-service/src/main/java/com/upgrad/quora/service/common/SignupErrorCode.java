package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum SignupErrorCode implements ErrorCode {

  /**
   * Error message: <b>Try any other Username, this Username has already been taken</b><br>
   * <b>Cause:</b> If the username provided already exists in the current database.<br>
   * <b>Action: Try again with another username</b><br>
   */
  SGR_001("SGR-001", "Try any other Username, this Username has already been taken"),

  /**
   * Error message: <b>This user has already been registered, try with any other emailId</b><br>
   * <b>Cause:</b> If the email Id provided by the user already exists in the current database.<br>
   * <b>Action: Try again with another email Id</b><br>
   */
  SGR_002("SGR-002", "This user has already been registered, try with any other emailId");

  private static final Map<String, SignupErrorCode> LOOKUP =
      new HashMap<String, SignupErrorCode>();

  static {
    for (final SignupErrorCode enumeration : SignupErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private SignupErrorCode(final String code, final String defaultMessage) {
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
