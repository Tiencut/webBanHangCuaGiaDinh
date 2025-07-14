package com.giadinh.banphutung.web_ban_hang_gia_dinh.config;

// SwaggerConfig tạm thời bị comment out do lỗi version compatibility
/*
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Web Bán Hàng Gia Đình API")
                        .description("API hệ thống quản lý bán hàng đa nhà cung cấp với tính năng thay thế sản phẩm, quản lý xe khách hàng, và quản lý kho")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Gia Đình Ban Phụ Tùng")
                                .email("contact@giadinhbanphutung.com")
                                .url("https://giadinhbanphutung.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.giadinhbanphutung.com")
                                .description("Production Server")
                ));
    }
}
*/ 