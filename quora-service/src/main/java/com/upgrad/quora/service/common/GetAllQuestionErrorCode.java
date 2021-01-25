package com.upgrad.quora.service.common;

        import java.util.HashMap;
        import java.util.Map;

public enum GetAllQuestionErrorCode implements ErrorCode {


    /**
     * Error message: <b>User is signed out.Sign in first to get create a question</b><br>
     * <b>Cause:</b> If the user has signed out.<br>
     * <b>Action: User has been signed out need to re-login to get the access again</b><br>
     */
    ATHR_002_GETALLQUESTION_PROMPT("ATHR-002", "User is signed out.Sign in first to get all questions");


    private static final Map<String, GetAllQuestionErrorCode> LOOKUP =
            new HashMap<String, GetAllQuestionErrorCode>();

    static {
        for (final GetAllQuestionErrorCode enumeration : GetAllQuestionErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private GetAllQuestionErrorCode(final String code, final String defaultMessage) {
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
