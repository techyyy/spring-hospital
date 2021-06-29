package com.myhospital.controller;

import com.myhospital.model.Doctor;
import com.myhospital.service.DoctorService;
import com.myhospital.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class DoctorController {

    private final HttpSession session;
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(HttpSession session, DoctorService doctorService) {
        this.session = session;
        this.doctorService = doctorService;
    }

    @RequestMapping(value = "/doctors")
    public ModelAndView getPaginatedDoctors(@RequestParam("page") int pageNumber) {
        ModelAndView mav = new ModelAndView("view_all_doctors");
        String sortingParameter = session.getAttribute("sortDoctorsBy").toString();
        PageRequest pageable = PageRequest.of(pageNumber-1, 5, Sort.by(sortingParameter));
        Page<Doctor> doctorPage = doctorService.getPaginatedDoctors(pageable);
        PaginationUtils.setNumberOfPages(mav, doctorPage);
        mav.addObject("doctors", doctorPage.getContent());
        return mav;
    }

    @RequestMapping(value = "/doctors/update", method = RequestMethod.GET)
    public String showUpdateDoctorForm(@RequestParam("id") Long id, Model model) {
        Optional<Doctor> op = doctorService.getDoctorById(id);
        Doctor doctor;
        if(op.isPresent()) {
            doctor = op.get();
            model.addAttribute("doctor", doctor);
        }
        return "update_doctor";
    }

    @RequestMapping(value = "/doctors/save", method = RequestMethod.POST)
    public RedirectView saveDoctor(@ModelAttribute("doctor") Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return new RedirectView("/doctors?page=1");
    }

    @RequestMapping(value = "/doctors/sort", method = RequestMethod.GET)
    public RedirectView sortDoctors(@RequestParam("parameter") String param, HttpServletRequest request) {
        session.setAttribute("sortDoctorsBy", param);
        return new RedirectView(request.getHeader("referer"));
    }

    @RequestMapping(value = "/doctors/search", method = RequestMethod.GET)
    public ModelAndView searchForDoctors(@RequestParam("query") String query) {
        ModelAndView modelAndView = new ModelAndView("view_all_doctors");
        modelAndView.addObject("doctors", doctorService.searchByPosition(query));
        return modelAndView;
    }

    @RequestMapping(value = "/add/doctor", method = RequestMethod.GET)
    public ModelAndView showAddDoctorForm() {
        return new ModelAndView("register_a_doctor").addObject("doctor", new Doctor());
    }

    @RequestMapping(value = "/add/doctor/save", method = RequestMethod.POST)
    public RedirectView insertNewDoctor(@ModelAttribute("doctor") Doctor doctor) {
        String password = doctor.getUser().getPassword();
        doctor.getUser().setPassword(new Pbkdf2PasswordEncoder().encode(password));
        doctorService.saveDoctor(doctor);
        return new RedirectView("/doctors?page=1");
    }

}
