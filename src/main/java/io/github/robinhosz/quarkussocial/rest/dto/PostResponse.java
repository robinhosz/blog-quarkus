package io.github.robinhosz.quarkussocial.rest.dto;

import io.github.robinhosz.quarkussocial.rest.entities.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {

    private String text;
    private LocalDateTime localDateTime;

    public static PostResponse fromEntity(Post post) {
        var response = new PostResponse();
        response.setText(post.getText());
        response.setLocalDateTime(post.getDateTime());
        return response;
    }

}
