package com.viettel.vdt2023.security.service;

import com.viettel.vdt2023.entity.UserEntity;
import com.viettel.vdt2023.repository.UserRepository;
import org.jvnet.hk2.annotations.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{


    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username);

        return UserDetailsImpl.build(user);
    }
}
