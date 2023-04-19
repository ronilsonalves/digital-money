package com.digitalhouse.money.accountservice.data.dto;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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

    @Pattern(regexp = "\\d{2}/\\d{2}", message = "Expiration date must be in the format MM/yy")
    private String expiration_date;

    @NotNull(message = "Card number is required")
    @Digits(integer = 16,fraction = 0)
    private Long number;
}
