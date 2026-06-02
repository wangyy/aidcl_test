package com.example.userdemo.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "用户名不能为空")
        @Size(max = 64, message = "用户名不能超过64个字符")
        String username,

        @NotBlank(message = "用户所属机构不能为空")
        @Size(max = 128, message = "用户所属机构不能超过128个字符")
        String organization
) {
}
