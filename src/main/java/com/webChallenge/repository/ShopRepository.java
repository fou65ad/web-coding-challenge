package com.webChallenge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webChallenge.model.Shop;
import com.webChallenge.model.User;



public interface ShopRepository extends JpaRepository <Shop ,String>{
	 Shop findByName(String name);
	 public List<Shop> findByOrderByDistance();

}

