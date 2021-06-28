package com.myhospital.controller;

import com.myhospital.model.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

@Controller
public class MainController {

    @RequestMapping(value= "/", method = RequestMethod.GET)
    public String home(Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        if(authorities.contains(Role.ROLE_ADMIN)) {
            return "admin_main";
        } else if(authorities.contains(Role.ROLE_DOCTOR)){
            return "doctor_main";
        } else if(authorities.contains(Role.ROLE_NURSE)) {
            return "nurse_main";
        }
        return "login";
    }
    
    @RequestMapping(value = "/locale")
    public RedirectView changeLang(HttpServletRequest request) {
        return new RedirectView(request.getHeader("referer"));
    }
}
