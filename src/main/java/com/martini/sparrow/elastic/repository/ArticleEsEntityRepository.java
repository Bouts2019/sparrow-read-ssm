package com.martini.sparrow.elastic.repository;

import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author martini at 2020/12/16 17:42
 */
public interface ArticleEsEntityRepository extends ElasticsearchRepository<ArticleEsEntity, String> {
}
