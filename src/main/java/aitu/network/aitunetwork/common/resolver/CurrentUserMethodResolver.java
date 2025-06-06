package aitu.network.aitunetwork.common.resolver;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.common.exception.UnauthorizedException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
public class CurrentUserMethodResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        CurrentUser currentUserAnnotation = parameter.getParameterAnnotation(CurrentUser.class);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null &&
                authentication.isAuthenticated() &&
                authentication.getPrincipal() instanceof CustomUserDetails userDetails) {

            return userDetails;
        }

        if (currentUserAnnotation != null && currentUserAnnotation.required()) {
            throw new UnauthorizedException("User not authorized");
        }

        return null;
    }
}
