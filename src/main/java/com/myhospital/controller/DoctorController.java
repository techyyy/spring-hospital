package com.myhospital.controller;

import com.myhospital.model.entity.Doctor;
import com.myhospital.model.entity.Patient;
import com.myhospital.model.entity.PatientAssignment;
import com.myhospital.service.DoctorService;
import com.myhospital.service.PatientAssignmentService;
import com.myhospital.service.PatientService;
import com.myhospital.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class DoctorController {

    private static final String DOCTORS_ATTRIBUTE = "doctors";

    private final HttpSession session;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PatientAssignmentService patientAssignmentService;

    @Autowired
    public DoctorController(HttpSession session, DoctorService doctorService, PatientService patientService, PatientAssignmentService patientAssignmentService) {
        this.session = session;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.patientAssignmentService = patientAssignmentService;
    }

    @GetMapping(value = "/doctors")
    public ModelAndView getPaginatedDoctors(@RequestParam("page") int pageNumber) {
        ModelAndView mav = new ModelAndView("view_all_doctors");
        String sortingParameter = session.getAttribute("sortDoctorsBy").toString();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(sortingParameter));
        Page<Doctor> doctorPage = doctorService.getPaginatedDoctors(pageable);
        PaginationUtils.setNumberOfPages(mav, doctorPage);
        mav.addObject(DOCTORS_ATTRIBUTE, doctorPage.getContent());
        return mav;
    }

    @GetMapping(value = "/doctors/update")
    public ModelAndView showUpdateDoctorForm(@RequestParam("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("update_doctor");
        Optional<Doctor> op = doctorService.getDoctorById(id);
        Doctor doctor;
        if (op.isPresent()) {
            doctor = op.get();
            modelAndView.addObject("doctor", doctor);
        } else {
            return new ModelAndView("error");
        }
        return modelAndView;
    }

    @PostMapping(value = "/doctors/save")
    public RedirectView saveDoctor(@ModelAttribute("doctor") Doctor doctor) {
        doctorService.saveDoctor(doctor);
        return new RedirectView("/doctors?page=1");
    }

    @GetMapping(value = "/doctors/sort")
    public RedirectView sortDoctors(@RequestParam("parameter") String param, HttpServletRequest request) {
        session.setAttribute("sortDoctorsBy", param);
        return new RedirectView(request.getHeader("referer"));
    }

    @GetMapping(value = "/doctors/search")
    public ModelAndView searchForDoctors(@RequestParam("query") String query) {
        ModelAndView modelAndView = new ModelAndView("view_all_doctors");
        modelAndView.addObject(DOCTORS_ATTRIBUTE, doctorService.searchByPosition(query));
        return modelAndView;
    }

    @GetMapping(value = "/add/doctor")
    public ModelAndView showAddDoctorForm() {
        return new ModelAndView("register_a_doctor").addObject("doctor", new Doctor());
    }

    @PostMapping(value = "/add/doctor/save")
    public RedirectView insertNewDoctor(@ModelAttribute("doctor") Doctor doctor) {
        String password = doctor.getUser().getPassword();
        doctor.getUser().setPassword(new BCryptPasswordEncoder().encode(password));
        doctorService.saveDoctor(doctor);
        return new RedirectView("/doctors?page=1");
    }

    @GetMapping(value = "/add/appointment")
    public ModelAndView showAppointmentForm(@RequestParam("p") String position, @RequestParam("n") String fullName) {
        ModelAndView modelAndView = new ModelAndView("set_doctor_for_a_patient");
        if (!position.isEmpty()) {
            modelAndView.addObject(DOCTORS_ATTRIBUTE, doctorService.searchByPosition(position));
        } else {
            modelAndView.addObject(DOCTORS_ATTRIBUTE, doctorService.getAllDoctors());
        }
        if (!fullName.isEmpty()) {
            modelAndView.addObject("patients", patientService.searchForPatientLike(fullName));
        } else {
            modelAndView.addObject("patients", patientService.getAllPatients());
        }
        return modelAndView;
    }

    @PostMapping(value = "/add/appointment/save")
    public RedirectView saveNewAppointment(@RequestParam("doctorId") Long doctorId, @RequestParam("patientId") Long patientId) {
        Optional<Doctor> doctorOptional = doctorService.getDoctorById(doctorId);
        Optional<Patient> patientOptional = patientService.getPatientById(patientId);
        if (doctorOptional.isPresent() && patientOptional.isPresent()) {
            PatientAssignment patientAssignment = new PatientAssignment(doctorOptional.get(), patientOptional.get());
            patientAssignmentService.save(patientAssignment);
        } else {
            return new RedirectView("/error");
        }
        return new RedirectView("/");
    }

}
