package com.vorova.todo.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(name = "DTO Page - для пагинации")
@Setter
@Getter
public class PageDto<T> {

    @Schema(title = "Количество элементов на странице")
    private int itemsOnPage;
    @Schema(title = "Общее количество элементов")
    private int totalCountItems;
    @Schema(title = "Номер данной страницы")
    private int currentPageNumber;
    @Schema(title = "Общее количество страниц")
    private int totalCountPages;
    @Schema(title = "Запрашиваемые элементы")
    private List<T> items;

}