package com.myhospital.controller;

import com.myhospital.model.entity.Nurse;
import com.myhospital.service.NurseService;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class NurseController {

    private final NurseService nurseService;

    public NurseController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @GetMapping(value = "/add/nurse")
    public ModelAndView showAddNurseForm() {
        return new ModelAndView("register_a_nurse").addObject("nurse", new Nurse());
    }

    @PostMapping(value = "/add/nurse/save")
    public RedirectView insertNewNurse(@ModelAttribute("nurse") Nurse nurse) {
        String password = nurse.getUser().getPassword();
        nurse.getUser().setPassword(new Pbkdf2PasswordEncoder().encode(password));
        nurseService.saveNurse(nurse);
        return new RedirectView("/");
    }

}
