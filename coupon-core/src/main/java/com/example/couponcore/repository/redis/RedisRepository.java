package com.example.couponcore.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    // SortedSet
    public Boolean zAdd(String key, String value, double score){
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score);
    }
    
    // Set에 값 넣기
    public Long sAdd(String key, String value){
        return redisTemplate.opsForSet().add(key, value);
    }

    // Set의 크기 구하기
    public Long sCard(String key){
        return redisTemplate.opsForSet().size(key);
    }

    // Set에 해당 데이터가 존재하는지 확인
    public Boolean sIsMember(String key, String value){
        return redisTemplate.opsForSet().isMember(key, value);
    }
    
    // Queue 적재를 위한 list에 값 넣는 과정
    public Long rPush(String key, String value){
        return redisTemplate.opsForList().rightPush(key, value);
    }
}



