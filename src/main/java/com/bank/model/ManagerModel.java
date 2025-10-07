package com.bank.model;

public class ManagerModel {
	private String managerId;
	private String managerName;
	private String managerEmail;
	private String managerPassword;
	private String branchId;

	    // Getters
	    public String getManagerId() {
	        return managerId;
	    }

	    public String getManagerName() {
	        return managerName;
	    }

	    public String getManagerEmail() {
	        return managerEmail;
	    }

	    public String getManagerPassword() {
	        return managerPassword;
	    }

	    public String getBranchId() {
	        return branchId;
	    }

	    // Setters
	    public void setManagerId(String managerId) {
	        this.managerId = managerId;
	    }

	    public void setManagerName(String managerName) {
	        this.managerName = managerName;
	    }

	    public void setManagerEmail(String managerEmail) {
	        this.managerEmail = managerEmail;
	    }

	    public void setManagerPassword(String managerPassword) {
	        this.managerPassword = managerPassword;
	    }

	    public void setBranchId(String branchId) {
	        this.branchId = branchId;
	    }
	


}
