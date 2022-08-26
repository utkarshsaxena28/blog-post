package com.blog.controller;

import com.blog.BlogPostApplication;
import com.blog.entity.User;
import com.blog.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    static Logger logger = LogManager.getLogger(BlogPostApplication.class);

    // @Autowired
    // private KafkaProducer kafkaProducer;

    @Autowired
    private UserService usrService;

    @GetMapping("/getUser")
    public ResponseEntity<List<User>> list() {

        logger.info("Getting the list of all Users");

        List<User> list = usrService.listAll();

        if (list.size()<=0) {
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(list));
    }

    /*
    @GetMapping("/employee/{id}")
    public User getUser(@PathVariable("id") int id) {
        logger.info("getting User By Id...........");
        //return "User data "+id;
        return usrService.getUserById(id);
    }*/

    @GetMapping("/userid/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") int id) {
        try {
            logger.info("getting employee by id number {}..............", id);
            //return "User data "+id;

            User ur = usrService.getUserById(id);
            //System.out.println("*************************************************************************");
            //System.out.println(ur);
            //System.out.println("*************************************************************************");
            return ResponseEntity.of(Optional.of(ur));
        }catch (Exception e) {
            e.printStackTrace();
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
        }

    }

    @PostMapping("/adduser")
    public ResponseEntity<User> add(@Valid @RequestBody User uzr) {
        User ue = null;
        try {
            ue = usrService.addUser(uzr);
            int id = uzr.getUser_id();
            logger.info("Adding New User having id equale to {}..........", id);
            //kafkaProducer.sendMessage(String.format("NEW USER IS ADDED having id equale to %s ", id));   KAFKA line
            return ResponseEntity.of(Optional.of(ue));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateuser/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody User us, @PathVariable("id") int id) {

        try {
            User ue = usrService.updateUser(us,id);
            logger.info("Updating User having id equale to {}............", id);
            return ResponseEntity.ok().body(ue);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/patchuser/{id}")
    public ResponseEntity<User> updatePartially(@Valid @RequestBody User ue, @PathVariable("id") int id) {
        try {
            User uer = usrService.partiallyUpdateUser(ue,id);
            logger.info("Updating User having id equale to {}............", id);
            return ResponseEntity.of(Optional.of(uer));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteuser/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) {
        try {
            usrService.delete(id);
            logger.info("User having id equal to {} is deleted.............", id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
