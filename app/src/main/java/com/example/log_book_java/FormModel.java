package com.example.log_book_java;

import java.io.Serializable;
@SuppressWarnings("serial")
public class FormModel implements Serializable {
    private int id;
    private String property;
    private String bed_rooms;
    private String create_at;
    private double rent;
    private String furniture;
    private String notes;
    private String name_reporter;
    private String reporter;
    private String name;
    private String address;

    public FormModel() {
        this.property = "";
        this.bed_rooms = "";
        this.create_at = "";
        this.rent = 0.0;
        this.furniture = "";
        this.notes = "";
        this.name_reporter = "";
        this.reporter = "";
        this.name="";
        this.address="";
    }

    public String get_propertyType() {
        return property;
    }

    public void set_propertyType(String _propertyType) {
        this.property = _propertyType;
    }

    public String get_bedType() {
        return bed_rooms;
    }

    public void set_bedType(String _bedType) {
        this.bed_rooms = _bedType;
    }

    public String get_createdDate() {
        return create_at;
    }

    public void set_createdDate(String _createdDate) {
        this.create_at = _createdDate;
    }

    public double get_rent() {
        return rent;
    }

    public void set_rent(double _rent) {
        this.rent = _rent;
    }

    public String get_furnitureType() {
        return furniture;
    }

    public void set_furnitureType(String _furnitureType) {
        this.furniture = _furnitureType;
    }

    public String get_noteText() {
        return notes;
    }

    public void set_noteText(String _noteText) {
        this.notes = _noteText;
    }

    public String get_reporter() {
        return name_reporter;
    }

    public void set_reporter(String _reporter) {
        this.name_reporter = _reporter;
    }

    @Override
    public String toString() {
        return this.property.toUpperCase() +" ($"+ this.rent+"/Month)" + " Bed room: " +this.bed_rooms.toUpperCase();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
