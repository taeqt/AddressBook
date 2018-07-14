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
public class Contact {
    private User user;
    
    
    public void setUser(User newUser) {
        this.user = newUser;
    }
    
    public User getUser() {
        return this.user;
    }
    
}
