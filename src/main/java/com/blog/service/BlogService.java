package com.blog.service;

import com.blog.entity.Blog;
import com.blog.entity.Blog;
import com.blog.repositiory.BlogRepositiory;
import com.blog.repositiory.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BlogService {

    @Autowired
    private BlogRepositiory blogRepo;

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
    public Blog addBlog(Blog usr) {
        Blog result = blogRepo.save(usr);
        return result;
    }

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
    public void delete(Integer id) {
        blogRepo.deleteById(id);
    }
}
