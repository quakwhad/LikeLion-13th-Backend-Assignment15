package com.likelion.likelionmail.global.jwt;

import com.likelion.likelionmail.common.error.ErrorCode;
import com.likelion.likelionmail.common.exception.BusinessException;
import com.likelion.likelionmail.member.domain.Member;
import com.likelion.likelionmail.member.domain.repository.MemberRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final MemberRepository memberRepository;

    @Value("${token.expire.time}")
    private String tokenExpireTime;

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey key;

    public JwtTokenProvider(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성 - 이메일을 subject로
    public String generateToken(Member member) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + Long.parseLong(tokenExpireTime));

        return Jwts.builder()
                .subject(member.getEmail()) //이메일을 subject로
                .claim(AUTHORITIES_KEY, member.getRole().toString())
                .issuedAt(now)
                .expiration(expireDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 유효하지 않습니다.");
        } catch (SignatureException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 서명 검증에 실패했습니다.");
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 만료되었습니다.");
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 가 null 이거나 비어있거나 공백만 있습니다.");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "JWT 검증에 실패했습니다.");
        }
    }

    // 인증 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String authority = claims.get(AUTHORITIES_KEY, String.class);
        if (authority == null) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "권한 정보가 없는 토큰입니다.");
        }

        List<GrantedAuthority> authorities = Arrays.stream(authority.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //수정된 부분: subject는 email이므로, 이메일로 Member 조회
        String email = claims.getSubject();
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, "회원 정보가 없습니다."));

        // principal에 이메일 넣기
        return new UsernamePasswordAuthenticationToken(member.getEmail(), "", authorities);
    }

    // Claims 파싱
    private Claims parseClaims(String accessToken) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            return parser.parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
