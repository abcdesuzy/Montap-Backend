package com.project.montap.domain.repository;

import com.project.montap.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// JPA -> SQL 자동 생성
// query method
public interface UserRepository extends JpaRepository<User, Long> {

    // @Query(value = "select u from User u left join u.inventoryItemList i where u.userId = :userId")
    public User findByUserId(String userId);

    // public Optional<User> findByUserId(String userId);
    public Optional<User> findByNickname(String nickname);

    public Optional<User> findByEmail(String email);

    // select * from users where user_id = {user_id} and user_pwd = {user_pwd};
    public Optional<User> findByUserIdAndUserPwd(String userId, String userPwd);

}
