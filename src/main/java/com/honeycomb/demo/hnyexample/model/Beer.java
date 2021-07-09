package com.honeycomb.demo.hnyexample.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Beer {

	private long id;
	private String uuid;
	private String brand;
	private String name;
	private String style;
	private String hop;
	private String yeast;
	private String malts;
	private String ibu;
	private String alcohol;
	private String blg;
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	@JsonProperty("uid")
	public String getUuid() {
		return uuid;
	}
	@JsonProperty("uid")
	public void setUuid(String uid) {
		this.uuid = uid;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getHop() {
		return hop;
	}
	public void setHop(String hop) {
		this.hop = hop;
	}
	public String getYeast() {
		return yeast;
	}
	public void setYeast(String yeast) {
		this.yeast = yeast;
	}
	public String getMalts() {
		return malts;
	}
	public void setMalts(String malts) {
		this.malts = malts;
	}
	public String getIbu() {
		return ibu;
	}
	public void setIbu(String ibu) {
		this.ibu = ibu;
	}
	public String getAlcohol() {
		return alcohol;
	}
	public void setAlcohol(String alcohol) {
		this.alcohol = alcohol;
	}
	public String getBlg() {
		return blg;
	}
	public void setBlg(String blg) {
		this.blg = blg;
	}
	
	
}
