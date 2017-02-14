package com.temp.designPatterns.flyweight;

public class AuthorizationFlyweight implements Flyweight {
	
	private String securityEntity;
	
	private String permit;
	
	public AuthorizationFlyweight(String state) {
		String ss[] = state.split(",");
		this.securityEntity = ss[0];
		this.permit  =ss[1];
	}
	
	public String getSecurityEntity() {
		return this.securityEntity;
	}
	
	public String getPermit() {
		return this.permit;
	}

	@Override
	public boolean match(String securityEntity, String permit) {
		if(this.securityEntity.equals(securityEntity) && this.permit.equals(permit)) {
			return true;	
		}
		
		return false;
	}

}
