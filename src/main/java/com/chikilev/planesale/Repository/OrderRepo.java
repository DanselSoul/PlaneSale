package com.chikilev.planesale.Repository;

import com.chikilev.planesale.Entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo  extends CrudRepository<OrderEntity, Integer> {

}
