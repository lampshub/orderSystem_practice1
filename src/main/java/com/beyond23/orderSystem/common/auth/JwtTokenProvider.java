package com.beyond23.orderSystem.common.auth;

import com.beyond23.orderSystem.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.hibernate.annotations.Comment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.Signature;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}") //yml에 있는 코드를 가져옴
    private String st_secret_key;

    @Value("${jwt.expiration}")
    private int expiration;

//    인코딩된 문자열 -> 디코딩된 문자열 -> HS512알고리즘으로 암호화
//    st_secret_key를 디코딩 + 암호화
    private Key secret_key ;

    //    생성자 호출 이후에 아래 메서드를 실행하게 함으로서 @Value보다 늦게(생성자 만들어지고 나서) 실행하게되어 각 주입의 문제해결
    @PostConstruct
    public void init(){
        secret_key = new SecretKeySpec(Base64.getDecoder().decode(st_secret_key), SignatureAlgorithm.HS512.getJcaName());
    }


    public String createToken(Member member){

//        sub : abc@naver.com 형태
        Claims claims = Jwts.claims().setSubject(member.getEmail());
        claims.put("role",member.getRole().toString());

        Date now = new Date();

//        토큰의 구성요소 : 헤더, 페이로드, 시그니처(서명부)
        String token = Jwts.builder()
//                아래 3가지 요소는 페이로드
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+expiration*60*1000L)) //30분*60초*1000밀리초 : 30분을 밀리초형태로 변환 => 실무에선 yml에서 코드 관리(30->expiration)
//                아래 메서드는 secret키를 통해 서명값(signature) 생성
                .signWith(secret_key)
                .compact();    //compact 는 return을 string으로 만들어줌
        return token;
    }

}

