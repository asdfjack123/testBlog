package com.jack.blog.web.admin;

import com.jack.blog.po.User;
import com.jack.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

//    @Autowired
//    private UserService userService;

    private final UserService userService;
    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes){
        System.out.println(username);
        System.out.println(password);
        System.out.println("*****************--------------****************");
        System.out.println(userService);
        System.out.println("*****************--------------****************");
        User user = userService.checkUser(username,password);
        if(user != null){
            //先把密碼清掉
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/indexlogin";
        }
        else{
            attributes.addFlashAttribute("message","帳號密碼錯誤");
            //進到上面的loginPage()方法,最後return "admin/login";
            return "redirect:/admin";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        //進到上面的loginPage()方法,最後return "admin/login";
        return "redirect:/admin";
    }

}
