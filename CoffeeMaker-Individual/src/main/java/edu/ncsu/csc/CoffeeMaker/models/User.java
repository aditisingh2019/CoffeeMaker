package edu.ncsu.csc.CoffeeMaker.models;

public class User {

	public Long id;
	public String userName;
	public String passwordHash;

	// Constructor
	public User(long id, String userName, String passwordHash) {
		this.id = id;
		this.userName = userName;
		this.passwordHash = passwordHash;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the passwordHash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * @param passwordHash the passwordHash to set
	 */
	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	// dummy
	public boolean authenticate(String userName, String password) {
		return false;
	}

	public void resetPassword(String newPassword) {
		this.passwordHash = newPassword;
	}

	public void updateProfile(String userName, String newPassword) {
		this.userName = userName;
		this.passwordHash = newPassword;
	}

}
