/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedyang.addressbook;

/**
 *
 * @author tedyang
 */
public class User {
    private String name;
    private String phoneNumber;
    private String address;
    private long id;
    
    public User(long id, String name, String phoneNumber, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
    
    
    
    /** PUBLIC GETTERS AND SETTERS FOR PRIVATE FIELDS OF USER
     */
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String newName) {
        this.name = newName;
    }
    
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    public void setPhoneNumber(String newNumber) {
        this.phoneNumber = newNumber;
    }
    
    public String getAddress() {
        return this.address;
    }
    
    public void setAddress(String newAddress) {
        this.address = newAddress;
    }
    
    public long getId() {
        return this.id;
    }
    
    public void setId(long newId) {
        this.id = newId;
    }
    
    public String toString() {
        return "Name: " + this.name;
    }
}
