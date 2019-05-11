package com.sokeila.woody.backend;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.sokeila.woody.backend.entity.Category;

@Component
public class CategoryEnumConverter implements Converter<String, Category> {
    @Override
    public Category convert(String value) {
        return Category.valueOf(value);
    }
}
