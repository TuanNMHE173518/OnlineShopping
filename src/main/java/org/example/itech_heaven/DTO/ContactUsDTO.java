package org.example.itech_heaven.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactUsDTO {

    private String name;
    private String email;
    private String phone;
    private String message;
    private String subject;


}
