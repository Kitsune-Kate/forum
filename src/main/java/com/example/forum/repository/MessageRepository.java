package com.example.forum.repository;

import com.example.forum.model.Category;
import com.example.forum.model.Message;
import com.example.forum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

//    @Query(value = "SELECT m FROM Message m")
//    public List<Message> fechAllFromCustom();

//    @Query(value = "SELECT * FROM  m where m.title =: val")
//    public List<Message> fetchAllByCategory(@Param("val") String title);
//
//    @Query(value = "SELECT * FROM messages", nativeQuery = true)
//    List<Message> findAll();

    public List<Message> findByChoice(Category category);

    public List<Message> findByUser(User user);
//    public List<Message> findMessagesByUser(String username);

}
