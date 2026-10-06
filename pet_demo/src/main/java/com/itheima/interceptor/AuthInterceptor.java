package com.itheima.interceptor;

import com.itheima.common.BusinessException;
import com.itheima.common.ResultCdoeEnum;
import com.itheima.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;
    private final StringRedisTemplate stringRedisTemplate;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        log.info("[Auth] 收到Authorization头: [{}]", token);
        if (token == null || token.isEmpty()) {
            throw new BusinessException(ResultCdoeEnum.UNAUTHORIZED.getCode(), "缺少Token");
        }
        if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
            token = token.substring(7);
        }
        token = token.trim();
        if (token.startsWith("\"") && token.endsWith("\"")) {
            token = token.substring(1, token.length() - 1).trim();
        }
        token = token.replaceAll("\\s+", "");
        log.info("[Auth] 清洗后token长度={}, 内容=[{}]", token.length(), token);

        Claims claims = jwtUtil.parseToken(token);
        if (claims == null) {
            log.error("[Auth] parseToken返回null, 验证失败");
            throw new BusinessException(ResultCdoeEnum.UNAUTHORIZED.getCode(), "Token格式错误或签名无效");
        }
        log.info("[Auth] Token解析成功, userId={}, role={}", claims.get("userId"), claims.get("role"));

        if (claims.getExpiration().before(new Date())) {
            log.warn("[Auth] Token已过期, exp={}, now={}", claims.getExpiration(), new Date());
            throw new BusinessException(ResultCdoeEnum.UNAUTHORIZED.getCode(), "Token已过期");
        }

        Long userId = claims.get("userId", Long.class);
        String redisKey = "token:" + userId;
        String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
        log.info("[Auth] Redis key={}, RedisToken={}", redisKey, redisToken);

        if (redisToken == null) {
            log.warn("[Auth] Redis中找不到key={}, 可能用户已退出登录或Token未存入", redisKey);
            throw new BusinessException(ResultCdoeEnum.UNAUTHORIZED.getCode(), "Token无效或者已退出登录");
        }
        if (!redisToken.equals(token)) {
            log.warn("[Auth] Token与Redis不匹配, 请求Token前20=[{}], RedisToken前20=[{}]",
                    token.length() >= 20 ? token.substring(0, 20) : token,
                    redisToken.length() >= 20 ? redisToken.substring(0, 20) : redisToken);
            throw new BusinessException(ResultCdoeEnum.UNAUTHORIZED.getCode(), "Token无效或者已退出登录");
        }

        log.info("[Auth] 验证通过, 放行请求, userId={}", userId);
        request.setAttribute("userId", userId);
        request.setAttribute("role", claims.get("role", String.class));
        return true;
    }
}