//package aitu.network.aitunetwork.config.security;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.web.context.HttpRequestResponseHolder;
//import org.springframework.security.web.context.SecurityContextRepository;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class CustomSecurityContextRepository implements SecurityContextRepository {
//    private static final String SECURITY_CONTEXT_KEY_PREFIX = "security:context:";
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    @Override
//    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
//        return null;
//    }
//
//
//    @Override
//    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
//        String sessionId = getSessionId(request);
//        if (sessionId != null) {
//            if (context == null) {
//                redisTemplate.delete(SECURITY_CONTEXT_KEY_PREFIX + sessionId);
//            } else {
//                redisTemplate.opsForValue().set(SECURITY_CONTEXT_KEY_PREFIX + sessionId, context);
//            }
//        }
//    }
//
//    @Override
//    public boolean containsContext(HttpServletRequest request) {
//        String sessionId = getSessionId(request);
//        return sessionId != null && redisTemplate.hasKey(SECURITY_CONTEXT_KEY_PREFIX + sessionId);
//    }
//
//    private String getSessionId(HttpServletRequest request) {
//        return request.getSession(false) != null ? request.getSession().getId() : null;
//    }
//}
