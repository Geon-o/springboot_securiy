package com.jgo.security.repository;

import com.jgo.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // findBy(규칙) -> Username(문법)
    // select * from user where username = 1?
    public User findByUsername(String username);
}
