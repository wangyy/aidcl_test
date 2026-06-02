package com.example.userdemo.user;

public record UserResponse(
        Long userId,
        String username,
        String organization
) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getUserId(), user.getUsername(), user.getOrganization());
    }
}
