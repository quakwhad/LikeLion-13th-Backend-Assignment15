package com.likelion.likelionmail.member.application;

import com.likelion.likelionmail.common.error.ErrorCode;
import com.likelion.likelionmail.common.exception.BusinessException;
import com.likelion.likelionmail.global.jwt.JwtTokenProvider;
import com.likelion.likelionmail.member.api.dto.request.MemberJoinReqDto;
import com.likelion.likelionmail.member.api.dto.request.MemberLoginReqDto;
import com.likelion.likelionmail.member.api.dto.response.MemberInfoResDto;
import com.likelion.likelionmail.member.domain.Member;
import com.likelion.likelionmail.member.domain.Role;
import com.likelion.likelionmail.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원가입 (email, password, name)
    @Transactional
    public void join(MemberJoinReqDto memberJoinReqDto) {
        // 존재하는 이메일 확인
        if(memberRepository.existsByEmail(memberJoinReqDto.email())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_EMAIL,
                    ErrorCode.ALREADY_EXISTS_EMAIL.getMessage());
        }
        // 멤버 객체
        Member member = Member.builder()
                .email(memberJoinReqDto.email())
                .password(passwordEncoder.encode(memberJoinReqDto.password()))
                .name(memberJoinReqDto.name())
                .role(Role.ROLE_USER)
                .build();

        // 멤버 저장
        memberRepository.save(member);
    }

    // 로그인
    public MemberInfoResDto login(MemberLoginReqDto memberLoginReqDto) {
        // 멤버 찾기
        Member member = memberRepository.findByEmail(memberLoginReqDto.email())
            .orElseThrow(
                    () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION,
                            ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));


        //비밀번호 일치하는지 확인 로직
        if(!passwordEncoder.matches(memberLoginReqDto.password(), member.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD
            , ErrorCode.INVALID_PASSWORD.getMessage());
        }
        // 토큰 생성
        String token = jwtTokenProvider.generateToken(member);
        // 토큰과 함께 멤버 정보 리턴
        return MemberInfoResDto.of(member, token);
    }


}
