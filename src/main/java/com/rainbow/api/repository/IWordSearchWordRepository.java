package com.rainbow.api.repository;

import com.rainbow.api.entity.WordSearchWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWordSearchWordRepository extends JpaRepository<WordSearchWord, Long> {
}
