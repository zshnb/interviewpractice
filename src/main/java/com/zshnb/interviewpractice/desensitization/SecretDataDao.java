package com.zshnb.interviewpractice.desensitization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SecretDataDao extends JpaRepository<SecretData, Integer> {
    SecretData findByUser(User user);
}
