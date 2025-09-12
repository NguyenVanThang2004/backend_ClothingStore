package vn.ClothingStore.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

import lombok.Value;

@Configuration
public class CloundinaryConfiguration {

    @Bean
    public Cloudinary cloudinary() {

        final Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dbfecfacu");
        config.put("api_key", "614629122142599");
        config.put("api_secret", "7Ng8TtN9-TRQsZbD2tBl6t7Y2-Y");

        return new Cloudinary(config);

    }

}
