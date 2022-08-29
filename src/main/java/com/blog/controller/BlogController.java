package com.blog.controller;

import java.util.List;

import java.util.Optional;


import javax.validation.Valid;

import com.blog.BlogPostApplication;
import com.blog.entity.Blog;
import com.blog.service.BlogService;
import com.blog.kafka.KafkaProducer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/blog")
public class BlogController {

    static Logger logger = LogManager.getLogger(BlogPostApplication.class);
    
    @Autowired
	private KafkaProducer kafkaProducer;

    //@Autowired
    //private KafkaProducer kafkaProducer;

    @Autowired
    private BlogService blgService;

    @GetMapping("/getblog")
    public ResponseEntity<List<Blog>> list(@RequestBody Blog bg) {

        logger.info("Getting the list of all blogs");

        int userid =  bg.getUserid();

        if (userid == 0) {
            List<Blog> list = blgService.listAll();
            System.out.println(list);
            if (list.size()<=0) {
                return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.of(Optional.of(list));
        }
        else {
            List<Blog> list1 = blgService.listAllByUserId(userid);
            System.out.println(list1);
            if (list1.size()<=0) {
                return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.of(Optional.of(list1));
        }
    }

    /*
    @GetMapping("/blog/{id}")
    public Blog getBlog(@PathVariable("id") int id) {
        logger.info("getting Blog By Id...........");
        //return "Blog data "+id;
        return blgService.getBlogById(id);
    }*/

    @GetMapping("/blogbyid/{id}")
    public ResponseEntity<Blog> getEmployee(@PathVariable("id") int id) {

        logger.info("getting Blog by id number {}..............", id);
        //return "Blog data "+id;

        Blog Blog = blgService.getBlogById(id);

        if (Blog==null) {
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(Blog));
    }

    @PostMapping("{userid}/addblog") //  /api/blog/addblog/5
    public ResponseEntity<Blog> add(@Valid @RequestBody Blog bg, @PathVariable("userid") int userid) {
        Blog b = null;
        try {
            b = blgService.addBlog(bg);
            int id = bg.getBlog_id();
            //logger.info("Adding New Blog having id equale to {}..........", id);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has added a new blog having blog_id = %s", userid, id));
            return ResponseEntity.of(Optional.of(b));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("{userid}/update/{blogid}")
    public ResponseEntity<Blog> updateBlog(@Valid @RequestBody Blog bog, @PathVariable("blogid") int blogid,@PathVariable("userid") int userid) {

        try {
            Blog bg = blgService.updateBlog(bog,blogid);
            logger.info("Updating Blog having id equale to {}............", blogid);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has updated his profile", userid));
            return ResponseEntity.ok().body(bg);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("{userid}/updateblog/{blogid}")
    public ResponseEntity<Blog> updatePartially(@Valid @RequestBody Blog b, @PathVariable("blogid") int blogid, @PathVariable("userid") int userid) {
        try {
            Blog bl = blgService.partiallyUpdateBlog(b,blogid);
            //logger.info("Updating Blog having id equale to {}............", id);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has Updated his blog having blog_id = %s", userid, blogid));
            return ResponseEntity.of(Optional.of(bl));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{userid}/deleteblog/{blogid}")
    public ResponseEntity<?> delete(@PathVariable("blogid") int blogid, @PathVariable("userid") int userid) {
        try {
            blgService.delete(blogid, userid);
            //logger.info("Blog having id equal to {} is deleted.............", blogid);
            //kafkaProducer.sendMessage(String.format("USER having id equal to %s has deleted his blog having blog_id = %s", userid, blogid));
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
