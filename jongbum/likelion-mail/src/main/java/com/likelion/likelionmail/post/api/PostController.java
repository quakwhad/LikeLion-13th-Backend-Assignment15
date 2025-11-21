package com.likelion.likelionmail.post.api;

import com.likelion.likelionmail.common.error.SuccessCode;
import com.likelion.likelionmail.common.template.ApiResTemplate;
import com.likelion.likelionmail.post.api.dto.request.PostSaveRequestDto;
import com.likelion.likelionmail.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.likelionmail.post.api.dto.response.PostListResponseDto;
import com.likelion.likelionmail.post.application.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 게시물 저장
    @PostMapping("/save")
    public ApiResTemplate<String> postSave(@RequestBody @Valid PostSaveRequestDto postSaveRequestDto, Principal principal) {
        postService.postSave(postSaveRequestDto, principal);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_SAVE_SUCCESS);
    }

    // 전체 게시글 조회
    @GetMapping("/all")
    public ApiResTemplate<PostListResponseDto> postFindAll() {
        PostListResponseDto postListResponseDto = postService.postFindAll();
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, postListResponseDto);
    }

    // 사용자 id를 기준으로 해당 사용자가 작성한 게시글 목록 조회
    @GetMapping("/{members}")
    public ApiResTemplate<PostListResponseDto> myPostFindAll(Principal principal) {
        PostListResponseDto postListResponseDto = postService.postFindMember(principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, postListResponseDto);
    }

    // 게시글 id를 기준으로 사용자가 작성한 게시물 수정
    @PatchMapping("/{postId}")
    public ApiResTemplate<String> postUpdate(@PathVariable("postId") Long postId,
                                             @RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto) {
        postService.postUpdate(postId, postUpdateRequestDto);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_UPDATE_SUCCESS);
    }

    // 게시글 id를 기준으로 사용자가 작성한 게시물 삭제
    @DeleteMapping("/{postId}")
    public ApiResTemplate<String> postDelete(@PathVariable("postId") Long postId) {
        postService.postDelete(postId);
        return ApiResTemplate.successWithNoContent(SuccessCode.POST_DELETE_SUCCESS);
    }
}