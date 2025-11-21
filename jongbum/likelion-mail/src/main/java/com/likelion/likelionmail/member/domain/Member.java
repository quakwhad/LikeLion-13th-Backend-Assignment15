package com.likelion.likelionmail.member.domain;

import com.likelion.likelionmail.post.domain.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_email", nullable = false)
    private String email;
    private String password;

    @Column(name = "member_name", nullable = false)
    private String name;

    @Column(name = "member_picture_url")
    private String pictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role", nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Member(String email, String password, String name, String pictureUrl, Role role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.role = role;
    }

}