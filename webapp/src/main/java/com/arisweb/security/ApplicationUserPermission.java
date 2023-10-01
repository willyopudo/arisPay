package com.arisweb.security;

public enum ApplicationUserPermission {
	USER_READ("user:read"),
	USER_WRITE("user:write"),
	COMPNAYCUSTOMER_READ("companyCustomer:read"),
	COMPNAYCUSTOMER_WRITE("companyCustomer:write");

	private final String permission;

	ApplicationUserPermission(String permission) {
		this.permission = permission;
	}

	public String getPermission() {
		return permission;
	}
}
