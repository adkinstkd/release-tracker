package com.assignment.releasetracker.dto;

public enum ReleaseStatus {

    CREATED("Created"),
    IN_DEVELOPMENT("In Development"),
    ON_DEV("On DEV"),
    QA_DONE_ON_DEV("QA Done on DEV"),
    ON_STAGING("On staging"),
    QA_DONE_ON_STAGING("QA done On STAGING"),
    ON_PROD("On PROD"),
    DONE("Done");

    private String value;

    ReleaseStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ReleaseStatus of(String value) {
        for (ReleaseStatus releaseStatus : values()) {
            if (releaseStatus.value.equals(value)) {
                return releaseStatus;
            }
        }
        throw new IllegalArgumentException(value);
    }
}
