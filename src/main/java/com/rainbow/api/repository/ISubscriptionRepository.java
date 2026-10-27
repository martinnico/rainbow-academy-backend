package com.rainbow.api.repository;

import com.rainbow.api.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISubscriptionRepository extends JpaRepository<Subscription, Long> {
}
