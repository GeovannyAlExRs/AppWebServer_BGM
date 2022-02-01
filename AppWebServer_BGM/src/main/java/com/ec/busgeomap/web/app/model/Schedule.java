package com.ec.busgeomap.web.app.model;

import java.util.Date;

import javax.validation.constraints.Future;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class Schedule {
	
	private String sch_id;
	
	private String sch_asb_id_number_disc;

	private String sch_rou_id_name;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private long sch_registration_date;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private long sch_departure_time = new Date().getTime();

	private String sch_state;

	private boolean sch_status;

	public Schedule() {}

	public Schedule(String sch_id) {
		this.sch_id = sch_id;
	}

	public Schedule(String sch_id, String sch_asb_id_number_disc, String sch_rou_id_name, long sch_registration_date, 
			long sch_departure_time, String sch_state, boolean sch_status) {
		this.sch_id = sch_id;
		this.sch_asb_id_number_disc = sch_asb_id_number_disc;
		this.sch_rou_id_name = sch_rou_id_name;
		this.sch_registration_date = sch_registration_date;
		this.sch_departure_time = sch_departure_time;
		this.sch_state = sch_state;
		this.sch_status = sch_status;
	}

	public String getSch_id() {
		return sch_id;
	}

	public void setSch_id(String sch_id) {
		this.sch_id = sch_id;
	}

	public String getSch_asb_id_number_disc() {
		return sch_asb_id_number_disc;
	}

	public void setSch_asb_id_number_disc(String sch_asb_id_number_disc) {
		this.sch_asb_id_number_disc = sch_asb_id_number_disc;
	}

	public String getSch_rou_id_name() {
		return sch_rou_id_name;
	}

	public void setSch_rou_id_name(String sch_rou_id_name) {
		this.sch_rou_id_name = sch_rou_id_name;
	}

	public long getSch_registration_date() {
		return sch_registration_date;
	}

	public void setSch_registration_date(long sch_registration_date) {
		this.sch_registration_date = sch_registration_date;
	}

	public long getSch_departure_time() {
		return sch_departure_time;
	}

	public void setSch_departure_time(long sch_departure_time) {
		this.sch_departure_time = sch_departure_time;
	}

	public String getSch_state() {
		return sch_state;
	}

	public void setSch_state(String sch_state) {
		this.sch_state = sch_state;
	}

	public boolean getSch_status() {
		return sch_status;
	}

	public void setSch_status(boolean sch_status) {
		this.sch_status = sch_status;
	}

	@Override
	public String toString() {
		return "Schedule [sch_id=" + sch_id + ", sch_asb_id_number_disc=" + sch_asb_id_number_disc
				+ ", sch_rou_id_name=" + sch_rou_id_name + ", sch_registration_date=" + sch_registration_date
				+ ", sch_departure_time=" + sch_departure_time + ", sch_state=" + sch_state + ", sch_status="
				+ sch_status + "]";
	}
	
}
