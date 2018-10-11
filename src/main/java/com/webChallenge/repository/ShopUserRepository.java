package com.webChallenge.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.webChallenge.model.Role;
import com.webChallenge.model.Shop;
import com.webChallenge.model.Shop_User;
import com.webChallenge.model.User;



@Transactional

public interface ShopUserRepository  extends JpaRepository<Shop_User, Long>{
	
@Query("SELECT u FROM Shop_User u WHERE u.user = ?1 AND u.likes = ?2 order by u.shop.distance")
		List<Shop_User> findByUserAndLikes(User id,int like);

	@Modifying
	@Query("update Shop_User u  set u.likes= ?1, u.Dislikes = ?2 where u.user = ?3 and u.shop= ?4")
	int updateShopUserSetLikesForUserAndShop(Integer status,Date date,  User user, Shop shop );
	
	
	@Query("SELECT u FROM Shop_User u WHERE u.user = ?1 AND u.shop = ?2 ")
	List<Shop_User> findByUserAndShop(User user,Shop shop);

	
	
	List<Shop_User> findByUser(User user);

	
}


