package com.upgrad.quora.service.common;

import java.util.HashMap;
import java.util.Map;

public enum DatabaseErrorCode implements ErrorCode {

    /**
     * Error message: <b>This request has caused table constraint violation</b><br>
     * <b>Cause:</b> This error could have occurred due to undetermined runtime errors.<br>
     * <b>Action: None</b><br>
     */
    DB_001("DB-001", "This request has caused table constraint violation");

    private static final Map<String, DatabaseErrorCode> LOOKUP = new HashMap<String, DatabaseErrorCode>();

    static {
        for (final DatabaseErrorCode enumeration : DatabaseErrorCode.values()) {
            LOOKUP.put(enumeration.getCode(), enumeration);
        }
    }

    private final String code;

    private final String defaultMessage;

    private DatabaseErrorCode(final String code, final String defaultMessage) {
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
