package com.blog.controller;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;


import javax.validation.Valid;

import com.blog.BlogPostApplication;
import com.blog.entity.Blog;
import com.blog.http.Request;
import com.blog.http.Response;
import com.blog.service.BlogService;
import com.blog.kafka.KafkaProducer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/api/blog")
public class BlogController {

    static Logger logger = LogManager.getLogger(BlogPostApplication.class);
    
    @Autowired
	private KafkaProducer kafkaProducer;

    @Autowired
    private BlogService blgService;

    @GetMapping("/blogs")                                                              // CHECKED
    public ResponseEntity<Response> list(@RequestParam(required = false) Integer userId) {

        Response resp = new Response();
        //int userId = req.getUserId();

        if (userId == null) {
            List<Blog> list = blgService.listAll();
            resp.setList(list);
            logger.info(list);
            if (list.size()<=0) {
                resp.setMessage("Sorry No List of blog is present in the database");
                return new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
            }
            resp.setMessage("here is the list of all blogs");
            return new ResponseEntity<>(resp,HttpStatus.OK);
        }

        else {
            List<Blog> list1 = blgService.listAllByUserId(userId);
            resp.setList(list1);
            logger.info(list1);
            if (list1.size() <= 0) {
                resp.setMessage(String.format("Sorry user having id = %s has not written any blog", userId));
                return  new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
            }
            resp.setMessage(String.format("here is the list of all blogs written by user having id = %s ", userId));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        }
    }

    @GetMapping("/blgs")                                                    // CHECKED
    public ResponseEntity<Response> getBlogsById(@RequestParam("blogId") Integer blogId) {

        logger.info("getting Blog by id number {}..............", blogId);
        List<Blog> list2 = new ArrayList<Blog>();
        Response resp = new Response();
        list2.add(blgService.getBlogById(blogId));
        resp.setList(list2);
        if (list2 == null) {
            resp.setMessage(String.format("blog having id = %s is not present", blogId));
            return  new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
        }
        resp.setMessage(String.format("here is the blog having id = %s", blogId));
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @GetMapping("/blogs/search")                                      // CHECKED
    public ResponseEntity<Response> getBlogsByWords(@RequestParam("keyword") String keyword) {

        logger.info("getting Blog which contains the word {}..............", keyword);

        Response resp = new Response();
        List<Blog> list3 = blgService.listAllByKeyword(keyword);
        resp.setList(list3);
        logger.info(list3);
        if (list3.isEmpty()) {
            resp.setMessage(String.format("Sorry Blog having Keyword = %s does not exist ", keyword));
            return  new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
        }
        resp.setMessage(String.format("here is the list of all blogs having Keyword = %s ", keyword));
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping("/blogs")                                           // CHECKED
    public ResponseEntity<Response> addBlog(@Valid @RequestBody Request req ) {

        List<Blog> list4 = new ArrayList<Blog>();
        Response resp = new Response();
        Blog bg = req.getBlog();

        try {
            list4.add(blgService.addBlog(bg));
            resp.setList(list4);
            int id = bg.getBlogId();
            int userId = bg.getUserId();
            logger.info("Adding New Blog having id equale to {}..........", id);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has added a new blog having blog_id = %s", userId, id));
            resp.setMessage(String.format("Your blog having id = %s is successfully added", id));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/blogs")                                       // CHECKED
    public ResponseEntity<Response> updateBlog(@Valid @RequestBody Request req ) {
        Response resp = new Response();
        Blog bog = req.getBlog();
        int blogId = bog.getBlogId();
        int userId = bog.getUserId();
        try {
            List<Blog> list5 = new ArrayList<Blog>();
            list5.add(blgService.updateBlog(bog,blogId));
            resp.setList(list5);
            logger.info("Updating Blog having id equale to {}............", blogId);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has updated his profile", userId));
            resp.setMessage("Your profile is successfully updated");
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/blogs")                                     // CHECKED
    public ResponseEntity<Response> updatePartially(@Valid @RequestBody Request req) {
        Response resp = new Response();
        Blog bg = req.getBlog();
        int blogId = bg.getBlogId();
        int userId = bg.getUserId();
        try {
            List<Blog> list6 = new ArrayList<Blog>();
            list6.add(blgService.partiallyUpdateBlog(bg,blogId));
            resp.setList(list6);
            logger.info("Updating Blog having id equale to {}............", blogId);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has Updated his blog having blog_id = %s", userId, blogId));
            resp.setMessage(String.format("Your blog having id = %s is successfully updated", blogId));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/blogs")                                                       // CHECKED
    public ResponseEntity<String> deleteBlog(@RequestParam("blogId") Integer blogId, @RequestParam("userId") Integer userId) {

        Response resp = blgService.delete(blogId, userId);
        try {
            logger.info("Blog having id equal to {} is deleted.............", blogId);
            return new ResponseEntity<>(resp.getMessage(),HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
