package com.blockchain.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@javax.persistence.Entity
public class SysTokenAccess implements Entity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name = "loginTimeId")
	private Long id;

	@Column(nullable = false)
	private String accessCode;

	@Column
	private Date date;

	@ManyToOne
	private User user;

	@Column
	private Date dueDate;

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	protected SysTokenAccess() {
		/* Reflection instantiation */
	}

	public SysTokenAccess(User user, String accessCode) {
		this.user = user;
		this.accessCode = accessCode;
	}

	public SysTokenAccess(String accessCode) {
		this.accessCode = accessCode;
	}

	public SysTokenAccess(User user, String accessCode, Date expiry) {
		this(user, accessCode);
		this.dueDate = expiry;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	public String getToken() {
		return this.accessCode;
	}

	public User getUser() {
		return this.user;
	}

	public Date getExpiry() {
		return this.dueDate;
	}

	public boolean isExpired() {
		if (null == this.dueDate) {
			return false;
		}

		return this.dueDate.getTime() > System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return "{\\\"id\\\":\\\"" + id + "\\\", \\\"accessCode\\\":\\\"" + accessCode + "\\\", \\\"date\\\":\\\"" + date
				+ "\\\", \\\"userId\\\":\\\"" + (null != user ? user.getId() : null) + "\\\", \\\"dueDate\\\":\\\""
				+ dueDate + "\\\"}";
	}

}
