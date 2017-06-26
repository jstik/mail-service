package com.dao;

import com.model.entity.MailItem;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Julia on 08.06.2017.
 */

@Repository
public interface MailItemRepository extends QueryDslPredicateExecutor<MailItem>, PagingAndSortingRepository<MailItem, Long> {
    MailItem findByUuid(String uuid);
}
