package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum QuestionDeleteErrorCode implements ErrorCode {


    /**
     * Error message: <b>User is signed out.Sign in first to get create a question</b><br>
     * <b>Cause:</b> If the user has signed out.<br>
     * <b>Action: User has been signed out need to re-login to get the access again</b><br>
     */
    ATHR_002_DELETEQUESTION_PROMPT("ATHR-002", "User is signed out.Sign in first to delete a question"),
    ATHR_003_DELETEQUESTION_ACCESS("ATHR-003", "Only the question owner or admin can delete the question"),
    QUES_001_DELETEQUESTION_ACCESS("QUES-001", "Entered question uuid does not exist");




    private static final Map<String, QuestionDeleteErrorCode> LOOKUP =
            new HashMap<String, QuestionDeleteErrorCode>();

    static {
        for (final QuestionDeleteErrorCode enumeration : QuestionDeleteErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private QuestionDeleteErrorCode(final String code, final String defaultMessage) {
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
