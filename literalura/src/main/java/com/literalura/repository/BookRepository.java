package com.literalura.repository;

import com.literalura.entity.BookEntity;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;



public interface BookRepository extends JpaRepository<BookEntity, Long> {

    BookEntity findByTitle(String title);

    List<BookEntity> findByOrderByDownloadCountDesc();
}
