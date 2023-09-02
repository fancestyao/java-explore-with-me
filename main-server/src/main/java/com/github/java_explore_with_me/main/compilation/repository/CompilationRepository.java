package com.github.java_explore_with_me.main.compilation.repository;

import com.github.java_explore_with_me.main.compilation.model.Compilation;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>,
        PagingAndSortingRepository<Compilation, Long> {
    List<Compilation> findAllByPinned(boolean pinned, Pageable pagination);
}
