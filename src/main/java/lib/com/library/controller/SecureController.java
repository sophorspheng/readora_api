package lib.com.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/secure")
public class SecureController {

    @GetMapping("/request")
    public String getMethodName() {
        String a = "Request Successfully";
        System.out.println(a);
        return new String(a);
    }
    
    
}
