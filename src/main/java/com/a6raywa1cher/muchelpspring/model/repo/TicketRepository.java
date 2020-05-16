package com.a6raywa1cher.muchelpspring.model.repo;

import com.a6raywa1cher.muchelpspring.model.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
}
