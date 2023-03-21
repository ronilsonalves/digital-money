package com.digitalhouse.money.accountservice.response;

import com.digitalhouse.money.accountservice.data.dto.ITransferResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransfersPage {
    private Long totalItems;
    private int totalPages;
    private int currentPage;
    private List<ITransferResponseDTO> transfers;
}
