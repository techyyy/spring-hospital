package com.myhospital.service.impl;

import com.myhospital.repository.UserRepository;
import com.myhospital.service.details.MyUserDetails;
import com.myhospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    private final HttpSession session;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(HttpSession session, UserRepository userRepository) {
        this.session = session;
        this.userRepository = userRepository;
    }

    private void writeUserIdInSession(Long id) {
        session.setAttribute("login_id", id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails userDetails = new MyUserDetails(userRepository.findByUsername(username));
        writeUserIdInSession(userDetails.getId());
        return userDetails;
    }
}
