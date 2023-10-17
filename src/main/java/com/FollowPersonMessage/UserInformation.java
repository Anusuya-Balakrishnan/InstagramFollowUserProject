package com.FollowPersonMessage;

public class UserInformation {
	String username=null;
	String password = null;
	String sendMessage=null;
	
	public UserInformation(String username,String password,String sendMessage) {
		this.username=username;
		this.password=password;
		this.sendMessage=sendMessage;
	}
	public String toString() {
		String result=username.concat(" ").concat(password).concat(" ").concat(sendMessage);
		return result;
	}
}
