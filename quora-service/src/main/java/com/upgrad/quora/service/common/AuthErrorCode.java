package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum AuthErrorCode implements ErrorCode {

  /**
   * Error message: <b>User has not signed in</b><br>
   * <b>Cause:</b> If the access token provided by the user does not exist in the database .<br>
   * <b>Action: Sign in to application and get new token</b><br>
   */
  ATHR_001("ATHR-001", "User has not signed in"),

  /**
   * Error message: <b>User is signed out.Sign in first to get user details</b><br>
   * <b>Cause:</b> If the user has signed out.<br>
   * <b>Action: User has been signed out need to re-login to get the access again</b><br>
   */
  ATHR_002_RELOGIN_PROMPT("ATHR-002", "User is signed out.Sign in first to get user details"),

  /**
   * Error message: <b>User is signed out.Sign in first to get user details</b><br>
   * <b>Cause:</b> If the user has signed out.<br>
   * <b>Action: none</b><br>
   */
  ATHR_002("ATHR-002", "User is signed out"),

  /**
   * Error message: <b>Unauthorized Access, Entered user is not an admin</b><br>
   * <b>Cause:</b> If the role of the user is 'nonadmin'.<br>
   * <b>Action: try this action again with user who has 'admin' user role</b><br>
   */
  ATHR_003("ATHR-003", "Unauthorized Access, Entered user is not an admin"),

  /**
   * Error message: <b>User with entered uuid does not exist</b><br>
   * <b>Cause:</b> If the user with uuid whose profile is to be retrieved does not exist in the database.<br>
   * <b>Action: Check and provide proper uuid of currently logged in user</b><br>
   */
  USR_001("USR-001", "User with entered uuid does not exist"),

  /**
   * Error message: <b>User with entered uuid to be deleted does not exist</b><br>
   * <b>Cause:</b> If the user with uuid whose profile is to be deleted does not exist in the database.<br>
   * <b>Action: This action can performed only on user with uuid existing on the database</b><br>
   */
  USR_001_DELETED("USR-001", "User with entered uuid to be deleted does not exist");

  private static final Map<String, AuthErrorCode> LOOKUP =
      new HashMap<String, AuthErrorCode>();

  static {
    for (final AuthErrorCode enumeration : AuthErrorCode.values()) {
      LOOKUP.put(enumeration.getCode(), enumeration);
    }
  }

  private final String code;

  private final String defaultMessage;

  private AuthErrorCode(final String code, final String defaultMessage) {
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
