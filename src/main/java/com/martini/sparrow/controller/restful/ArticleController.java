package com.martini.sparrow.controller.restful;

import cn.hutool.core.date.DateField;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.ssh.Sftp;
import cn.hutool.http.HtmlUtil;
import com.martini.sparrow.elastic.entity.ArticleEsEntity;
import com.martini.sparrow.elastic.repository.ArticleEsEntityRepository;
import com.martini.sparrow.entity.Article;
import com.martini.sparrow.entity.Message;
import com.martini.sparrow.entity.User;
import com.martini.sparrow.service.ArticleService;
import com.martini.sparrow.service.MessageService;
import com.martini.sparrow.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author martini at 2020/12/15 8:14
 */
@CrossOrigin
@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public Object findAll() {
        return articleService.findAll();
    }

    @PostMapping("/add")
    public Object postArticle(@RequestBody Map<String, String> form, HttpServletResponse response) {
        log.info(form.toString());
        // TODO: 是否有必要向上抛出的时候设置响应码
        // TODO: 选择在异常中处理上述问题
        // TODO: 接口验证
        Article article = new Article();
        String userId = form.get("userId");
        String bookId = form.get("bookId");
        String title = form.get("title");
        String content = form.get("content");
        String picUrl = form.get("picUrl");
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(bookId) || StringUtils.isEmpty(title) || StringUtils.isEmpty(content)) {
            // TODO: 接口抽离
            response.setStatus(402);
            return null;
        }
        article.setUserId(Long.valueOf(userId)).
                setBookId(Long.valueOf(bookId)).
                setTitle(title).
                setContent(content).
                setPublishTime(new Date()).
                setPicUrl(picUrl).
                setLikeCount(0).
                setFavoriteCount(0).
                setCommentCount(0).
                setReadCount(0).
                setStatus(1); // 默认上传的状态为未审核
        articleService.postOne(article);
        messageService.pushMessage(
                article.getUserId(),
                new Message()
                        .setId(IdUtil.simpleUUID())
                        .setDatetime(new Date())
                        .setTitle("消息通知：文章待审核")
                        .setContent("您的文章《" + article.getTitle() + "》已经发表，请等待审核"));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Long id) {
        return articleService.findById(id);
    }


    /**
     * TODO: 审核通过，向ES中插入数据
     *       删除文章，ES中同步删除
     *       更新文章，ES中同步更新
     * @param id 修改的文章id
     * @param fields 更新的字段
     * @return
     */
    @PutMapping(value = "/{id}")
    public Object updateStatus(@PathVariable("id") Long id, @RequestBody Map<String, String> fields) {
        Article article = new Article();
        if (fields.get("status") != null) {
            // TODO: 增加方法
            article.setStatus(Integer.valueOf(fields.get("status")));
        }
        return articleService.updateById(id, article);
    }

    @PutMapping(value = "/approve/{id}")
    public Object approve(@PathVariable("id") Long id) {
        return articleService.approve(id);
    }

    @PostMapping("/image")
    public Object uploadAvatar(@RequestParam("file") MultipartFile uploadFiles) {
        // TODO: 路径需要外部properties注入
        // TODO: SFTP -> FTP
        // TODO: upload 路径
        Sftp sftp = new Sftp("192.168.1.100", 22, "bouts", "120309");
        String name = IdUtil.fastSimpleUUID();
        try {
            name = name + "." + FileUtil.getSuffix(uploadFiles.getResource().getFilename());
            File file = new File(name);
            uploadFiles.transferTo(file);
            sftp.upload("/public/upload_images/article_pics", file);
            file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            sftp.close();
        }
        return "http://192.168.1.100/article_pics/" + name;
    }

    @PutMapping(value = "/disapprove/{id}")
    public Object disapprove(@PathVariable("id") Long id) {
        return articleService.disapprove(id);
    }

    @GetMapping("/count")
    public Object getCount() {
        return articleService.count();
    }

    @GetMapping("/limit")
    public Object findLimit(@RequestParam Map<String, String> params, HttpServletResponse response) {
        // 检查参数
        if (params.get("page") == null || params.get("limit") == null) {
            // TODO: 401
            response.setStatus(401);
            return null;
        }
        Integer page = Integer.parseInt(params.get("page"));
        Integer limit = Integer.parseInt(params.get("limit"));
        return articleService.findLimit(page, limit);
    }

    @GetMapping("/uid/{uid}")
    public Object findByUserId(@PathVariable("uid") Long uid) {
        return articleService.findByUserId(uid);
    }


    @Autowired
    private ArticleEsEntityRepository articleEsEntityRepository;
    @GetMapping(value = "/generate")
    public Object generate() {
        List<String> titles = Arrays.asList(
                "世界上最短的婚姻，只有三分钟",
                "证实确实在外找小姐，我竟然觉得舒坦了",
                "今年我读了四个开源项目的源码，来分享下心得"
        );
        List<String> bookNames = Arrays.asList(
                "红楼梦",
                "西游记",
                "三国演义",
                "水浒传"
        );
        List<String> contents = Arrays.asList(
                "老公忽然产生纳妾的念头，便与妻子商量，没想到妻子非常爽快地说：“只要你先给我一笔钱，我不但同意，还会帮你物色一个最好的妾来。” 于是老公给了妻子...",
                "Hola，我是 yes。 今年来看了 RocketMQ、Kafka、Dubbo 、Tomcat 的源码，之前也有读者询问过如何读源码，索性就来分...",
                "人的一生是一场漫长而坎坷的旅程，多去汲取别人人生的经验，我们在路途上就能少走些弯路，受益终生。 杨绛先生的这4句话，字字都是珠玑，读懂了它们，就...",
                "1.我天生不合群。 一向话少，时而冷场。 有过被孤立，有过被诟病。 有过自我质疑，也有到过崩坏的边缘。 合也无味，孤也无味。 党同伐异，这是人性..."
        );
        List<String> bookImgs = Arrays.asList(
                "s1034062.jpg",
                "s1070222.jpg",
                "s1070959.jpg",
                "s1070960.jpg",
                "s1076932.jpg"
        );
        List<String> typeNames = Arrays.asList(
                "中国文学",
                "外国文学",
                "心理学",
                "人文科学",
                "艺术品鉴"
        );
        for (int i = 0; i < 10; i++) {
            ArticleEsEntity articleEsEntity = new ArticleEsEntity();
            articleEsEntity.setArticleId(1L)
                    .setArticleTitle(titles.get(i % 3))
                    .setAuthorId(1L)
                    .setAuthorName("曹雪芹")
                    .setBookId(1L)
                    .setBookName(bookNames.get(i % 4))
                    .setCommentCount(RandomUtil.randomInt(0, 100))
                    .setContent(StrUtil.sub(HtmlUtil.cleanHtmlTag(contents.get(i % 4)), 0, 100))
                    .setFavoriteCount(RandomUtil.randomInt(0, 100))
                    .setLikeCount(RandomUtil.randomInt(0, 100))
                    .setReadCount(RandomUtil.randomInt(0, 100))
                    .setUserAvatar("http://m.imeitou.com/uploads/allimg/2020110210/da4rsi0mlkj-lp.jpg")
                    // 自定义
                    .setPicUrl("http://192.168.1.100/book_covers/" + bookImgs.get(i % 5))
                    .setPublishTime(RandomUtil.randomDate(new Date(), DateField.DAY_OF_MONTH, -15, 0))
                    // .setPublishTime(new Date())
                    .setTypeId(1L)
                    .setTypeName(typeNames.get(i % 5))
                    // TODO: 这里
                    .setUpdateTime(null)
                    .setUserId(1L)
                    .setUserName("不来不去")
                    .setId(IdUtil.fastSimpleUUID());
            articleEsEntityRepository.save(articleEsEntity);
        }
        return null;
    }


    @GetMapping("/read-count/{id}")
    public Object addReadCount(@PathVariable("id") Long id) {
        int number = RandomUtil.randomInt(10, 20);
        Iterator<ArticleEsEntity> foundArticleIterator = articleEsEntityRepository.search(QueryBuilders.matchQuery("articleId", id)).iterator();
        if (foundArticleIterator.hasNext()) {
            ArticleEsEntity foundArticle = foundArticleIterator.next();
            foundArticle.setReadCount(foundArticle.getReadCount() + number);
            articleEsEntityRepository.save(foundArticle);
        }
        articleService.addReadCount(id, number);
        return null;
    }

    /**
     * 获取所有已经过审的文章的总数
     * 然后随机挑选size个id
     * @param size
     * @return
     */
    @GetMapping("/random")
    public Object randomArticles(@RequestParam("size") Integer size) {
        Integer allApprovedSize = articleService.approvedCount();
        Set<Long> ids = new HashSet<>();
        for (Integer i = 0; i < size; i++) {
            ids.add(RandomUtil.randomLong(1, allApprovedSize));
        }
        List<Article> articles = new ArrayList<>();
        for (Long id : ids) {
            articles.add(articleService.findById(id));
        }
        return articles;
    }

    /**
     * 文章点赞/取消点赞功能
     * redis: article:[id]:likes
     * @param map 用户id，文章id
     * @return
     */
    @PostMapping("/toggle-like")
    public Object likeArticle(@RequestBody Map<String, Object> map, HttpServletResponse response) {
        String uid = map.get("uid").toString();
        String aid = map.get("aid").toString();
        Article article = articleService.findById(Long.parseLong(aid));
        User articleUser = userService.findById(article.getUserId());
        User likeUser = userService.findById(Long.parseLong(uid));
        if (redisTemplate.opsForSet().isMember("article:" + aid + ":likes", uid)) {
            redisTemplate.opsForSet().remove("article:" + aid + ":likes", uid);
            articleService.cancelLikeArticle(Long.parseLong(aid));

            // TODO 冗余
            Iterator<ArticleEsEntity> foundArticleIterator = articleEsEntityRepository.search(QueryBuilders.matchQuery("articleId", article.getId())).iterator();
            if (foundArticleIterator.hasNext()) {
                ArticleEsEntity foundArticle = foundArticleIterator.next();
                foundArticle.setLikeCount(foundArticle.getLikeCount() - 1);
                articleEsEntityRepository.save(foundArticle);
            }
        } else {
            redisTemplate.opsForSet().add("article:" + aid + ":likes", uid);
            articleService.likeArticle(Long.parseLong(aid));
            messageService.pushMessage(
                    articleUser.getUid(),
                    new Message()
                    .setId(IdUtil.simpleUUID())
                    .setDatetime(new Date())
                    .setTitle("消息通知：您的点赞")
                    .setContent(
                            "<a href='/user/" +
                                    likeUser.getUid() +
                                    "' target='_blank'>" + likeUser.getNickname() +
                                    "</a>给您的文章：<a target='_blank' href='/article/" + article.getId() + "'>《" +
                                    article.getTitle() + "》</a>点赞"));

            // TODO 冗余
            Iterator<ArticleEsEntity> foundArticleIterator = articleEsEntityRepository.search(QueryBuilders.matchQuery("articleId", article.getId())).iterator();
            if (foundArticleIterator.hasNext()) {
                ArticleEsEntity foundArticle = foundArticleIterator.next();
                foundArticle.setLikeCount(foundArticle.getLikeCount() + 1);
                articleEsEntityRepository.save(foundArticle);
            }
        }
        return null;
    }

    /**
     * 是否点赞了文章
     * @return
     */
    @GetMapping("/is-like")
    public Object isLike(@RequestParam Map<String, Object> map, HttpServletResponse response) {
        String uid = map.get("uid").toString();
        String aid = map.get("aid").toString();
        return redisTemplate.opsForSet().isMember("article:" + aid + ":likes", uid);
    }
}
