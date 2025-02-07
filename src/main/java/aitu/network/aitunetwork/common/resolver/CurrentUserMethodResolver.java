package aitu.network.aitunetwork.common.resolver;


import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Service
@RequiredArgsConstructor
public class CurrentUserMethodResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {
        var principal = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUser().getEmail();
        return userRepository.findUserByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(User.class, "email", email));
    }
}
