package com.microservice.postsservice.service;

import com.microservice.postsservice.dto.PostDto;
import com.microservice.postsservice.entity.CommentEntity;
import com.microservice.postsservice.entity.ImageEntity;
import com.microservice.postsservice.entity.PostEntity;
import com.microservice.postsservice.helper.DateFormat;
import com.microservice.postsservice.helper.FileFormat;
import com.microservice.postsservice.helper.PostStatus;
import com.microservice.postsservice.helper.PostType;
import com.microservice.postsservice.repository.ImageRepository;
import com.microservice.postsservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    private ImageRepository imageRepository;

    @Autowired
    public PostServiceImpl(
            PostRepository postRepository,
            ImageRepository imageRepository
    ) {
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }


    @Override
    @Transactional
    public PostDto write(PostDto postDto) throws Exception {
        PostEntity post = PostEntity.builder()
                .postType(postDto.getPostType())
                .rentalPrice(postDto.getRentalPrice())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .startDate(postDto.getStartDate())
                .endDate(postDto.getEndDate())
                .writer(postDto.getWriter())
                .userId(postDto.getUserId())
                .createdDate(DateFormat.now())
                .status(postDto.getStatus())
                .build();
        List<ImageEntity> images = FileFormat.parseFileInfo(postDto.getMultipartFiles(), post);
        postRepository.save(post);
        if (!images.isEmpty()) {
            for (ImageEntity image : images
            ) {
                post.addImage(imageRepository.save(image));
            }
        }
        return PostDto.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .postType(post.getPostType())
                .rentalPrice(post.getRentalPrice())
                .title(post.getTitle())
                .content(post.getContent())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .createdDate(post.getCreatedDate())
                .writer(post.getWriter())
                .images(images)
                .build();
    }

    @Transactional
    @Override
    public Optional<PostDto> readPostById(Long id) {
        PostEntity postEntity = postRepository.findById(id).get();
        List<ImageEntity> images = new ArrayList<>();
        List<CommentEntity> comments = new ArrayList<>();
        if (postEntity.getPostType().equals(PostType.WANT_RENTAL.name())) {
            postEntity.getImages().forEach(i -> {
                String filePath = i.getFilePath();
                i.setFilePath(FileFormat.getFileContent(filePath));

                images.add(i);
            });
        }

        postEntity.getComments().forEach(i -> {
            comments.add(CommentEntity.builder()
                    .id(i.getId())
                    .comment(i.getComment())
                    .writer(i.getWriter())
                    .createdDate(i.getCreatedDate())
                    .build());
        });

        return Optional.ofNullable(PostDto.builder()
                .id(postEntity.getId())
                .userId(postEntity.getUserId())
                .postType(postEntity.getPostType())
                .rentalPrice(postEntity.getRentalPrice())
                .title(postEntity.getTitle())
                .content(postEntity.getContent())
                .startDate(postEntity.getStartDate())
                .endDate(postEntity.getEndDate())
                .createdDate(postEntity.getCreatedDate())
                .writer(postEntity.getWriter())
                .images(images)
                .comments(comments)
                .status(postEntity.getStatus())
                .build());
    }

    @Transactional
    @Override
    public Iterable<PostDto> getAllPosts() {
        List<String> exceptList = new ArrayList<>();

        exceptList.add(PostStatus.COMPLETE_RENTAL.name());
        exceptList.add(PostStatus.DELETE_POST.name());

        Iterable<PostEntity> posts = postRepository.findAllByStatusNotIn(exceptList);
        List<PostDto> postList = new ArrayList<>();

        posts.forEach(v -> {
            List<ImageEntity> images = new ArrayList<>();

            if (v.getPostType().equals(PostType.WANT_RENTAL.name())) {
                v.getImages().forEach(i -> {
                    String filePath = i.getFilePath();
                    i.setFilePath(FileFormat.getFileContent(filePath));

                    images.add(i);
                });
            }

            postList.add(PostDto.builder()
                    .id(v.getId())
                    .userId(v.getUserId())
                    .postType(v.getPostType())
                    .title(v.getTitle())
                    .createdDate(v.getCreatedDate())
                    .writer(v.getWriter())
                    .images(images)
                    .status(v.getStatus())
                    .build());
        });

        return postList;
    }

    @Transactional
    @Override
    public Iterable<PostDto> getAllPostsByStatus(String status) {
        Iterable<PostEntity> posts = postRepository.findAllByStatus(status);
        List<PostDto> postList = new ArrayList<>();

        posts.forEach(v -> {
            List<ImageEntity> images = new ArrayList<>();

            if (v.getPostType().equals(PostType.WANT_RENTAL.name())) {
                v.getImages().forEach(i -> {
                    String filePath = i.getFilePath();
                    i.setFilePath(FileFormat.getFileContent(filePath));

                    images.add(i);
                });

            }

            postList.add(PostDto.builder()
                    .id(v.getId())
                    .userId(v.getUserId())
                    .postType(v.getPostType())
                    .title(v.getTitle())
                    .createdDate(v.getCreatedDate())
                    .writer(v.getWriter())
                    .images(images)
                    .status(v.getStatus())
                    .build());
        });

        return postList;
    }

    @Transactional
    @Override
    public Iterable<PostDto> getPostsByUserId(String userId) {
        Iterable<PostEntity> posts = postRepository.findAllByUserId(userId);
        List<PostDto> postList = new ArrayList<>();

        posts.forEach(v -> {
            List<ImageEntity> images = new ArrayList<>();

            if (v.getPostType().equals(PostType.WANT_RENTAL.name())) {
                v.getImages().forEach(i -> {
                    String filePath = i.getFilePath();
                    i.setFilePath(FileFormat.getFileContent(filePath));

                    images.add(i);
                });
            }

            postList.add(PostDto.builder()
                    .id(v.getId())
                    .userId(v.getUserId())
                    .postType(v.getPostType())
                    .title(v.getTitle())
                    .createdDate(v.getCreatedDate())
                    .writer(v.getWriter())
                    .images(images)
                    .status(v.getStatus())
                    .build());
        });

        return postList;
    }

    @Transactional
    @Override
    public PostDto deletePost(Long postId) {
        PostEntity postEntity = postRepository.findPostById(postId);

        postEntity.setStatus(PostStatus.DELETE_POST.name());

        postRepository.save(postEntity);

        return PostDto.builder()
                .id(postEntity.getId())
                .status(postEntity.getStatus())
                .build();
    }

    @Transactional
    @Override
    public Iterable<PostDto> getPostsByKeyword(String keyword) {
        Iterable<PostEntity> posts = postRepository.findByKeywordLike(keyword);
        List<PostDto> postList = new ArrayList<>();

        posts.forEach(v -> {
            List<ImageEntity> images = new ArrayList<>();

            if (v.getPostType().equals(PostType.WANT_RENTAL.name())) {
                v.getImages().forEach(i -> {
                    String filePath = i.getFilePath();
                    i.setFilePath(FileFormat.getFileContent(filePath));

                    images.add(i);
                });
            }

            postList.add(PostDto.builder()
                    .id(v.getId())
                    .userId(v.getUserId())
                    .postType(v.getPostType())
                    .title(v.getTitle())
                    .createdDate(v.getCreatedDate())
                    .writer(v.getWriter())
                    .images(images)
                    .status(v.getStatus())
                    .build());
        });

        return postList;
    }

    @Override
    public Iterable<PostDto> getPostsByCategory(String category) {
        ArrayList<String> exceptList = new ArrayList<>();

        exceptList.add(PostStatus.COMPLETE_RENTAL.name());
        exceptList.add(PostStatus.DELETE_POST.name());
        Iterable<PostEntity> postEntities = postRepository.findAllByCategory(category, exceptList);
        List<PostDto> postList = new ArrayList<>();
        postEntities.forEach(v -> {
            List<ImageEntity> images = new ArrayList<>();
            if (v.getPostType().equals(PostType.WANT_RENTAL.name())) {
                for (ImageEntity image : v.getImages()) {
                    String filePath = image.getFilePath();
                    image.setFilePath(FileFormat.getFileContent(filePath));
                    images.add(image);
                }
            }
            postList.add(PostDto.builder()
                    .postType(v.getPostType())
                    .createdDate(v.getCreatedDate())
                    .id(v.getId())
                    .userId(v.getUserId())
                    .rentalPrice(v.getRentalPrice())
                    .title(v.getTitle())
                    .content(v.getContent())
                    .images(images)
                    .status(v.getStatus())
                    .writer(v.getWriter())
                    .build());
        });
        return postList;
    }

    @Transactional
    @Override
    public void rental(Long postId) {
        PostEntity entity = postRepository.findPostById(postId);

        entity.setStatus(PostStatus.COMPLETE_RENTAL.name());

        postRepository.save(entity);
    }

    @Transactional
    @Override
    public void rollbackPost(Long postId) {
        PostEntity entity = postRepository.findPostById(postId);

        entity.setStatus(PostStatus.READY_RENTAL.name());

        postRepository.save(entity);
    }
}
