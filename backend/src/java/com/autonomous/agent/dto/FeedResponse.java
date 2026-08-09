package com.autonomous.agent.dto;

import java.util.List;

public class FeedResponse {

    private List<PostDto> posts;

    public FeedResponse() {
    }

    public FeedResponse(List<PostDto> posts) {
        this.posts = posts;
    }

    public List<PostDto> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDto> posts) {
        this.posts = posts;
    }
}
