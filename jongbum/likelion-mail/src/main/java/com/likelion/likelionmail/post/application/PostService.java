package com.likelion.likelionmail.post.application;

import com.likelion.likelionmail.common.error.ErrorCode;
import com.likelion.likelionmail.common.exception.BusinessException;
import com.likelion.likelionmail.member.domain.Member;
import com.likelion.likelionmail.member.domain.repository.MemberRepository;
import com.likelion.likelionmail.post.api.dto.request.PostSaveRequestDto;
import com.likelion.likelionmail.post.api.dto.request.PostUpdateRequestDto;
import com.likelion.likelionmail.post.api.dto.response.PostInfoResponseDto;
import com.likelion.likelionmail.post.api.dto.response.PostListResponseDto;
import com.likelion.likelionmail.post.domain.Post;
import com.likelion.likelionmail.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    // 게시물 저장
    @Transactional
    public void postSave(PostSaveRequestDto postSaveRequestDto, Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));

        Post post = Post.builder()
                .title(postSaveRequestDto.title())
                .content(postSaveRequestDto.content())
                .member(member)
                .build();

        postRepository.save(post);
    }

    // 전체 게시글 조회
    public PostListResponseDto postFindAll() {
        List<Post> posts = postRepository.findAll();
        List<PostInfoResponseDto> postInfoResponseDtos = posts.stream()
                .map(PostInfoResponseDto::from)
                .toList();

        return PostListResponseDto.from(postInfoResponseDtos);
    }

    // 특정 작성자가 작성한 게시글 목록을 조회
    public PostListResponseDto postFindMember(Principal principal) {
        Long memberId = Long.parseLong(principal.getName());
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + memberId));

        List<Post> posts = postRepository.findByMember(member);
        List<PostInfoResponseDto> postInfoResponseDtos = posts.stream()
                .map(PostInfoResponseDto::from)
                .toList();

        return PostListResponseDto.from(postInfoResponseDtos);
    }

    // 게시물 수정
    @Transactional
    public void postUpdate(Long postId, PostUpdateRequestDto postUpdateRequestDto)
    {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND_EXCEPTION
                        , ErrorCode.POST_NOT_FOUND_EXCEPTION.getMessage() + postId)
        );
        post.update(postUpdateRequestDto);
    }

    // 게시물 삭제
    @Transactional
    public void postDelete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new BusinessException(ErrorCode.POST_NOT_FOUND_EXCEPTION
                        , ErrorCode.POST_NOT_FOUND_EXCEPTION.getMessage() + postId)
        );
        postRepository.delete(post);
    }
}