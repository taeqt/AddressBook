
import com.tedyang.addressbook.ContactService;
import com.tedyang.addressbook.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author tedyang
 */
public class ContactServiceTest {
    ContactService service;
    
    public void setUp() {
        service = new ContactService();
    }
    
    public void testAdd() {
        User user = service.addUser("Ted", "1234567890", "1234 Hello World Lane");
        //assertNotNull(user);
        service.updateUser(user.getName(), "1234123412", "New Address");
        
        service.deleteUser(user.getName());
    }
    
}
