package com.househelp.repository;

import com.househelp.domain.User;
import com.househelp.domain.enums.IsDelete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    User findByPhone(String phone);

    User findByUserNameOrEmail(String username, String email);

    User findByEmailOrPhone(String email, String phone);

    User findByEmail(String email);

    User findById(long id);

    List<User> findAllByIsDelete(IsDelete isDelete);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set outDate=:outDate, validataCode=:validataCode where email=:email")
    int setOutDateAndValidataCode(@Param("outDate") String outDate, @Param("validataCode") String validataCode, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set passWord=:passWord where email=:email")
    int setNewPassword(@Param("passWord") String passWord, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set introduction=:introduction where email=:email")
    int setIntroduction(@Param("introduction") String introduction, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set userName=:userName where email=:email")
    int setUserName(@Param("userName") String userName, @Param("email") String email);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set profilePicture=:profilePicture where id=:id")
    int setProfilePicture(@Param("profilePicture") String profilePicture, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set phone=:phone where id=:id")
    int setPhone(@Param("phone") String phone, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set email=:email where id=:id")
    int setEmail(@Param("email") String email, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set backgroundPicture=:backgroundPicture where id=:id")
    int setBackgroundPicture(@Param("backgroundPicture") String backgroundPicture, @Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("update User set isDelete=:isDelete where id=:id")
    int setIsDelete(@Param("isDelete") IsDelete isDelete, @Param("id") Long id);
}