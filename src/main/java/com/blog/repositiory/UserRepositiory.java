package com.blog.repositiory;

import com.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public interface UserRepositiory extends JpaRepository<User, Integer> {
    
	public User findById(int id);
    
	public User findByName(String name);
}
