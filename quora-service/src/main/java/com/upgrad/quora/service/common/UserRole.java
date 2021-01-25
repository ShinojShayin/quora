package com.upgrad.quora.service.common;

/**
 * This enum contain all the type of user role in the system
 */
public enum UserRole {

    // This role will provide admin level access
    ADMIN("admin"),

    // This role will provide regular user level access
    NON_ADMIN("nonadmin");

    String name;

    UserRole(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
