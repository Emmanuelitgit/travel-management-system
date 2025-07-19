package com.hotel_service.util;

import com.hotel_service.dto.PaginationPayload;
import com.hotel_service.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Slf4j
@Component
public class AppUtils {

    /**
     * This method is used to handle all responses in the application.
     * @param message
     * @param status
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static ResponseDTO getResponseDto(String message, HttpStatus status){
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setMessage(message);
        responseDto.setDate(ZonedDateTime.now());
        responseDto.setStatusCode(status.value());
        return responseDto;
    }

    /**
     * This method is used to handle all responses in the application.
     * @param message
     * @param status
     * @param data
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static ResponseDTO getResponseDto(String message, HttpStatus status, Object data){
        if(data==null){
            ResponseDTO responseDto = getResponseDto(message, status);
            return responseDto;
        }
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setMessage(message);
        responseDto.setDate(ZonedDateTime.now());
        responseDto.setStatusCode(status.value());
        responseDto.setData(data);
        return responseDto;
    }


    /**
     * This method is used to get user full name.
     * @param first
     * @param last
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static String getFullName(String first, String last){
        return first + " " + " " + last;
    }

    public static final int DEFAULT_PAGE_NUMBER = 1;
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final String DEFAULT_PAGE_SORT = "createdAt";
    public static final String DEFAULT_PAGE_SORT_DIR = "desc";

    /**
     * This method is used to set or handle pagination items.
     * @param paginationPayload
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static Pageable getPageRequest(PaginationPayload paginationPayload){
        Pageable pageable = null;
        if (paginationPayload.isPaginate() && paginationPayload.getPage()<1){
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, paginationPayload.getSize());
        }
        if (paginationPayload.isPaginate() && paginationPayload.getSize()<1){
            PageRequest.of(paginationPayload.getPage(), DEFAULT_PAGE_SIZE);
        }
        if (paginationPayload.isPaginate() && paginationPayload.getPage()<1 && paginationPayload.getSize()<1){
            pageable = PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE);
        }else {
            pageable = PageRequest.of(paginationPayload.getPage()-1, paginationPayload.getSize());
        }
        return pageable;
    }

    /**
     * This method is used to get authenticated user id.
     * @return UUID string
     * @auther Emmanuel Yidana
     * @createdAt 19th May 2025
     */
    public static String getAuthenticatedUserId(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * This method is used to log an error message in the catch exception block.
     * @auther Emmanuel Yidana
     * @createdAt 19th july 2025
     */
    public static void logException(String message, String value){
        log.info(message, value);
    }

    public static final ExampleMatcher SEARCH_CONDITION_MATCH_ALL = ExampleMatcher.matchingAll()
            .withMatcher("price", exact())
            .withIgnorePaths("id", "createdBy", "updatedBy", "createdAt", "updatedAt")
            .withMatcher("name", contains().ignoreCase());

    public static final ExampleMatcher SEARCH_CONDITION_MATCH_ANY = ExampleMatcher.matchingAny()
            .withMatcher("price", exact())
            .withIgnorePaths("id", "createdBy", "updatedBy", "createdAt", "updatedAt")
            .withMatcher("name", contains().ignoreCase());
}
