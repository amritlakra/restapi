package com.interview.template.dao;

import com.interview.template.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("Select u from UserEntity u where u.username like %:searchText%")
    List<UserEntity> searchByUserName(@Param("searchText")String searchText);
}
