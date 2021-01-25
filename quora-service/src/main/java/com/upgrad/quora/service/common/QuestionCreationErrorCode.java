
package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum QuestionCreationErrorCode implements ErrorCode {


    /**
     * Error message: <b>User is signed out.Sign in first to get create a question</b><br>
     * <b>Cause:</b> If the user has signed out.<br>
     * <b>Action: User has been signed out need to re-login to get the access again</b><br>
     */
    ATHR_002_CREATEQUESTION_PROMPT("ATHR-002", "User is signed out.Sign in first to post a question");


    private static final Map<String, QuestionCreationErrorCode> LOOKUP =
            new HashMap<String, QuestionCreationErrorCode>();

    static {
        for (final QuestionCreationErrorCode enumeration : QuestionCreationErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private QuestionCreationErrorCode(final String code, final String defaultMessage) {
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