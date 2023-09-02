package com.github.java_explore_with_me.main.category.repository;

import com.github.java_explore_with_me.main.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, PagingAndSortingRepository<Category, Long> {
}