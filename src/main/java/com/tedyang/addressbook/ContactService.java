/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tedyang.addressbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author tedyang
 */
public class ContactService {
    
    private static final AtomicInteger COUNT = new AtomicInteger(0);
    private final HashMap<String, User> users;
    
    public ContactService() {
        users = new HashMap();
    }
    
    
    public User getUserByName(String name) { 
        return users.get(name);
    }
    
    public User addUser(String name, String phoneNumber, String address) {
        long currentId = this.COUNT.getAndIncrement();
        User newUser = new User(currentId, name, phoneNumber, address);
        this.users.put(newUser.getName(), newUser);
        return newUser;
    }
    
    public void deleteUser(String name) {
        this.users.remove(name);
    }
    
    public User updateUser(String name, String phoneNumber, String address) {
        User foundUser = this.users.get(name);
        if (foundUser != null) {
            if (name != null) {
                foundUser.setName(name);
            }
            else {
                //TODO: Throw name not set Exception
            }
            if (address != null) {
                foundUser.setAddress(address);
            }
            else {
                //TODO: Address not set Exception
            }
            foundUser.setPhoneNumber(phoneNumber);
            this.users.put(name, foundUser);
        }
        else {
            //userId not found:
            //TODO: throw an exception
        }
        return foundUser;
    }
    
    public ArrayList<User> findAll() {
        return new ArrayList<>(users.values());
    }
    
    public HashMap<String, User> getUsers() {
        return this.users;
    }
    
}
