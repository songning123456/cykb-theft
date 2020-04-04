package com.sn.theft.entity;

import com.sn.theft.annotation.AClassConvert;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: songning
 * @date: 2020/3/9 22:30
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "Chapters")
@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
public class Chapters {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    @Column(length = 128)
    @AClassConvert(fieldName = "chaptersId")
    private String id;

    @Column(name = "chapter", columnDefinition = "VARCHAR(128) NOT NULL COMMENT '章节'")
    private String chapter;

    @Column(name = "content", columnDefinition = "TEXT NOT NULL COMMENT '文本内容'")
    private String content;

    @Column(name = "novelsId", columnDefinition = "VARCHAR(32) NOT NULL COMMENT 'novels_id'")
    private String novelsId;

    @Column(name = "updateTime", columnDefinition = "DATETIME NOT NULL COMMENT '更新时间'")
    private Date updateTime;
}
