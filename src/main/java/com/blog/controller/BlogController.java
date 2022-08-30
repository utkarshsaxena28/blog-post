package com.blog.controller;

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
@RequestMapping("/api/blog")
public class BlogController {

    static Logger logger = LogManager.getLogger(BlogPostApplication.class);
    
    @Autowired
	private KafkaProducer kafkaProducer;

    @Autowired
    private BlogService blgService;

    @GetMapping("/blogs")
    public ResponseEntity<Response> list(@RequestBody Request req) {

        Response resp = new Response();
        int userId = req.getUserId();

        if (userId == 0) {
            logger.info("************** Getting the list of all blogs ********************");
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
            logger.info("************ Getting the list of all blogs by particular user ***********************");
            List<Blog> list1 = blgService.listAllByUserId(userId);
            resp.setList(list1);
            logger.info(list1);
            if (list1.size()<=0) {
                resp.setMessage(String.format("Sorry user having id = %s has not written any blog", userId));
                return  new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
            }
            resp.setMessage(String.format("here is the list of all blogs written by user having id = %s ", userId));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        }
    }

    @GetMapping("/blogs/{blogId}")
    public ResponseEntity<Response> getEmployee(@PathVariable("blogId") int blogId) {

        logger.info("getting Blog by id number {}..............", blogId);

        Response resp = new Response();
        Blog blog = blgService.getBlogById(blogId);
        resp.setBlog(blog);
        if (blog == null) {
            resp.setMessage(String.format("blog having id = %s is not present", blogId));
            return  new ResponseEntity<>(resp,HttpStatus.NOT_FOUND);
        }
        resp.setMessage(String.format("here is the blog having id = %s", blogId));
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @PostMapping("{userId}/blogs") //  /api/blog/addblog/5
    public ResponseEntity<Response> add(@Valid @RequestBody Request req, @PathVariable("userId") int userId) {

        Blog b;
        Response resp = new Response();
        System.out.println("**********************" + resp + "**********************");
        Blog bg = req.getBlog();

        try {
            b = blgService.addBlog(bg);
            resp.setBlog(b);
            int id = bg.getBlogId();
            logger.info("Adding New Blog having id equale to {}..........", id);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has added a new blog having blog_id = %s", userId, id));
            resp.setMessage(String.format("Your blog having id = %s is successfully added", id));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("{userId}/blogs/{blogId}")
    public ResponseEntity<Response> updateBlog(@Valid @RequestBody Request req, @PathVariable("blogId") int blogId,@PathVariable("userId") int userId) {
        Response resp = new Response();
        Blog bog = req.getBlog();
        try {
            Blog bg = blgService.updateBlog(bog,blogId);
            resp.setBlog(bg);
            logger.info("Updating Blog having id equale to {}............", blogId);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has updated his profile", userId));
            resp.setMessage(String.format("Your profile is successfully updated"));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("{userId}/blogs/{blogId}")
    public ResponseEntity<Response> updatePartially(@Valid @RequestBody Request req, @PathVariable("blogId") int blogId, @PathVariable("userId") int userId) {
        Response resp = new Response();
        Blog b = req.getBlog();
        try {
            Blog bl = blgService.partiallyUpdateBlog(b,blogId);
            resp.setBlog(bl);
            logger.info("Updating Blog having id equale to {}............", blogId);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has Updated his blog having blog_id = %s", userId, blogId));
            resp.setMessage(String.format("Your blog having id = %s is successfully updated", blogId));
            return new ResponseEntity<>(resp,HttpStatus.OK);
        } catch(Exception e) {
            logger.error("Ops!", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{userId}/blogs/{blogId}")
    public ResponseEntity<String> delete(@PathVariable("blogId") int blogId, @PathVariable("userId") int userId) {

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
