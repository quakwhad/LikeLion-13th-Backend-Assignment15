package com.likelion.likelionmail.member.api;

import com.likelion.likelionmail.common.error.SuccessCode;
import com.likelion.likelionmail.common.template.ApiResTemplate;
import com.likelion.likelionmail.member.api.dto.request.MemberJoinReqDto;
import com.likelion.likelionmail.member.api.dto.request.MemberLoginReqDto;
import com.likelion.likelionmail.member.api.dto.response.MemberInfoResDto;
import com.likelion.likelionmail.member.application.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // join
    @PostMapping("/join")
    public ApiResTemplate<String> join(@RequestBody @Valid MemberJoinReqDto memberJoinReqDto) {
        memberService.join(memberJoinReqDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.MEMBER_JOIN_SUCCESS);
    }

    // login
    @PostMapping("/login")
    public ApiResTemplate<MemberInfoResDto> login(@RequestBody @Valid MemberLoginReqDto memberLoginReqDto) {
        MemberInfoResDto memberInfoResDto = memberService.login(memberLoginReqDto);
        return ApiResTemplate.successResponse(SuccessCode.MEMBER_LOGIN_SUCCESS, memberInfoResDto);
    }
}
