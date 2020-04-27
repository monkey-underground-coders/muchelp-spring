package com.a6raywa1cher.muchelpspring.model.repo;

import com.a6raywa1cher.muchelpspring.model.Ticket;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TicketRepository extends PagingAndSortingRepository<Ticket, Long> {
}
