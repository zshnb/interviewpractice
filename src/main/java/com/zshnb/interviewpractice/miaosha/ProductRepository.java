package com.zshnb.interviewpractice.miaosha;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("update Product set stock = stock - 1 where stock > 0 and id = :id")
    @Modifying(clearAutomatically = true)
    int updateStock(int id);

    Product findByName(String name);
}
