package com.wziem.store.services;

import com.wziem.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class Jwt {
    private final Claims claims;
    private final SecretKey key;


    public long getUserId() {
        return Long.parseLong(claims.getSubject());
    }

    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public String toString() {
        return Jwts.builder().claims(claims).signWith(key).compact();
    }

    public boolean isExpired() {
        if( claims == null ) {
            return false;
        }

        return !claims.getExpiration().after(new Date());
    }


}
