package com.myhospital.listeners;

import com.myhospital.model.Role;
import com.myhospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Collection;

@Component
public class AuthSuccessListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    private final HttpSession session;
    private final DoctorService doctorService;

    @Autowired
    public AuthSuccessListener(HttpSession session, DoctorService doctorService) {
        this.session = session;
        this.doctorService = doctorService;
    }

    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
        Collection<? extends GrantedAuthority> grantedAuthorities = event.getAuthentication().getAuthorities();
        if(grantedAuthorities.contains(Role.ROLE_ADMIN)) {
            session.setAttribute("sortDoctorsBy", "id");
            session.setAttribute("sortPatientsBy", "id");
        } else if(grantedAuthorities.contains(Role.ROLE_DOCTOR)) {
            session.setAttribute("sortPatientsBy", "patient.id");
            Long loginId = Long.parseLong(session.getAttribute("login_id").toString());
            session.setAttribute("doctor_id", doctorService.getDoctorByUserId(loginId).getId());
        }
    }
}
