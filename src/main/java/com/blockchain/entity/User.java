package com.blockchain.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@javax.persistence.Entity
public class User implements Entity, UserDetails {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "userId")
	private Long id;

	@Column(unique = true, length = 45, nullable = false)
	private String userEmail;

	@Column(length = 64, nullable = false)
	private String userPassword;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<SysRole> userGroup = new HashSet<SysRole>();

	@Column(length = 45, nullable = false)
	private String createdAt;

	public User() {
	}

	public User(String email, String passwordHash) {
		this.userEmail = email;
		this.userPassword = passwordHash;
	}

	public void addUserGroup(SysRole role) {
		this.userGroup.add(role);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getUserGroup();
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public Long getId() {
		return this.id;
	}

	@Override
	public String getPassword() {
		return this.userPassword;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public Set<SysRole> getUserGroup() {
		return userGroup;
	}

	@Override
	public String getUsername() {
		return this.userEmail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPassword(String password) {
		this.userPassword = password;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void setUserGroup(Set<SysRole> userGroup) {
		this.userGroup = userGroup;
	}

	@Override
	public String toString() {
		return "{\\\"id\\\":" + id + ", \\\"userEmail\\\":\\\"" + userEmail + "\\\", \\\"userPassword\\\":\\\"" + userPassword
				+ "\\\", \\\"userGroup\\\":\\\"" + userGroup + "\\\", \\\"createdAt\\\":\\\"" + createdAt + "\\\"}";
	}

}
