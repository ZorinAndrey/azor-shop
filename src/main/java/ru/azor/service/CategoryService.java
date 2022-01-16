package ru.azor.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.azor.dao.CategoryDao;
import ru.azor.entity.Category;
import ru.azor.web.dto.CategoryDto;
import ru.azor.web.dto.mapper.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;


    @Transactional
    public CategoryDto save(final CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        if (category.getId() != null) {
            categoryDao.findById(category.getId()).ifPresent(
                    (p) -> category.setVersion(p.getVersion())
            );
        }
        return categoryMapper.toCategoryDto(categoryDao.save(category));
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id) {
        return categoryMapper.toCategoryDto(categoryDao.findById(id).orElse(null));
    }


    public List<CategoryDto> findAll() {
        return categoryDao.findAll().stream().map(categoryMapper::toCategoryDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        try {
            categoryDao.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
        }
    }
}
