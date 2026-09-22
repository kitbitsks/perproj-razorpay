package com.perproj.razorpay.merchant.security;

import com.perproj.razorpay.merchant.cache.ApiKeyCache;
import com.perproj.razorpay.merchant.cache.ApiKeyCacheEntry;
import com.perproj.razorpay.merchant.entity.ApiKey;
import com.perproj.razorpay.merchant.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    private static final String BASIC_PREFIX = "Basic";

    private final ApiKeyCache apiKeyCache;

    private final ApiKeyRepository apiKeyRepository;

    private final MerchantContext merchantContext;

    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("Incoming request : {}", request.getRequestURI());

        try{
            String header = request.getHeader("Authorization");

            if(header == null || !header.startsWith(BASIC_PREFIX)){
                filterChain.doFilter(request,response);
                return;
            }
            
            String[] credentials = decode(header);

            if(credentials == null){
                throw new BadRequestException("Malformed API key header");
            }

            String keyId = credentials[0];
            String secret = credentials[1];

            ApiKeyCacheEntry apiKeyCacheEntry = apiKeyCache.get(keyId).orElseGet(
                    ()-> loadAndCache(keyId)
            );

            /*
            *
            * Add Rate limiting
            *
            * */

            var auth = new UsernamePasswordAuthenticationToken(keyId,null, List.of(new SimpleGrantedAuthority("API_KEY_ROLE")));
            SecurityContextHolder.getContext().setAuthentication(auth);
            merchantContext.setKeyId(apiKeyCacheEntry.keyId());
            merchantContext.setMerchantId(apiKeyCacheEntry.merchantId());
            filterChain.doFilter(request,response);
        }
        catch(Exception e){
            handlerExceptionResolver.resolveException(request, response,null,e);
        }
    }

    private ApiKeyCacheEntry loadAndCache(String keyId) {
        ApiKey apiKey = apiKeyRepository.findByKeyId(keyId).orElse(null);
        if(apiKey == null){
            return null;
        }
        ApiKeyCacheEntry apiKeyCacheEntry = new ApiKeyCacheEntry(
                apiKey.getKeyId(),
                apiKey.getKeySecretHash(),
                apiKey.getPreviousKeySecretHash(),
                apiKey.getGracePeriodExpiresAt(),
                apiKey.getMerchant().getId(),
                apiKey.getEnvironment(),
                apiKey.isEnabled()
        );
        apiKeyCache.put(keyId,apiKeyCacheEntry);
        return apiKeyCacheEntry;
    }


    private String[] decode(String header) {

        String encode = header.substring(BASIC_PREFIX.length());
        String decode = new String(Base64.getDecoder().decode(encode), StandardCharsets.UTF_8);

        int colon = decode.indexOf(":");
        return new String[]{decode.substring(0,colon), decode.substring(colon+1)};
    }
}
