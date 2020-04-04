package com.sn.theft.entity;

import com.sn.theft.annotation.AClassConvert;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: songning
 * @date: 2020/3/9 22:17
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "Novels")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Novels {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 32)
    @AClassConvert(fieldName = "novelsId")
    private String id;

    @Column(name = "title", columnDefinition = "VARCHAR(64) NOT NULL COMMENT '书名' default '未知书名'")
    private String title;

    @Column(name = "author", columnDefinition = "VARCHAR(64) NOT NULL COMMENT '作者' default '未知作者'")
    private String author;

    @Column(name = "category", columnDefinition = "VARCHAR(24) NOT NULL default 'dushi' COMMENT '类别(玄幻/都市)'")
    private String category;

    @Column(name = "introduction", columnDefinition = "TEXT COMMENT '简介'")
    private String introduction;

    @Column(name = "latestChapter", columnDefinition = "VARCHAR(64) NOT NULL default '暂无' COMMENT '最新章节'")
    @AClassConvert(fieldName = "chapter")
    private String latestChapter;

    @Column(name = "coverUrl", columnDefinition = "VARCHAR(128) default '暂无' COMMENT '封面'")
    private String coverUrl;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;

    @Column(name = "createTime", columnDefinition = "BIGINT NOT NULL COMMENT '创建时间'")
    private Long createTime;

    @Column(name = "sourceUrl", columnDefinition = "VARCHAR(128) default '暂无' COMMENT '来源url'")
    private String sourceUrl;

    @Column(name = "sourceName", columnDefinition = "VARCHAR(128) default '暂无' COMMENT '来源url'")
    private String sourceName;
}
