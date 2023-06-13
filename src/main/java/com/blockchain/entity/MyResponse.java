package com.blockchain.entity;

public class MyResponse {
	public MyResponse(String result, String json) {
		this.result = result;
		this.json = json;
	}

	private String result;
	private String json;

	public String getResult() {
		return this.result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getJson() {
		return this.json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
