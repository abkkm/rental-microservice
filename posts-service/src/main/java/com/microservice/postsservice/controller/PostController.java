package com.microservice.postsservice.controller;


import com.microservice.postsservice.dto.CommentDto;
import com.microservice.postsservice.dto.PostDto;
import com.microservice.postsservice.helper.DateFormat;
import com.microservice.postsservice.helper.PostStatus;
import com.microservice.postsservice.helper.PostType;
import com.microservice.postsservice.repository.PostRepository;
import com.microservice.postsservice.service.CommentService;
import com.microservice.postsservice.service.ImageService;
import com.microservice.postsservice.service.PostService;
import com.microservice.postsservice.vo.RequestCreateComment;
import com.microservice.postsservice.vo.RequestPost;
import com.microservice.postsservice.vo.ResponsePost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/")
public class PostController {
    private PostService postService;
    private CommentService commentService;
    private ImageService imageService;
    private Environment env;

    @Autowired
    public PostController(
            PostService postService,
            CommentService commentService,
            ImageService imageService,
            Environment env
    ) {
        this.postService = postService;
        this.commentService = commentService;
        this.imageService = imageService;
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format(
                "It's working in Post Service"
                        + ", port(local.server.port) =" + env.getProperty("local.server.port")
                        + ", port(server.port) =" + env.getProperty("server.port")
        );
    }


