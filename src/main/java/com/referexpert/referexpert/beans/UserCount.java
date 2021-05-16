package com.referexpert.referexpert.beans;

public class UserCount {

	private Integer total;

	private Integer active;
	
	private Integer pending;
	
	private Integer disabled;
	
	public UserCount() {
		
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getPending() {
		return pending;
	}

	public void setPending(Integer pending) {
		this.pending = pending;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	@Override
	public String toString() {
		return "UserCount [total=" + total + ", active=" + active + ", pending=" + pending + ", disabled=" + disabled
				+ "]";
	}

}
