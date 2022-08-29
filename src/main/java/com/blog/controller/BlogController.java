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
    public ResponseEntity<List<Blog>> list() {

        logger.info("Getting the list of all blogs");

        List<Blog> list = blgService.listAll();
        System.out.println(list);
        if (list.size()<=0) {
            return  ResponseEntity.status( HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.of(Optional.of(list));
    }

    /*
    @GetMapping("/blog/{id}")
    public Blog getBlog(@PathVariable("id") int id) {
        logger.info("getting Blog By Id...........");
        //return "Blog data "+id;
        return blgService.getBlogById(id);
    }*/

	@GetMapping("/Blogid/{id}")
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
            kafkaProducer.sendMessage(String.format("USER having id equale to %s has added a new blog having blog_id = %s", userid, id));
            return ResponseEntity.of(Optional.of(b));
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/updateblog/{id}")
    public ResponseEntity<Blog> updateBlog(@Valid @RequestBody Blog bog, @PathVariable("id") int id) {

        try {
            Blog bg = blgService.updateBlog(bog,id);
            logger.info("Updating Blog having id equale to {}............", id);
            return ResponseEntity.ok().body(bg);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/patchblog/{id}")
    public ResponseEntity<Blog> updatePartially(@Valid @RequestBody Blog b, @PathVariable("id") int id) {
        try {
            Blog bl = blgService.partiallyUpdateBlog(b,id);
            logger.info("Updating Blog having id equale to {}............", id);
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
