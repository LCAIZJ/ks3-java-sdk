package com.ksyun.ks3.dto;
/**
 * @author lijunwei[13810414122@163.com]  
 * 
 * @date 2014年10月24日 下午1:18:02
 * 
 * @description 
 **/
public class GranteeId implements Grantee{
	private String id;
	private String displayName;
	public String getTypeIdentifier() {
		return "id";
	}

	public void setIdentifier(String id) {
		this.id = id;
	}

	public String getIdentifier() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
    @Override
    public String toString()
    {
    	return "GranteeId[id="+this.id+";displayName="+this.displayName+"]";
    }
}