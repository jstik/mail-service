package com.dao;

import com.model.entity.MailStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Julia on 07.06.2017.
 */
@Repository
public interface MailStatusRepository extends PagingAndSortingRepository<MailStatus, Long> {
}
