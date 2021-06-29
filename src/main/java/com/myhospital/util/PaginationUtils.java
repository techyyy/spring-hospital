package com.myhospital.util;

import org.springframework.data.domain.Page;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PaginationUtils {

    public static<T> void setNumberOfPages(ModelAndView modelAndView, Page<T> page) {
        int countOfPages = page.getTotalPages();
        if(countOfPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,countOfPages).boxed().collect(Collectors.toList());
            modelAndView.addObject("pageNumbers", pageNumbers);
        }
    }

}
