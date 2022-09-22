package com.microservice.authservice.clients;


import com.microservice.authservice.vo.response.ResponsePost;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "posts-service")
public interface PostsClient {
    @GetMapping("/{userId}/posts")
    public List<ResponsePost> getPosts(@PathVariable("userId") String userId);
}
