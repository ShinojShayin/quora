package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;


public enum GellAllQuestionByUserErrorCode implements ErrorCode {


    /**
     * Error message: <b>User is signed out.Sign in first to get create a question</b><br>
     * <b>Cause:</b> If the user has signed out.<br>
     * <b>Action: User has been signed out need to re-login to get the access again</b><br>
     */
    ATHR_002_GETALLQUESTIONBYUSER_PROMPT("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");


    private static final Map<String, GellAllQuestionByUserErrorCode> LOOKUP =
            new HashMap<String, GellAllQuestionByUserErrorCode>();

    static {
        for (final GellAllQuestionByUserErrorCode enumeration : GellAllQuestionByUserErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private GellAllQuestionByUserErrorCode(final String code, final String defaultMessage) {
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

