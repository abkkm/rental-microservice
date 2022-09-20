package com.microservice.postsservice.vo;


import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RequestCreateComment {
    @NotNull()
    String writer;

    @NotNull(message = "Comment not null")
    String comment;

    @NotNull(message = "post id not null")
    Long postId;

    String createdDate;
}
