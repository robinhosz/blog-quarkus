package io.github.robinhosz.quarkussocial.rest.dto;

import io.github.robinhosz.quarkussocial.rest.entities.Follower;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowerResponse {

    private Long id;
    private String name;

    public FollowerResponse(Follower follower) {
        this(follower.getId(), follower.getFollower().getName());
    }
}
