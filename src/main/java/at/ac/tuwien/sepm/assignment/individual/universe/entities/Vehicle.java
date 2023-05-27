package at.ac.tuwien.sepm.assignment.individual.universe.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Vehicle {

    private int id;
    private String title;
    private String description ="";
    private int buildyear;
    private String imageUrl ="";
    private int seatingCapacity = 1;
    private String licensePlate;
    private String powerUnit;
    private int power;
    private double price = 0.0;
    private Timestamp createDate;
    private Timestamp updatedate;
    private boolean isDelete;
    private ArrayList<LicenseCategory> licenseCategories;
    private String print;

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getisDelete(){
        return isDelete;
    }

    public int getSeatingCapacity() {
        return seatingCapacity;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public int getPower() {
        return power;
    }

    public String getPowerUnit() {
        return powerUnit;
    }

    public int getBuildyear() {
        return buildyear;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public Timestamp getUpdatedate() {
        return updatedate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSeatingCapacity(int seatingCapacity) {
        this.seatingCapacity = seatingCapacity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBuildyear(int buildyear) {
        this.buildyear = buildyear;
    }

    public void setUpdatedate(Timestamp updatedate) {
        this.updatedate = updatedate;
    }

    public void setPrice(double price) {
        this.price = Math.rint(price*100)/100;
    }

    public void setPowerUnit(String powerUnit) {
        this.powerUnit = powerUnit;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setisDelete(boolean delete) {
        isDelete = delete;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public void setIsdelete(Boolean isDelete){
        this.isDelete = isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public void setLicenseCategories(ArrayList<LicenseCategory> licenseCategories) {
        this.licenseCategories = licenseCategories;
    }

    public ArrayList<LicenseCategory> getLicenseCategories() {
        return licenseCategories;
    }


    public void printLicenseCategory(){

        print = "";

        if(licenseCategories.isEmpty()){print = " - ";}

        for (LicenseCategory i : licenseCategories) {

            if(i == LicenseCategory.A){
                print = "A ";
            }else if(i == LicenseCategory.B){
                print += "B ";
            }else if(i == LicenseCategory.C){
                print += "C ";
            }else{
                print = " - ";
            }
        }
    }

    @Override
    public String toString() {
        return "";
    }


}