    @PostMapping("/post")
    public ResponseEntity<?> addPost(@RequestBody RequestPost requestPost) throws Exception {
        PostDto postDto = null;
        if (Objects.equals(requestPost.getPostType(), PostType.WANT_RENTAL.name())) {
            postDto = PostDto.builder()
                    .writer(requestPost.getWriter())
                    .userId(requestPost.getUserId())
                    .postType(requestPost.getPostType())
                    .rentalPrice(requestPost.getRentalPrice())
                    .title(requestPost.getTitle())
                    .content(requestPost.getContent())
                    .startDate(null)
                    .endDate(null)
                    .createdDate(DateFormat.now())
                    .multipartFiles(requestPost.getImages())
                    .status(PostStatus.REQUEST_RENTAL.name())
                    .build();
        } else {
            postDto = PostDto.builder()
                    .postType(requestPost.getPostType())
                    .title(requestPost.getTitle())
                    .content(requestPost.getContent())
                    .startDate(requestPost.getDate().get(0))
                    .endDate(requestPost.getDate().get(1))
                    .rentalPrice(requestPost.getRentalPrice())
                    .writer(requestPost.getWriter())
                    .createdDate(DateFormat.now())
                    .status(PostStatus.READY_RENTAL.name())
                    .userId(requestPost.getUserId())
                    .multipartFiles(requestPost.getImages())
                    .build();
        }
        PostDto post = postService.write(postDto);
        ResponsePost result = ResponsePost.builder()
                .id(post.getId())
                .postType(post.getPostType())
                .title(post.getTitle())
                .content(post.getContent())
                .rentalPrice(post.getRentalPrice())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .createdDate(post.getCreatedDate())
                .writer(post.getWriter())
                .userId(post.getUserId())
                .images(post.getImages())
                .comments(post.getComments())
                .status(post.getStatus())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<?> getPostById(@PathVariable("id") Long id) throws Exception {
        try {
            System.out.println("id : " + id);
            PostDto postDto = postService.readPostById(id).get();
            return ResponseEntity.status(HttpStatus.OK).body(ResponsePost
                    .builder()
                    .id(postDto.getId())
                    .postType(postDto.getPostType())
                    .title(postDto.getTitle())
                    .content(postDto.getContent())
                    .rentalPrice(postDto.getRentalPrice())
                    .startDate(postDto.getStartDate())
                    .endDate(postDto.getEndDate())
                    .createdDate(postDto.getCreatedDate())
                    .writer(postDto.getWriter())
                    .userId(postDto.getUserId())
                    .images(postDto.getImages())
                    .comments(postDto.getComments())
                    .status(postDto.getStatus())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/posts/{status}")
    public ResponseEntity<?> getPostByStatus(@PathVariable("status") String status) throws Exception {
        try {
            Iterable<PostDto> postDtoList = postService.getAllPostsByStatus(status);
            List<ResponsePost> responsePosts = new ArrayList<>();
            postDtoList.forEach(v -> {
                responsePosts.add(ResponsePost
                        .builder()
                        .id(v.getId())
                        .postType(v.getPostType())
                        .title(v.getTitle())
                        .content(v.getContent())
                        .rentalPrice(v.getRentalPrice())
                        .startDate(v.getStartDate())
                        .endDate(v.getEndDate())
                        .createdDate(v.getCreatedDate())
                        .writer(v.getWriter())
                        .userId(v.getUserId())
                        .images(v.getImages())
                        .comments(v.getComments())
                        .status(v.getStatus())
                        .build());
            });
            return ResponseEntity.status(HttpStatus.OK).body(responsePosts);


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/posts")
    public ResponseEntity<?> getAllPosts() {

        Iterable<PostDto> postList = postService.getAllPosts();
        List<ResponsePost> result = new ArrayList<>();

        postList.forEach(post -> {
            result.add(
                    ResponsePost.builder()
                            .id(post.getId())
                            .postType(post.getPostType())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .rentalPrice(post.getRentalPrice())
                            .startDate(post.getStartDate())
                            .endDate(post.getEndDate())
                            .createdDate(post.getCreatedDate())
                            .writer(post.getWriter())
                            .userId(post.getUserId())
                            .status(post.getStatus())
                            .images(post.getImages())
                            .comments(post.getComments())
                            .build());
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<?> getPostByUserId(@PathVariable("userId") String userId) throws Exception {
        try {
            Iterable<PostDto> postDtos = postService.getPostsByUserId(userId);
            List<ResponsePost> responsePosts = new ArrayList<>();
            postDtos.forEach(postDto -> {
                responsePosts.add(ResponsePost.builder()
                        .id(postDto.getId())
                        .postType(postDto.getPostType())
                        .title(postDto.getTitle())
                        .content(postDto.getContent())
                        .rentalPrice(postDto.getRentalPrice())
                        .startDate(postDto.getStartDate())
                        .endDate(postDto.getEndDate())
                        .createdDate(postDto.getCreatedDate())
                        .writer(postDto.getWriter())
                        .userId(postDto.getUserId())
                        .status(postDto.getStatus())
                        .images(postDto.getImages())
                        .comments(postDto.getComments())
                        .build());
            });
            return ResponseEntity.status(HttpStatus.OK).body(responsePosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/posts/keyword/{keyword}")
    public ResponseEntity<?> getPostsByKeyword(@PathVariable("keyword") String keyword) throws Exception {
        Iterable<PostDto> postDtos = postService.getPostsByKeyword(keyword);
        List<ResponsePost> responsePosts = new ArrayList<>();
        postDtos.forEach(postDto -> {
            responsePosts.add(ResponsePost.builder()
                    .id(postDto.getId())
                    .postType(postDto.getPostType())
                    .title(postDto.getTitle())
                    .content(postDto.getContent())
                    .rentalPrice(postDto.getRentalPrice())
                    .startDate(postDto.getStartDate())
                    .endDate(postDto.getEndDate())
                    .createdDate(postDto.getCreatedDate())
                    .writer(postDto.getWriter())
                    .userId(postDto.getUserId())
                    .status(postDto.getStatus())
                    .images(postDto.getImages())
                    .comments(postDto.getComments())
                    .build());
        });
        return ResponseEntity.status(HttpStatus.OK).body(responsePosts);
    }

    @GetMapping("/posts/category/{category}")
    public ResponseEntity<?> getPostsByCategory(@PathVariable("category") String category) {

        Iterable<PostDto> postList = postService.getPostsByCategory(category);
        List<ResponsePost> result = new ArrayList<>();

        postList.forEach(post -> {
                    result.add(ResponsePost.builder()
                            .id(post.getId())
                            .postType(post.getPostType())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .rentalPrice(post.getRentalPrice())
                            .startDate(post.getStartDate())
                            .endDate(post.getEndDate())
                            .createdDate(post.getCreatedDate())
                            .writer(post.getWriter())
                            .userId(post.getUserId())
                            .status(post.getStatus())
                            .images(post.getImages())
                            .comments(post.getComments())
                            .build());
                }
        );

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

//    @PostMapping("/rental")
//    public ResponseEntity<?> rental(@RequestBody RequestRental postVo) {
//        log.info("Post Service's Controller Layer :: Call rental Method!");
//
//        kafkaProducer.send("rental-topic", postVo);
//
//        postService.rental(postVo.getPostId());
//
//        return ResponseEntity.status(HttpStatus.OK).body("Success rental");
//    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.deletePost(id));
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody RequestCreateComment comment) throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(commentService.addComment(CommentDto.builder()
                    .comment(comment.getComment())
                    .postId(comment.getPostId())
                    .writer(comment.getWriter())
                    .build()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @DeleteMapping("/{id}/comments")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.deleteComment(id));
    }

    @PostMapping("/rollback/{postId}")
    public ResponseEntity<?> rollbackPost(@PathVariable("postId") Long postId) {

        postService.rollbackPost(postId);

        return ResponseEntity.status(HttpStatus.OK).body("Successfully rollback!");
    }

}
