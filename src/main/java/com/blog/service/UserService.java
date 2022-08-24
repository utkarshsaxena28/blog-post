package com.blog.service;

import com.blog.entity.User;
import com.blog.repositiory.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepositiory userRepo;

    // Getting list of all users present in database
    public List<User> listAll() {
        List<User> list=  (List<User>) userRepo.findAll();
        return list;
    }

    // get User by id
    public User getUserById(int id) {
        User result = userRepo.getReferenceById(id);
        return result;

    }

    // Adding the user or Posting the User
    public User addUser(User usr) {
        User result = userRepo.save(usr);
        return result;
    }

    // Update the User
    public User updateUser(User usr, int Eid) {
        usr.setUser_id(Eid);
        User result = userRepo.save(usr);
        return result;
    }

    // Partially Update the User
    public User partiallyUpdateUser(User usr, int Eid) {
        usr.setUser_id(Eid);
        User result = userRepo.save(usr);
        return result;
    }

    // Deleting the User
    public void delete(Integer id) {
        userRepo.deleteById(id);
    }
}
