package com.webChallenge.model;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

@Entity
public class Shop {
	
	@Id 
	  @NotEmpty(message = "*Please provide a name")
	private String name;
	private String imageLink;
    @DecimalMin(value = "0.1", inclusive = true)
    private BigDecimal distance;
	 
	    @OneToMany(mappedBy = "shop")
	 private Set<Shop_User> Shop_User; 
	
	    public Set<Shop_User>   getShop_User() {
	        return Shop_User;
	    }
	    
	    public void setBookPublishers(Set<Shop_User> Shop_User) {
	        this.Shop_User = Shop_User;
	    }

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public BigDecimal getDistance() {
		return distance;
	}
	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}
	


	
	

}
