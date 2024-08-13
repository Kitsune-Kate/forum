package com.example.forum.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@ToString
@Entity //создает БД автоматически
public class Message {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //создали автоинкремент   @GeneratedValue
    private int id;
    private String title;
    private String textarea;
    @Enumerated(EnumType.STRING)
    private Category choice;
    //    @CreationTimestamp
//    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date date;
    @ManyToOne
    private User user;
    private String filename;


    public String getUserName() {
        return user.getUsername();
    }

    public String dateFormatter() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        return dateFormat.format(date);
    }
}
