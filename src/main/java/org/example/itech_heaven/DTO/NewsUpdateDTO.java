package org.example.itech_heaven.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewsUpdateDTO {

    private int id;
    private String title;
    private String image;
    private String content;
    private LocalDateTime date;
}
