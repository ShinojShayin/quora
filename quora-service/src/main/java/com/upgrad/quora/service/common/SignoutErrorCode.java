package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum SignoutErrorCode implements ErrorCode {

  /**
   * Error message: <b>User is not Signed in</b><br>
   * <b>Cause:</b> If the access token provided by the user does not exist in the database.<br>
   * <b>Action: Try login again and get new token</b><br>
   */
  SGR_001("SGR-001", "User is not Signed in");

  private static final Map<String, SignoutErrorCode> LOOKUP =
      new HashMap<String, SignoutErrorCode>();

  static {
    for (final SignoutErrorCode enumeration : SignoutErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private SignoutErrorCode(final String code, final String defaultMessage) {
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
