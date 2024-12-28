package si.uni.prpo.group03.apigateway.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            String validateTokenURL = "http://localhost:8081/api/auth/validate-token";
            ResponseEntity<String> validationResponse = restTemplate.postForEntity(validateTokenURL, token, String.class);

            final String userId = validationResponse.getBody();
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userId, null, null
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                CustomHttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(request);
                requestWrapper.addHeader("X-User-Id", userId);
                filterChain.doFilter(requestWrapper, response);
                return;
            }

        } catch (HttpClientErrorException e) {
            response.setStatus(e.getStatusCode().value());
            // we should add a response body here
            return;
        }
        filterChain.doFilter(request, response);
    }

    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final Map<String, String> customHeaders;

        public CustomHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
            this.customHeaders = new HashMap<>();
        }

        public void addHeader(String name, String value) {
            this.customHeaders.put(name, value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = customHeaders.get(name);
            if (headerValue != null) {
                return headerValue;
            }
            return super.getHeader(name);
        }

        @Override
        public Enumeration<String> getHeaderNames() {
            Enumeration<String> headerNames = super.getHeaderNames();
            return Collections.enumeration(customHeaders.keySet());
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            if (customHeaders.containsKey(name)) {
                return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
            }
            return super.getHeaders(name);
        }
    }
}