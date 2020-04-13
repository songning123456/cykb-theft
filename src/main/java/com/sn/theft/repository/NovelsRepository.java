package com.sn.theft.repository;

import com.sn.theft.entity.Novels;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: songning
 * @date: 2020/3/9 23:01
 */
public interface NovelsRepository extends JpaRepository<Novels, String> {

    List<Novels> findBySourceUrl(String sourceUrl);
}
