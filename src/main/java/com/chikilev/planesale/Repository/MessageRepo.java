package com.chikilev.planesale.Repository;

import com.chikilev.planesale.Entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<MessageEntity, Integer> {
    MessageEntity findByMessage(String message);
}
