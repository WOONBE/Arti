package com.d106.arti.gallery.repository;

import com.d106.arti.gallery.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {
    Optional<Subscription> findByIdAndIsDeletedFalse(Integer subscriptionId);

}
