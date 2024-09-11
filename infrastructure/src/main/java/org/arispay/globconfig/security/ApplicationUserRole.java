package org.arispay.globconfig.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static org.arispay.globconfig.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
	ADMIN(Sets.newHashSet(USER_READ, USER_WRITE, COMPNAYCUSTOMER_READ, COMPNAYCUSTOMER_WRITE)),
	COMPANYADMIN(Sets.newHashSet(USER_READ, USER_WRITE, COMPNAYCUSTOMER_READ, COMPNAYCUSTOMER_WRITE)),
	COMPANYUSER(Sets.newHashSet(COMPNAYCUSTOMER_READ, COMPNAYCUSTOMER_WRITE)),
	USER(Sets.newHashSet());

	private final Set<ApplicationUserPermission> permissions;

	ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<ApplicationUserPermission> getPermissions() {
		return permissions;
	}
}
