package org.example.itech_heaven.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;


@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private String image;
    private LocalDateTime date;

    @Column(columnDefinition = "nvarchar(255)")
    private String author;
}
