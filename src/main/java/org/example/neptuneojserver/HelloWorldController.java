package org.example.neptuneojserver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Date;

@RestController
@RequestMapping("/")
public class HelloWorldController {

    public static void main(String[] args) {
        Date date = new Date();
        System.out.println(date);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        System.out.println(zonedDateTime);
    }

}
