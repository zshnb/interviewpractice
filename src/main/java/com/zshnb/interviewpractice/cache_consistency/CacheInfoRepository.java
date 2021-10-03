package com.zshnb.interviewpractice.cache_consistency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CacheInfoRepository extends JpaRepository<CacheInfo, Integer> {
    CacheInfo findByName(String name);
}
