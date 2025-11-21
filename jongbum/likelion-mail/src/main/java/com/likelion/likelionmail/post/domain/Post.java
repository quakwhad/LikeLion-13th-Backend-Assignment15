package com.likelion.likelionmail.post.domain;

import com.likelion.likelionmail.member.domain.Member;
import com.likelion.likelionmail.post.api.dto.request.PostUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    private Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }

    public void update(PostUpdateRequestDto postUpdateRequestDto) {
        this.title = postUpdateRequestDto.title();
        this.content = postUpdateRequestDto.content();
    }
}