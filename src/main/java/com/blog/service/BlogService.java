package com.blog.service;

import com.blog.BlogPostApplication;
import com.blog.entity.Blog;
import com.blog.entity.User;
import com.blog.kafka.KafkaProducer;
import com.blog.repositiory.BlogRepositiory;
import com.blog.repositiory.UserRepositiory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BlogService {

    static Logger logger = LogManager.getLogger(BlogPostApplication.class);
    @Autowired
    private BlogRepositiory blogRepo;

    @Autowired
    private KafkaProducer kafkaProducer;


    @Autowired
    private UserRepositiory userRepo;

    // Getting list of all Blogs present in database
    public List<Blog> listAll() {
        List<Blog> list=  (List<Blog>) blogRepo.findAll();
        return list;
    }

    public List<Blog> listAllByUserId(int userid) {
        List<Blog> list=  blogRepo.findByUserid(userid);
        return list;
    }


    // get single blog by id
    public Blog getBlogById(int id) {
        Blog bg = null;
        try {
            bg=blogRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bg;
    }

    // Adding the Blog or Posting the Blog
    public Blog addBlog(Blog usr) {
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Update the Blog
    public Blog updateBlog(Blog b, int Eid) {
        b.setBlogId(Eid);
        Blog result = blogRepo.save(b);
        return result;
    }

    // Partially Update the Blog
    public Blog partiallyUpdateBlog(Blog usr, int Eid) {
        usr.setBlogId(Eid);
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Deleting the Blog
    public void delete(int blogid, int userid) {

        boolean role = userRepo.findById(userid).isAdmin();
        int adminid = userRepo.findById(userid).getUserId();
        String name = userRepo.findById(userid).getName();
        int FKey = blogRepo.findById(blogid).getUserId();

        if (role == true) {
            blogRepo.deleteById(blogid);
            kafkaProducer.sendMessage(String.format("Administrator having id equal to %s has deleted the blog having blog_id = %s", adminid, blogid));
            logger.info("************************************************************************************************");
            logger.info("Administrator has deleted your blog..............................");
            logger.info("************************************************************************************************");

        }
        else if (userid==FKey){
            blogRepo.deleteById(blogid);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has deleted his blog having blog_id = %s", userid, blogid));
            logger.info("************************************************************************************************");
            logger.info("{} your blog is deleted..................................", name);
            logger.info("************************************************************************************************");

        }
        else{
            logger.info("************************************************************************************************");
            logger.info("you are not authorized to delete this blog...........................");
            logger.info("************************************************************************************************");
        }
    }
}
