package com.divyansh.bookrr.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String,String> map =  new HashMap();
        map.put("cloud_name", "divyansh258");
        map.put("api_key", "681233821512451");
        map.put("api_secret", "kIQYOFuH8yDPl1_VViH2PXC6r0A");
        return new Cloudinary(map);
    }
}
