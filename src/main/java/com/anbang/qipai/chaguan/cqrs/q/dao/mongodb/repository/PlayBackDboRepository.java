package com.anbang.qipai.chaguan.cqrs.q.dao.mongodb.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.anbang.qipai.chaguan.cqrs.q.dbo.PlayBackDbo;

public interface PlayBackDboRepository extends MongoRepository<PlayBackDbo, String> {

}
