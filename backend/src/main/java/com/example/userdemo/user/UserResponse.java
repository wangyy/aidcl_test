package com.example.userdemo.user;

import java.util.Objects;

public class UserResponse {

    private Long userId;
    private String username;
    private String organization;

    public UserResponse() {
    }

    public UserResponse(Long userId, String username, String organization) {
        this.userId = userId;
        this.username = username;
        this.organization = organization;
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getOrganization());
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(organization, that.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, organization);
    }
}
