package org.example.neptuneojserver.caches;

import lombok.AllArgsConstructor;
import org.example.neptuneojserver.dto.contest.UserContestDTO;
import org.example.neptuneojserver.models.Contest;
import org.example.neptuneojserver.models.User;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class ContestRankingCache {
    private final String key = "contest_ranking";

    private RedisTemplate<String, UserContestDTO> redisTemplate;

    private Long getSize(){
        ListOperations<String, UserContestDTO> listOps = redisTemplate.opsForList();
        return listOps.size(key);
    }

    public List<UserContestDTO> getListUserOfContestByContestId(Long id) {
        ListOperations<String, UserContestDTO> listOps = redisTemplate.opsForList();
        return listOps.range(key, 0, getSize());
    }
    private void addUserToContest(User user, Contest contest) {
        UserContestDTO userContestDTO = new UserContestDTO();
        userContestDTO.setUser(new User(user.getId(), user.getUsername(), user.getFullName()));
        userContestDTO.setContest(new Contest(contest.getId(), contest.getTitle()));
        userContestDTO.setRank(getSize() + 1);
        userContestDTO.setSubmissions(new ArrayList<>());
        userContestDTO.setPoints(new ArrayList<>());
        userContestDTO.setTime(0F);
        userContestDTO.setPoint(0F);
        userContestDTO.setStatus("INVERTED");

        ListOperations<String, UserContestDTO> listOps = redisTemplate.opsForList();
        listOps.rightPush(key, userContestDTO);
    }

}
