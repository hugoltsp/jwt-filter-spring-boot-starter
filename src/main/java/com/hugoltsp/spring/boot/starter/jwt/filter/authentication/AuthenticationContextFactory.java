package com.hugoltsp.spring.boot.starter.jwt.filter.authentication;

import com.hugoltsp.spring.boot.starter.jwt.filter.userdetails.UserDetails;

import java.util.Optional;

@FunctionalInterface
public interface AuthenticationContextFactory {

	AuthenticationContext create(Optional<UserDetails> userDetails);

}