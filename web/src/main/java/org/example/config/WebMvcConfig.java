package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.example")
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SpringResourceTemplateResolver htmlResolver() {
        SpringResourceTemplateResolver htmlResolver = new SpringResourceTemplateResolver();

        htmlResolver.setApplicationContext(applicationContext);
        htmlResolver.setPrefix("/WEB-INF/view/");
        htmlResolver.setSuffix(".html");
        htmlResolver.setCharacterEncoding("UTF-8");

        return htmlResolver;
    }

    @Bean
    public SpringResourceTemplateResolver cssResolver() {
        SpringResourceTemplateResolver cssResolver = new SpringResourceTemplateResolver();

        cssResolver.setApplicationContext(applicationContext);
        cssResolver.setPrefix("/staticResources/css/");
        cssResolver.setSuffix(".css");

        return cssResolver;
    }

    @Bean
    public SpringResourceTemplateResolver pngResolver() {
        SpringResourceTemplateResolver pngResolver = new SpringResourceTemplateResolver();

        pngResolver.setApplicationContext(applicationContext);
        pngResolver.setPrefix("/staticResources/png/");
        pngResolver.setSuffix(".png");

        return pngResolver;
    }

    @Bean
    public SpringResourceTemplateResolver jsResolver() {
        SpringResourceTemplateResolver jsResolver = new SpringResourceTemplateResolver();

        jsResolver.setApplicationContext(applicationContext);
        jsResolver.setPrefix("/staticResources/jquery/");
        jsResolver.setSuffix(".js");

        return jsResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();

        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(htmlResolver());

        return engine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();

        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");

        return resolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/staticResources/**")
                .addResourceLocations("/staticResources/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(1048576); //1 MB

        return multipartResolver;
    }

}
