package com.sn.theft.repository;

import com.sn.theft.entity.Novels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {

    @Query(value = "select * from novels where source_name = ?1 order by create_time desc limit ?2", nativeQuery = true)
    List<Novels> findFirstClassifyNative(String sourceName, int size);

    List<Novels> findBySourceUrl(String sourceUrl);
}
