package com.myhospital.controller;

import com.myhospital.model.Patient;
import com.myhospital.model.PatientAssignment;
import com.myhospital.service.PatientAssignmentService;
import com.myhospital.service.PatientService;
import com.myhospital.util.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class PatientController {

    private final HttpSession session;
    private final PatientService patientService;
    private final PatientAssignmentService patientAssignmentService;

    @Autowired
    public PatientController(HttpSession session, PatientService patientService, PatientAssignmentService patientAssignmentService) {
        this.session = session;
        this.patientService = patientService;
        this.patientAssignmentService = patientAssignmentService;
    }

    private Long getCurrentDoctorId() {
        return Long.valueOf(session.getAttribute("doctor_id").toString());
    }

    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    public ModelAndView getPaginatedPatients(@RequestParam("page") int pageNumber) {
        ModelAndView modelAndView = new ModelAndView("view_all_patients");
        String sortingParameter = session.getAttribute("sortPatientsBy").toString();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(sortingParameter));
        Page<Patient> patientPage = patientService.getPaginatedPatients(pageable);
        PaginationUtils.setNumberOfPages(modelAndView, patientPage);
        modelAndView.addObject("patients", patientPage.getContent());
        return modelAndView;
    }

    @RequestMapping(value="/patients/update", method = RequestMethod.GET)
    public ModelAndView showUpdatePatientForm(@RequestParam("id") Long id) {
        ModelAndView model = new ModelAndView("update_patient");
        Optional<Patient> op = patientService.getPatientById(id);
        Patient patient;
        if(op.isPresent()) {
            patient = op.get();
            model.addObject("patient", patient);
        }
        return model;
    }

    @RequestMapping(value = "/patients/save", method = RequestMethod.POST)
    public RedirectView savePatient(@ModelAttribute("patient") Patient patient) {
        patientService.savePatient(patient);
        return new RedirectView("/patients?page=1");
    }

    @RequestMapping(value = "/patients/card", method = RequestMethod.GET)
    public ModelAndView showPatientsCard(@RequestParam("id") Long id) {
        ModelAndView model = new ModelAndView("admin_view_patient_card");
        List<PatientAssignment> patientsCard = patientAssignmentService.getPatientAssignments(id);
        model.addObject("patientsCard", patientsCard);
        return model;
    }

    @RequestMapping(value = {"/patients/sort", "/mypatients/sort"}, method = RequestMethod.GET)
    public RedirectView sortPatients(@RequestParam("parameter") String param, HttpServletRequest request) {
        session.setAttribute("sortPatientsBy", param);
        return new RedirectView(request.getHeader("referer"));
    }

    @RequestMapping(value = "/patients/search", method = RequestMethod.GET)
    public ModelAndView searchForPatients(@RequestParam("query") String query) {
        ModelAndView modelAndView = new ModelAndView("view_all_patients");
        modelAndView.addObject("patients", patientService.searchForPatientLike(query));
        return modelAndView;
    }

    @RequestMapping(value = "/add/patient")
    public ModelAndView showAddPatientForm() {
        return new ModelAndView("register_a_patient").addObject("patient", new Patient());
    }

    @RequestMapping(value = "/mypatients", method = RequestMethod.GET)
    public ModelAndView getMyPatientsPaginated(@RequestParam("page") int pageNumber) {
        ModelAndView modelAndView = new ModelAndView("view_my_patients");
        String sortingParameter = session.getAttribute("sortPatientsBy").toString();
        PageRequest pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(sortingParameter));
        Long doctorId = getCurrentDoctorId();
        Page<PatientAssignment> patientPage = patientAssignmentService.getPaginatedPatientsAssignments(doctorId, pageable);
        PaginationUtils.setNumberOfPages(modelAndView, patientPage);
        List<PatientAssignment> patientAssignments = patientPage.getContent();
        modelAndView.addObject("pa", patientAssignments);
        return modelAndView;
    }

    @RequestMapping(value = "/mypatients/search", method = RequestMethod.GET)
    public ModelAndView searchForMyPatients(@RequestParam("query") String query) {
        ModelAndView modelAndView = new ModelAndView("view_my_patients");
        Long doctorId = getCurrentDoctorId();
        List<PatientAssignment> pa = patientAssignmentService.searchForMyPatients(query, doctorId);
        modelAndView.addObject("pa", pa);
        return modelAndView;
    }

    @RequestMapping(value = "/mypatients/update", method = RequestMethod.GET)
    public ModelAndView showUpdateMyPatientForm(@RequestParam("id") Long assignmentId) {
        ModelAndView modelAndView = new ModelAndView("update_my_patient");
        Long doctorId = getCurrentDoctorId();
        PatientAssignment pa = patientAssignmentService.getPatientAssignmentById(assignmentId, doctorId);
        modelAndView.addObject("pa", pa);
        return modelAndView;
    }

    @RequestMapping(value = "/mypatients/save", method = RequestMethod.POST)
    public RedirectView saveMyPatientAssignment(@ModelAttribute("pa") PatientAssignment pa) {
        patientAssignmentService.saveMyPatient(pa);
        return new RedirectView("/mypatients?page=1");
    }

    @RequestMapping(value = "/mypatients/card", method = RequestMethod.GET)
    public ModelAndView showMyPatientsCard(@RequestParam("id") Long id) {
        ModelAndView model = new ModelAndView("doctor_view_patient_card");
        List<PatientAssignment> patientAssignments = patientAssignmentService.getPatientAssignments(id);
        List<Long> listOfDoctors = patientAssignments.stream().map(patientAssignment -> patientAssignment.getDoctor().getId()).collect(Collectors.toList());
        if(!listOfDoctors.contains(Long.valueOf(session.getAttribute("doctor_id").toString()))) return new ModelAndView("errors/error-403");
        model.addObject("patientsCard", patientAssignments);
        return model;
    }

    @RequestMapping(value = "/mypatients/treatment", method = RequestMethod.GET)
    public ModelAndView showSetTreatmentForm(@RequestParam("id") Long id) {
        Long doctorId = getCurrentDoctorId();
        PatientAssignment pa = patientAssignmentService.getPatientAssignmentById(id, doctorId);
        return new ModelAndView("set_treatment").addObject("pa", pa);
    }

    @RequestMapping(value = "/mypatients/diagnosis", method = RequestMethod.GET)
    public ModelAndView showSetDiagnosisForm(@RequestParam("id") Long id) {
        Long doctorId = getCurrentDoctorId();
        PatientAssignment pa = patientAssignmentService.getPatientAssignmentById(id, doctorId);
        return new ModelAndView("set_diagnosis").addObject("pa", pa);
    }

}
