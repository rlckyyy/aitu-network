package aitu.network.aitunetwork.config;

import aitu.network.aitunetwork.common.resolver.CurrentUserMethodResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final CurrentUserMethodResolver userMethodResolver;
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userMethodResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
}
