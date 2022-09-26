package com.iamiagkov.reactiveapidemo.repository;

import com.iamiagkov.reactiveapidemo.entity.ProductEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCassandraRepository<ProductEntity, Integer> {
}
