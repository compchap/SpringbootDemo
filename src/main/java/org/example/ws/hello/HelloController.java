package org.example.ws.hello;


import org.springframework.web.bind.annotation.*;

/**
 * Created by ng88763 on 2/8/2016.
 */

@RestController
@RequestMapping("demo")
public class HelloController {
    @RequestMapping("param")
    public String index(@RequestParam String name) {
        return "Greetings from Spring Boot! "+name;
    }
    @RequestMapping("path/{name}")
    public String indexPath(@PathVariable String name) {
        return "Greetings from Spring Boot! "+name;
    }
}
