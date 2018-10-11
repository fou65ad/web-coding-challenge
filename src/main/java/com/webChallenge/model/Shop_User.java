package com.webChallenge.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "shop_user")
public class Shop_User implements Serializable{
	@Id @GeneratedValue
	private Long id;
  
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private Shop shop;
    
   
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
	
    private int likes;
	private Date Dislikes;
	
	
	
	
	public Shop_User(Shop shop, User user, int likes, Date dislikes) {
		super();
		this.shop = shop;
		this.user = user;
		this.likes = likes;
		Dislikes = dislikes;
	}
	public Shop_User() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Shop getShop() {
		return shop;
	}
	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getLikes() {
		return likes;
	}
	public void setLike(int likes) {
		this.likes = likes;
	}
	public Date getDislikes() {
		return Dislikes;
	}
	public void setDislike(Date dislikes) {
		Dislikes = dislikes;
	}
	
	
	

}
