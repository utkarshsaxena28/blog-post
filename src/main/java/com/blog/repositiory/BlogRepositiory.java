package com.blog.repositiory;

import com.blog.entity.Blog;
import com.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

public interface BlogRepositiory extends JpaRepository<Blog, Integer> {
    Blog findById(int id);
    List<Blog> findByUserId(int userid);

    List<Blog> findByContentContaining(String keyword);


}
