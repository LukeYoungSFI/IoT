package com.shakeel.model;

//import java.io.Serializable;
import javax.persistence.*;


@Entity
@Table(name="asset_status")
//public class AssetStatus implements Serializable{
public class AssetStatus{
	
	//private static final long serialVersionUID = 1L;
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name="bid")
	private String bid;    
	@Column(name="managed_by")
	private String managed_by;
	@Column(name="temperature")
	private double temperature;
	@Column(name="light")
	private double light;
	@Column(name="pressure")
	private double pressure;
	@Column(name="is_moving")
	private String is_moving;
	@Column(name="date_time")
	private String date_time;
	@Column(name="transaction_type")
	private String transaction_type; // pressure >=, light <= and motion is true.
	@Column(name="location_x")
	private double location_x;
	@Column(name="location_y")
	private double location_y;
	public long getID() {
		return id;
	}
	public void setID(long iD) {
		id = iD;
	}
	public double gettemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
	public double getlight() {
		return light;
	}
	public void setlight(double light) {
		this.light = light;
	}
	public double getpressure() {
		return pressure;
	}
	public void setpressure(double pressure) {
		this.pressure = pressure;
	}
	public String getmanaged_by() {
		return managed_by;
	}
	public void setmanaged_by(String managed_by) {
		this.managed_by = managed_by;
	}
	public String getdate_ime() {
		return date_time;
	}
	public void setdate_time(String date_time) {
		this.date_time = date_time;
	}
	public String gettransaction_type() {
		return transaction_type;
	}
	public void settransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public double getLocation_x() {
		return location_x;
	}
	public void setLocation_x(double location_x) {
		this.location_x = location_x;
	}
	public double getLocation_y() {
		return location_y;
	}
	public void setLocation_y(double location_y) {
		this.location_y = location_y;
	}
	public String getis_moving() {
		return is_moving;
	}
	public void setis_moving(String is_moving) {
		this.is_moving = is_moving;
	}
	public String getbid() {
		return bid;
	}
	public void setbid(String bid) {
		this.bid = bid;
	}
}
