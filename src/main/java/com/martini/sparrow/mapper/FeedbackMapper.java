package com.martini.sparrow.mapper;

import com.martini.sparrow.entity.Feedback;
import net.bytebuddy.agent.builder.AgentBuilder;

import java.util.List;

/**
 * @author martini at 2021/1/13 14:12
 */
public interface FeedbackMapper {
    List<Feedback> findAll();
    int postOne(Feedback feedback);
}
