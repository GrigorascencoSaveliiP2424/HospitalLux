package com.example.practica.model.enums;

public enum WardType {
    INTENSIVE_CARE("Intensive Care"),
    STANDARD("Standard"),
    POST_SURGERY("Post Surgery"),
    CHILDREN_WARD("Children Ward"),
    CHEMOTHERAPY("Chemotherapy"),
    REHABILITATION("Rehabilitation"),
    SKIN_CARE("Skin Care"),
    EYE_CARE("Eye Care"),
    ENT_STANDARD("ENT Standard"),
    GENERAL_WARD("General Ward"),
    VIP("VIP");

    private final String displayName;

    WardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
