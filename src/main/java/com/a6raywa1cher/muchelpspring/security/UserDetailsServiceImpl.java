package com.a6raywa1cher.muchelpspring.security;

import com.a6raywa1cher.muchelpspring.model.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("user-details-service-impl")
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new DefaultUserDetails(userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)));
	}
}
