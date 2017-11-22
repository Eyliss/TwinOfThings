package com.twinofthings.sezamecore.api.error;

/**
 * @author Felix Tutzer
 *         © NOUS Wissensmanagement GmbH, 2017
 */

public enum UserRegistrationErrorStatus {
    USER_IN_RECOVERY(409, "remove"), //user is in recovery state
    INCOMPLETE_USER_REGISTRATION(409, "incomplete"), //incomplete registration
    DEVICE_DELETED(409, "deleted"), //deleted device
    USER_ACTIVE(409, "active"),
    DEVICE_INACTIVE(409, "inactive"),
    INTERNAL_SERVER_ERROR(500, ""),     //TODO: check if this is triggered next time the server has internal issues
    UNKNOWN(402, "");//an active user

    private final int code;
    private final String status;

    UserRegistrationErrorStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public static UserRegistrationErrorStatus getStatus(UserErrorResponse response) {
        for (UserRegistrationErrorStatus status : values()) {
            if (status.code == response.getCode() && status.status.equals(response.getStatus())) {
                return status;
            }
        }
        return UNKNOWN;
    }
}
