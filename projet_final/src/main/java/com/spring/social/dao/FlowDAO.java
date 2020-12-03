package com.spring.social.dao;

import java.util.Map;
import com.spring.social.model.Flow;

public interface FlowDAO {

    void save(Flow flow);
    Map<String,Flow> findAll();
    Flow findById(String id);
    void deleteById(String id);
    String drop();
}
