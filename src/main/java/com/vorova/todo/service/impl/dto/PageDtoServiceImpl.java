package com.vorova.todo.service.impl.dto;

import com.vorova.todo.dao.abstracts.dto.PageDtoDao;
import com.vorova.todo.exception.CheckRequestException;
import com.vorova.todo.models.dto.PageDto;
import com.vorova.todo.models.dto.TypeErrorDto;
import com.vorova.todo.service.abstracts.dto.PageDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PageDtoServiceImpl<T> implements PageDtoService<T> {

    Map<String, PageDtoDao<T>> pageDtoDaoMap;

    @Autowired
    public PageDtoServiceImpl(Map<String, PageDtoDao<T>> pageDtoDaoMap) {
        this.pageDtoDaoMap = pageDtoDaoMap;
    }

    @Override
    public PageDto<T> getPageDto(String pageDtoDaoName, Map<String, Object> params) throws CheckRequestException {

        List<TypeErrorDto> errors = new ArrayList<>();

        if(params.isEmpty()){
            errors.add(new TypeErrorDto("Params is empty", 1));
        }
        if((int) (params.get("itemsOnPage")) <= 0) {
            errors.add(new TypeErrorDto("Current page will not <= 0", 2));
        }

        if(!errors.isEmpty()) {
            throw new CheckRequestException(errors);
        }

        PageDto<T> pageDto = new PageDto<>();
        PageDtoDao<T> pageDtoDao;

        if(pageDtoDaoMap.containsKey(pageDtoDaoName)) {
            pageDtoDao = pageDtoDaoMap.get(pageDtoDaoName);
        } else {
            throw new RuntimeException(this.getClass().getName() + " invalid name bean of pageDtoDaoMap");
        }

        pageDto.setItemsOnPage((int) params.get("itemsOnPage"));
        pageDto.setCurrentPageNumber((int) params.get("currentPageNumber"));
        pageDto.setTotalCountItems(pageDtoDao.getCountItems(params));
        pageDto.setTotalCountPages((int) Math.ceil((double) (pageDto.getTotalCountItems() / pageDto.getItemsOnPage())));
        if (pageDto.getTotalCountPages() == 0) { pageDto.setTotalCountPages(1);}
        pageDto.setItems(pageDtoDao.getItems(params));

        if ((int) params.get("currentPageNumber") > pageDto.getTotalCountPages()) {
            errors.add(new TypeErrorDto("Page " + params.get("currentPageNumber") + " while not exist", 3));
            throw new CheckRequestException(errors);
        }

        return pageDto;
    }
}
