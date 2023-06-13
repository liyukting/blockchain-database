package com.blockchain.entity;

import org.springframework.security.core.GrantedAuthority;

public enum SysRole implements GrantedAuthority
{
	ROLE_HELPER("ROLE_HELPER"),
	ROLE_AGENT("ROLE_AGENT"),
	ROLE_TRAININGSCHOOL("ROLE_TRAININGSCHOOL"),
	ROLE_EMPLOYER("ROLE_EMPLOYER"),
	ROLE_GUEST("ROLE_GUEST");

    private String authority;

    SysRole(String authority)
    {
        this.authority = authority;
    }

    @Override
    public String getAuthority()
    {
        return this.authority;
    }
}
