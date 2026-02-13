package com.pu.es;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface JobESRepository extends ElasticsearchRepository<JobES,Long> {
    List<JobES> findByTitleContainingOrDescriptionContainingOrTagsContaining(String title, String description, String tags);
}
