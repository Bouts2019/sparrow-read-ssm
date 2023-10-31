package com.martini.sparrow.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author martini at 2020/12/13 21:15
 */
// @Configuration
// @PropertySource("classpath:prop/swagger.properties")
// @EnableSwagger2
public class SwaggerConfig {
    @Value("${swagger.title}")
    private String title;
    @Value("${swagger.description}")
    private String description;
    @Value("${swagger.version}")
    private String version;
    @Value("${swagger.termsOfServiceUrl}")
    private String termsOfServiceUrl;
    @Value("${swagger.license}")
    private String license;
    @Value("${swagger.licenseUrl}")
    private String licenseUrl;

    @Bean
    public Docket creatRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                //选择哪些路径和api会生成document
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.martini.sparrow.controller"))
                //对所有路径进行监控
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .title(title)
                        .description(description)
                        .version(version)
                        .termsOfServiceUrl(termsOfServiceUrl)
                        .license(license)
                        .licenseUrl(licenseUrl)
                        .build());
    }
}
