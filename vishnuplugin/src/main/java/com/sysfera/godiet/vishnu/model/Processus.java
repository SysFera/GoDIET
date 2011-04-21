package com.sysfera.godiet.vishnu.model;

import java.util.Date;

public class Processus {

	private String nomVishnu;
	private String status;
	//The last information update (status)
	private Date timestamp; 
	private PhysicalResource pluggedOn;
	
	//Pour lancer le sed
	private String scriptCommand;
	
	private String idDiet;
	public String getIdDiet() {
		return idDiet;
	}


	public void setIdDiet(String idDiet) {
		this.idDiet = idDiet;
	}


	//UMS, FMS, TMS, IMS ....
	public String getNomVishnu() {
		return nomVishnu;
	}


	public void setNomVishnu(String nomVishnu) {
		this.nomVishnu = nomVishnu;
	}


	//UP or DOWN DELETED
	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Date getTimestamp() {
		return timestamp;
	}


	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}


	public PhysicalResource getPluggedOn() {
		return pluggedOn;
	}


	public void setPluggedOn(PhysicalResource pluggedOn) {
		this.pluggedOn = pluggedOn;
	}



	
	
}