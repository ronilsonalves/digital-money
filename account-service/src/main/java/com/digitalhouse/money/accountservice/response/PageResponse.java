package com.digitalhouse.money.accountservice.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageResponse<T> {
    private Long totalItems;
    private int totalPages;
    private int currentPage;
    private List<T> data;
}
