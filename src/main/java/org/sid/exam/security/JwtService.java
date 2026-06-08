package org.sid.exam.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private final ObjectMapper objectMapper;
    private final String secret;
    private final long expirationSeconds;

    public JwtService(@Value("${app.jwt.secret:exam-secret-key-change-me-256-bits-minimum}") String secret,
                      @Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds) {
        this.objectMapper = new ObjectMapper();
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", username);
        payload.put("iat", now.getEpochSecond());
        payload.put("exp", now.plusSeconds(expirationSeconds).getEpochSecond());
        payload.put("roles", authorities.stream().map(GrantedAuthority::getAuthority).toList());

        String unsignedToken = base64UrlEncode(toJson(header)) + "." + base64UrlEncode(toJson(payload));
        return unsignedToken + "." + sign(unsignedToken);
    }

    public String extractUsername(String token) {
        return getPayload(token).get("sub").toString();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Map<String, Object> payload = getPayload(token);
        String username = payload.get("sub").toString();
        long exp = ((Number) payload.get("exp")).longValue();
        return username.equals(userDetails.getUsername())
                && exp > Instant.now().getEpochSecond()
                && signatureIsValid(token);
    }

    public List<String> extractRoles(String token) {
        Object roles = getPayload(token).get("roles");
        if (roles instanceof List<?> list) {
            return list.stream().map(Object::toString).toList();
        }
        return List.of();
    }

    private boolean signatureIsValid(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        return sign(parts[0] + "." + parts[1]).equals(parts[2]);
    }

    private Map<String, Object> getPayload(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3 || !signatureIsValid(token)) {
                throw new IllegalArgumentException("Token JWT invalide");
            }
            byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
            return objectMapper.readValue(decoded, new TypeReference<>() {
            });
        } catch (Exception exception) {
            throw new IllegalArgumentException("Token JWT invalide", exception);
        }
    }

    private byte[] toJson(Map<String, Object> content) {
        try {
            return objectMapper.writeValueAsBytes(content);
        } catch (Exception exception) {
            throw new IllegalStateException("Generation JWT impossible", exception);
        }
    }

    private String base64UrlEncode(byte[] content) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(content);
    }

    private String sign(String content) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            return base64UrlEncode(mac.doFinal(content.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception exception) {
            throw new IllegalStateException("Signature JWT impossible", exception);
        }
    }
}
