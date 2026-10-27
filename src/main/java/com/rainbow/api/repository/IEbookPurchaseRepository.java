package com.rainbow.api.repository;

import com.rainbow.api.entity.EbookPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEbookPurchaseRepository extends JpaRepository<EbookPurchase, Long> {
    boolean existsByStudentIdAndEbookId(Long studentId, Long ebookId);
}
