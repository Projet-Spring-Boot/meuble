package com.spring.social.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.util.Map;
import com.spring.social.model.Flow;

@Repository
public class FlowDAOImpl implements FlowDAO {

	private HashOperations hashOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    //@Autowired
    public FlowDAOImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;

        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void save(Flow flow) {
        hashOperations.put("FLOW", flow.getId(), flow);

    }

    @Override
    public Map<String,Flow> findAll() {

        return hashOperations.entries("FLOW");
    }

    @Override
    public Flow findById(String id) {

        return (Flow) hashOperations.get("FLOW", id);
    }

    @Override
    public void deleteById(String id) {
        hashOperations.delete("FLOW", id);
    }

    @Override
    public String drop(){
        Map<String, Flow> map;
        map=hashOperations.entries("FLOW");
        for(String id : map.keySet())
            deleteById(id);
        return "\nDatabase dropped!\n";}
}
