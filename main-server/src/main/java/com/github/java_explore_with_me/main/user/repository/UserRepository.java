package com.github.java_explore_with_me.main.user.repository;

import com.github.java_explore_with_me.main.user.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, PagingAndSortingRepository<User, Long> {
    List<User> findAllByIdIn(List<Long> ids, Pageable pagination);
}
