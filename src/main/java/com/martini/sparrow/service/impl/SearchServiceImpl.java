package com.martini.sparrow.service.impl;

import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import com.martini.sparrow.service.SearchService;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author martini at 2020/12/15 16:45
 */
@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Override
    public List<Object> searchArticle(String keyword, String order, String desc) {
        String content = keyword;
        String author = "";
        String type = "";
        //定义list
        List<Object> lists = null;
        try {
            SearchRequest searchRequest = new SearchRequest("article");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            //搜索条件为空指定查询条件
            if (StringUtils.isEmpty(content)) {
                //设置为查询所有
                sourceBuilder.query(QueryBuilders.matchAllQuery());
            } else {
                //设置为多字段检索
                sourceBuilder.query(QueryBuilders.multiMatchQuery(content, "articleTitle", "bookName", "content", "userName"));
            }

            //指定过滤条件
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            if (!StringUtils.isEmpty(author)) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("author",author));
            }
            if (!StringUtils.isEmpty(type)) {
                boolQueryBuilder.filter(QueryBuilders.termQuery("type",type));
            }
            //指定过滤
            sourceBuilder.postFilter(boolQueryBuilder);

            //指定高亮
            sourceBuilder.highlighter(new HighlightBuilder().field("*").requireFieldMatch(false).preTags("<span style='color:red;'>").postTags("</span>"));

            //指定显示记录
            sourceBuilder.size(10);
            // 指定排序
            if (order != null) {
                if (order.equals("read")) order = "readCount";
                else if (order.equals("like")) order = "likeCount";
                else if (order.equals("time")) order = "publishTime";
                sourceBuilder.sort(order, (desc == null || Objects.equals("desc", desc)) ? SortOrder.DESC : SortOrder.ASC);
            }

            //指定搜索类型
            searchRequest.types("article").source(sourceBuilder);

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            //获取返回结果
            if (searchResponse.getHits().totalHits > 0) lists = new ArrayList<>();
            SearchHit[] hits = searchResponse.getHits().getHits();
            for (SearchHit hit : hits) {
                ArticleEsEntity articleEsEntity = new ArticleEsEntity();

                //获取原始字段
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();

                // 原始字段赋值
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                articleEsEntity
                        .setId(hit.getId())
                        .setArticleId(Long.valueOf(String.valueOf(sourceAsMap.get("articleId"))))
                        .setArticleTitle((String)(sourceAsMap.get("articleTitle")))
                        .setAuthorId(Long.valueOf(String.valueOf(sourceAsMap.get("authorId"))))
                        .setAuthorName((String)sourceAsMap.get("authorName"))
                        .setBookId(Long.valueOf(String.valueOf(sourceAsMap.get("bookId"))))
                        .setBookName((String)sourceAsMap.get("bookName"))
                        .setCommentCount((int)sourceAsMap.get("commentCount"))
                        .setContent((String)sourceAsMap.get("content"))
                        .setUserAvatar((String)sourceAsMap.get("userAvatar"))
                        .setTypeId(Long.valueOf(String.valueOf(sourceAsMap.get("typeId"))))
                        .setReadCount((int)sourceAsMap.get("readCount"))
                        .setUserId(Long.valueOf(String.valueOf(sourceAsMap.get("userId"))))
                        .setUserName((String)sourceAsMap.get("userName"))
                        .setFavoriteCount((int)sourceAsMap.get("favoriteCount"))
                        .setLikeCount((int)sourceAsMap.get("likeCount"))
                        .setPicUrl((String)sourceAsMap.get("picUrl"))
                        // TODO: 由于之前的date莫名其妙转成了text
                        // 导致这里出错
                        // .setPublishTime(sdf.parse((String)sourceAsMap.get("publishTime")))
                        .setPublishTime(new Date((Long) sourceAsMap.get("publishTime")))
                        .setTypeName((String)sourceAsMap.get("typeName"));
                //获取高亮字段
                Map<String, HighlightField> highlightFields = hit.getHighlightFields();
                //name
                articleEsEntity.setArticleTitle(sourceAsMap.get("articleTitle").toString());
                if (highlightFields.containsKey("articleTitle")) {
                    articleEsEntity.setArticleTitle(highlightFields.get("articleTitle").fragments()[0].toString());
                }
                // articleEsEntity.setArticleId()



                lists.add(articleEsEntity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }
}

