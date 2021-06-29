package com.myhospital.controller;

import com.myhospital.model.Nurse;
import com.myhospital.service.NurseService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @RequestMapping(value = "/add/nurse", method = RequestMethod.GET)
    public ModelAndView showAddNurseForm() {
        return new ModelAndView("register_a_nurse").addObject("nurse", new Nurse());
    }

    @RequestMapping(value = "/add/nurse/save", method = RequestMethod.POST)
    public RedirectView insertNewNurse(@ModelAttribute("nurse") Nurse nurse) {
        String password = nurse.getUser().getPassword();
        nurse.getUser().setPassword(new Pbkdf2PasswordEncoder().encode(password));
        nurseService.saveNurse(nurse);
        return new RedirectView("/");
    }

}
