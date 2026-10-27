package com.rainbow.api.repository;

import com.rainbow.api.entity.Ebook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEbookRepository extends JpaRepository<Ebook, Long> {
}
