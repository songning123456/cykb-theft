package com.sn.theft.repository;

import com.sn.theft.entity.Chapters;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author: songning
 * @date: 2020/3/9 23:00
 */
public interface ChaptersRepository extends JpaRepository<Chapters, String> {

    @Override
    Optional<Chapters> findById(String id);

    List<Chapters> findByChapterAndNovelsId(String chapter, String novelsId);
}
