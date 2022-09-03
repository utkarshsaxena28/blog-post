package com.blog.service;

import com.blog.BlogPostApplication;
import com.blog.entity.Blog;
import com.blog.entity.User;
import com.blog.http.Response;
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

    // Getting list of all blogs written by particular user
    public List<Blog> listAllByUserId(int userId) {
        List<Blog> list=  blogRepo.findByUserId(userId);
        return list;
    }

    // Getting list of all blogs Containing Keyword
    public List<Blog> listAllByKeyword(String word) {
        List<Blog> list=  blogRepo.findByContentContaining(word);
        return list;
    }


    // get single blog by id
    public Blog getBlogById(int id) {
        Blog bg = null;
        try {
            bg=blogRepo.findById(id);
        } catch (Exception e) {
            logger.error("Ops....!!!!!", e);
        }
        return bg;
    }

    // Adding the Blog or Posting the Blog
    public Blog addBlog(Blog usr) {
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Update the Blog
    public Blog updateBlog(Blog b, int Bid) {
        b.setBlogId(Bid);
        Blog result = blogRepo.save(b);
        return result;
    }

    // Partially Update the Blog
    public Blog partiallyUpdateBlog(Blog usr, int Bid) {
        usr.setBlogId(Bid);
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Deleting the Blog
    public Response delete(int blogId, int userId) {

        Blog bg=blogRepo.findById(blogId);
        Response resp = new Response();
        resp.setBlog(bg);

        boolean role = userRepo.findById(userId).isAdmin();
        int adminId = userRepo.findById(userId).getUserId();
        String name = userRepo.findById(userId).getName();
        int userId1 = blogRepo.findById(blogId).getUserId();

        if (role == true) {
            blogRepo.deleteById(blogId);
            kafkaProducer.sendMessage(String.format("Administrator having id equal to %s has deleted the blog having blog_id = %s", adminId, blogId));
            resp.setMessage(String.format("Administrator having id equal to %s has deleted the blog having blog_id = %s", adminId, blogId));
            return resp;
        }
        else if (userId == userId1){
            blogRepo.deleteById(blogId);
            kafkaProducer.sendMessage(String.format("USER having id equal to %s has deleted his blog having blog_id = %s", userId, blogId));
            resp.setMessage(String.format("USER having id equal to %s has deleted his blog having blog_id = %s", userId, blogId));
            return resp;
        }
        else{
            resp.setMessage("you are not authorized to delete this blog...........................");
            return resp;
        }

    }
}
