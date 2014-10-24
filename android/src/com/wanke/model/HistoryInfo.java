package com.wanke.model;

public class HistoryInfo {

	/**
	 * 
	 * @author Administrator
	 * 
	 */

	private String number;
	private String name;
	private String path;

	public HistoryInfo(String number, String name, String path) {
		this.number = number;
		this.name = name;
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "BlackNumberInfo [number=" + number + ", path=" + path
				+ ", name=" + name + "]";
	}

}