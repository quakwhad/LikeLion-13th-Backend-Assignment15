package com.likelion.likelionmail.post.domain.repository;


import com.likelion.likelionmail.member.domain.Member;
import com.likelion.likelionmail.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByMember(Member member);
}
