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

    @Autowired
    private BlogService blgService;

    @GetMapping("/blogs")
    public ResponseEntity<List<Blog>> list(@RequestBody Blog bg) {

        int userid =  bg.getUserId();

        if (userid == 0) {
            logger.info("************ Getting the list of all blogs ***********************");
            List<Blog> list = blgService.listAll();
            logger.info(list);
            if (list.size()<=0) {
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.of(Optional.of(list));
        }
        else {
            logger.info("************ Getting the list of all blogs by particular user ***********************");
            List<Blog> list1 = blgService.listAllByUserId(userid);
            System.out.println(list1);
            if (list1.size()<=0) {
                return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.of(Optional.of(list1));
        }
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity<Blog> getEmployee(@PathVariable("id") int id) {

        logger.info("getting Blog by id number {}..............", id);

        Blog blog = blgService.getBlogById(id);

        if (blog == null) {
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(blog));
    }

    @PostMapping("{userid}/blogs") //  /api/blog/addblog/5
    public ResponseEntity<Blog> add(@Valid @RequestBody Blog bg, @PathVariable("userid") int userid) {
        Blog b;
        try {
            b = blgService.addBlog(bg);
            int id = bg.getBlogId();
            logger.info("Adding New Blog having id equale to {}..........", id);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has added a new blog having blog_id = %s", userid, id));
            return ResponseEntity.of(Optional.of(b));
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("{userid}/blogs/{blogid}")
    public ResponseEntity<Blog> updateBlog(@Valid @RequestBody Blog bog, @PathVariable("blogid") int blogid,@PathVariable("userid") int userid) {

        try {
            Blog bg = blgService.updateBlog(bog,blogid);
            logger.info("Updating Blog having id equale to {}............", blogid);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has updated his profile", userid));
            return ResponseEntity.ok().body(bg);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("{userid}/blogs/{blogid}")
    public ResponseEntity<Blog> updatePartially(@Valid @RequestBody Blog b, @PathVariable("blogid") int blogid, @PathVariable("userid") int userid) {
        try {
            Blog bl = blgService.partiallyUpdateBlog(b,blogid);
            logger.info("Updating Blog having id equale to {}............", blogid);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has Updated his blog having blog_id = %s", userid, blogid));
            return ResponseEntity.of(Optional.of(bl));
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{userid}/blogs/{blogid}")
    public ResponseEntity<?> delete(@PathVariable("blogid") int blogid, @PathVariable("userid") int userid) {
        try {
            blgService.delete(blogid, userid);
            logger.info("Blog having id equal to {} is deleted.............", blogid);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
