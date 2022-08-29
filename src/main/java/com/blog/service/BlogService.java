package com.blog.service;

import com.blog.BlogPostApplication;
import com.blog.entity.Blog;
import com.blog.entity.User;
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

    //private Blog bell;

    @Autowired
    private UserRepositiory userRepo;

    // Getting list of all Blogs present in database
    public List<Blog> listAll() {
        List<Blog> list=  (List<Blog>) blogRepo.findAll();
        return list;
    }

    // get Blog by id
    /*
    public Blog getBlogById(int id) {
        Blog result = blogRepo.getReferenceById(id);
        return result;

    }*/

    // get single blog by id
    public Blog getBlogById(int id) {
        Blog bg = null;
        try {
            // book = list.stream().filter(e -> e.getId() == id).findFirst().get();
            bg=blogRepo.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bg;
    }

    // Adding the Blog or Posting the Blog
    public Blog addBlog(Blog usr) { // int userid
        Blog result = blogRepo.save(usr);
        return result;
    }
    /*
        User user=userRepo.findById(userid);
        bell.assignUser(user);*/

    // Update the Blog
    public Blog updateBlog(Blog usr, int Eid) {
        usr.setBlog_id(Eid);
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Partially Update the Blog
    public Blog partiallyUpdateBlog(Blog usr, int Eid) {
        usr.setBlog_id(Eid);
        Blog result = blogRepo.save(usr);
        return result;
    }

    // Deleting the Blog
    public void delete(int blogid, int userid) {

        boolean role = userRepo.findById(userid).isAdmin();
        String name = userRepo.findById(userid).getName();
        int FKey = blogRepo.findById(blogid).getUser_id();

        if (role == true) {
            blogRepo.deleteById(blogid);
            System.out.println("***************************************************************************************************************************");
            logger.info("Administerator has deleted your blog..............................");
            System.out.println("***************************************************************************************************************************");

        }
        else if (userid==FKey){
            blogRepo.deleteById(blogid);
            System.out.println("***************************************************************************************************************************");
            logger.info("{} your blog is deleted..................................", name);
            System.out.println("***************************************************************************************************************************");

        }
        else{
            System.out.println("***************************************************************************************************************************");
            logger.info("you are not authorized to delete this blog...........................");
            System.out.println("***************************************************************************************************************************");
        }
    }
}
