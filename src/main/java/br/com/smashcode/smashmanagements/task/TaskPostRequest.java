package br.com.smashcode.smashmanagements.task;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TaskPostRequest(

        @NotBlank
        String title,
        @Size(min = 10, message = "a descrição deve ter pelo menos 10 caracteres")
        String description,
        @Positive @NotNull
        Integer score
) {
}
