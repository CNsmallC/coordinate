package com.github.coordinate.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * 描述 : addResourceHandlers
     *
     * @return WebMvcConfigurerAdapter
     */
    @Bean
    public WebMvcConfigurerAdapter addResourceHandlers() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("swagger-ui.html")
                        .addResourceLocations("classpath:/META-INF/resources/");
                registry.addResourceHandler("/webjars/**")
                        .addResourceLocations("classpath:/META-INF/resources/webjars/");
            }
        };
    }

    /**
     * 描述 : createRestApi
     *
     * @return createRestApi
     */
    @Bean
    public Docket createRestApi() {
        Parameter parameter = new ParameterBuilder()
                .name("loginToken")
                .description("令牌")
                .modelRef(new ModelRef("string"))
                .parameterType("query").required(false).build();
        List<Parameter> pars = new ArrayList<>();
        pars.add(parameter);
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("wfx.monitor")).paths(PathSelectors.any())
                .build().globalOperationParameters(pars);
    }

    /**
     * 描述 : apiInfo
     *
     * @return apiInfo
     */
    private ApiInfo apiInfo() {
        ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
        apiInfoBuilder.title("coordinate" + " online api document");
        apiInfoBuilder.version("1.0");
        return apiInfoBuilder.build();
    }
}
