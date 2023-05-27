package at.ac.tuwien.sepm.assignment.individual.universe.entities;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Booking {

    private int bid = 0;
    private String cname;
    private Status status;
    private String iban;
    private String cnumber;
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp createDate;
    private Double price = 0.0;
    private Double gesamtprice;
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Integer> vehiclesid;
    private ArrayList<Driver> drivers;
    private HashMap<Integer,Driver> BookedVehiclewithDriver = new HashMap<Integer, Driver>(); //first Vehicle second Driver


    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setPrice(Double price) {
        this.price = Math.rint(price*100)/100;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Double getGesamtprice() {
        return gesamtprice;
    }

    public Double getPrice() {
        return price;
    }

    public int getBid() {
        return bid;
    }

    public String getCnumber() {
        return cnumber;
    }

    public Status getStatus() {
        return status;
    }

    public String getCname() {
        return cname;
    }

    public String getIban() {
        return iban;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setGesamtprice(Double gesamtprice) {
        this.gesamtprice = gesamtprice;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public HashMap<Integer,Driver> getBookedVehiclewithDriver() {
        return BookedVehiclewithDriver;
    }

    public void setBookedVehiclewithDriver(int v, Driver d) {
        BookedVehiclewithDriver.put(v,d);
    }

    public void setVehicles(Vehicle vehicles) {

        this.vehicles.add(vehicles);
    }

    public void setVehicles(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public void setDrivers(ArrayList<Driver> drivers) {
        this.drivers = drivers;
    }

    public ArrayList<Driver> getDrivers() {
        return drivers;
    }

    public String getBookingper() {
        return createDate.toString()+"-"+endDate;
    }

    public ArrayList<Integer> getVehiclesid() {
        return vehiclesid;
    }

    public void setVehiclesid(ArrayList<Integer> vehiclesid) {
        this.vehiclesid = vehiclesid;
    }

    public LocalDate getDate(Timestamp timestamp) {

        String tmp = timestamp.toString();

        int year = Integer.parseInt(tmp.substring(0,4));
        int month = Integer.parseInt(tmp.substring(5,7));
        int day = Integer.parseInt(tmp.substring(8,9)) == 0 ? Integer.parseInt(tmp.substring(9,10)) : Integer.parseInt(tmp.substring(8,10));

        return LocalDate.of(year,month,day);
    }

    public String getHour(Timestamp timestamp){
        return timestamp.toString().substring(11,16);
    }

    public String toString(){

        String tostring = "Invoice Nr: " + getBid() + "                                Status: "+getStatus()+" \n\n\n"+
            "                              Booking Period \n\n"+"Start Date: "+getStartDate().toString()+"\nEnd Date:   "+getEndDate().toString()+"\n\n";

        tostring += "                              Booked Vehicle \n\n";



        for (Vehicle v: getVehicles()) {
            String p = String.valueOf(v.getPrice());
            tostring += v.getTitle() + ":  €"+ Math.rint(v.getPrice()*100)/100+"\n";
        }

        tostring += "\n\nTotal Price: €"+getPrice();

        return tostring;
    }


}
