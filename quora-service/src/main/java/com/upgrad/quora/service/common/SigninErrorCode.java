package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum SigninErrorCode implements ErrorCode {

  /**
   * Error message: <b>This username does not exist</b><br>
   * <b>Cause:</b> If the username provided by the user does not exist.<br>
   * <b>Action: Try different username or Signup with new username</b><br>
   */
  ATH_001("ATH-001", "This username does not exist"),

  /**
   * Error message: <b>Password failed</b><br>
   * <b>Cause:</b> If the password provided by the user does not match the password in the existing database.<br>
   * <b>Action: Try to remember the password and try again, in future need to add forgot password functionality</b><br>
   */
  ATH_002("ATH-002", "Password failed");

  private static final Map<String, SigninErrorCode> LOOKUP =
      new HashMap<String, SigninErrorCode>();

  static {
    for (final SigninErrorCode enumeration : SigninErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private SigninErrorCode(final String code, final String defaultMessage) {
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
