package com.house.biet.auth.command.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
   @NotBlank
   @Email
   String email,

   @NotBlank
   String password
) {
}
