package com.digitalhouse.money.accountservice.data.dto;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardRequestDTO {

    private Integer cvc;

    @NotBlank(message = "Name is required")
    private String first_last_name;

    @Pattern(regexp = "\\d{4}-\\d{2}", message = "Expiration date must be in the format yyyy-MM")
    private String expiration_date;

    @NotNull(message = "Card number is required")
    @Digits(integer = 16,fraction = 0)
    private Long number;
}
