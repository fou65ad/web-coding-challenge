package com.webChallenge.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.webChallenge.model.Shop;
import com.webChallenge.model.Shop_User;
import com.webChallenge.model.User;
import com.webChallenge.repository.ShopRepository;
import com.webChallenge.repository.ShopUserRepository;
import com.webChallenge.service.UserService;

@Controller
public class WebChallengeController {

    @Autowired
    private UserService userService;
    @Autowired
    private ShopRepository shopRepository;
    @Autowired
    private ShopUserRepository sur;
    
    private String dislike;
    private String like;
    
    @Value("${dir.images}")
    private String imageDir;
    
    @Value("${disappear.seconds}")
    private String sec;

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @RequestMapping(value="/admin/home", method = RequestMethod.GET)
    public  ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        List<Shop_User> sus=this.display(user);
        modelAndView.addObject("sus",sus);
        modelAndView.addObject("user",user);
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
    
    @RequestMapping(value="/admin/addShop", method = RequestMethod.GET)
    public ModelAndView addShop(){
        ModelAndView modelAndView = new ModelAndView();
        Shop shop = new Shop();
        shop.setImageLink("none");
        modelAndView.addObject("shop", shop);
        
        modelAndView.setViewName("admin/addShop");
        return modelAndView;
    }
    
    @RequestMapping(value = "/admin/addShop", method = RequestMethod.POST)
    public ModelAndView addShop(@Valid Shop shop, BindingResult bindingResult,
    		@RequestParam(name="picture") MultipartFile file) throws IllegalStateException, IOException {
        ModelAndView modelAndView = new ModelAndView();
        User shopExists = userService.findUserByEmail(shop.getName());
        if (shopExists != null) {
            bindingResult
                    .rejectValue("name", "error.shop",
                            "There is already a shop registered with the name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("admin/addShop");
        } else {
        	if(!(file.isEmpty())) {
                shop.setImageLink(file.getOriginalFilename());
        		file.transferTo(new File(imageDir+"/"+shop.getName()+"_"+file.getOriginalFilename()));
        	}
        	shopRepository.save(shop);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userService.findUserByEmail(auth.getName());
            List<Shop_User> sus=this.display(user);
            modelAndView.addObject("sus",sus);
            modelAndView.addObject("user",user);
            modelAndView.setViewName("admin/home");
            return modelAndView;

        }
        return modelAndView;
    }
    @RequestMapping(value="/admin/getPhoto",produces=MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getPhoto(String name,String imageLink) throws FileNotFoundException, IOException {
    	File f= new File(imageDir+"/"+name+"_"+imageLink);
    	
    	return IOUtils.toByteArray(new FileInputStream(f));
    	
    }
    
    @RequestMapping(value="/admin/like")
	public String  getProductee(@RequestParam("name")String name,Model model)
	{	
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
         Shop shop = shopRepository.findByName(name);
         List<Shop_User> suu= sur.findByUserAndShop(user,shop);
         Date date = new Date();
         Shop_User su=new Shop_User(shop,user,1,date);
         if(suu.size()==0)         
             sur.save(su);
         else if((suu.size()>0 && ((Shop_User)suu.get(0)).getLikes()!=1))  
        	 
         	sur.updateShopUserSetLikesForUserAndShop(1,date, user, shop);
         like=name;
         List<Shop_User> sus=this.display(user);
         like=null;
         model.addAttribute("sus",sus);
         model.addAttribute("user",user);
    	
        return "admin/home";
		  
	}
    
    @RequestMapping(value="/admin/likes")
	public String  likes(Model model)
	{	
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
         
         List<Shop_User> sus=sur.findByUserAndLikes(user,1);
         model.addAttribute("sus",sus);
         model.addAttribute("user",user);
    	
        return "admin/likes";
	}

    @RequestMapping(value="/admin/remove")
	public String  remove(@RequestParam("name")String name,Model model)
	{	
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
         Shop shop = shopRepository.findByName(name);
         Date date = new Date();
         sur.updateShopUserSetLikesForUserAndShop(0,date,user,shop);
         List<Shop_User> sus=sur.findByUserAndLikes(user,1);
         model.addAttribute("sus",sus);
         model.addAttribute("user",user);
        return "admin/likes";
		  
	}
    
    
    @RequestMapping(value="/admin/dislike")
	public String  dislike(@RequestParam("name")String name,Model model) throws InterruptedException
	{	
    	 Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         User user = userService.findUserByEmail(auth.getName());
         Shop shop = shopRepository.findByName(name);
         List<Shop_User> suu= sur.findByUserAndShop(user,shop);
         Date date = new Date();
         Shop_User su=new Shop_User(shop,user,3,date);
         if(suu.size()==0)         
             sur.save(su);
         else if(suu.size()>0 )  
         	 sur.updateShopUserSetLikesForUserAndShop(3,date, user, shop);
         Shop_User uss= (Shop_User) sur.findByUserAndShop(user, shop).get(0) ;
  		
  		 dislike =name;
         List<Shop_User> sus=this.display(user);
         model.addAttribute("sus",sus);
         model.addAttribute("user",user);
        dislike= null;
    	
        return "admin/home";
		  
	}
    
    public List<Shop_User> display(User user) {

        List<Shop> shops=shopRepository.findByOrderByDistance();
        List<Shop_User> sus = new ArrayList<Shop_User>();
       
        for(Shop shop:shops){
            List<Shop_User> su=sur.findByUserAndShop(user, shop);
            Shop_User su2;
            if(su.size()==0 ) {
            		su2= new Shop_User();
            		su2.setUser(user);
            		su2.setShop(shop);
            		if(!(su2.getShop().getName()).equals(dislike)) {
            			if(su2.getShop().getName().equals(like)) su2.setLike(1);
            			sus.add(su2);
            		}
           		 System.out.println(su2.getShop().getName()+" XXXXXXXXXXXXx 1" );

            }
            else if( su.size()>0 ) {
            	su2= (Shop_User) su.get(0);

            	if(su2.getLikes()!=3) {
            		if(!(su2.getShop().getName()).equals(dislike)) {
            			if(su2.getShop().getName().equals(like)) su2.setLike(1);
            			sus.add(su2);
            		}
              		 System.out.println(su2.getShop().getName()+"  "+su2.getLikes()+" XXXXXXXXXXXXx  2" );

            	}
            	else if(su2.getLikes()==3) {
            		 Date date= new Date();
            		 Long current= date.getTime();
            		 Long before=su2.getDislikes().getTime();
            		 Long elapsed=current-before;
            		 Long disappear = Long.parseLong(sec);
             		 if(elapsed>(disappear*1000)) {
            		System.out.println(su2.getShop().getName()+" time elapsed : " +elapsed);
            		if(!(su2.getShop().getName()).equals(dislike))  {
            			if(su2.getShop().getName().equals(like)) su2.setLike(1);
            			sus.add(su2);
            		}
                  		 System.out.println(su2.getShop().getName()+" XXXXXXXXXXXXx 3" );

            		 	sur.updateShopUserSetLikesForUserAndShop(0, date, user, shop);
             		 }
            		
            	}
            }

        }
        return sus;
    }
    
    
    
}
