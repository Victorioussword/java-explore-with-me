package ru.practicum.ewmservice.request.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.request.model.Request;
import ru.practicum.ewmservice.request.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByRequesterIdAndEventId(long initiatorId, long eventId);

    @Query("SELECT request FROM Request request " +
            "WHERE request.event.id = :eventId AND request.status = 'CONFIRMED'")
    List<Request> findAllConfirmed(long eventId);

    List<Request> findAllByIdAndStatus(long id, RequestStatus status);

    Long countByEventIdAndStatus(Long eventId, RequestStatus status);

    @Query("SELECT r FROM Request r " +
            "WHERE r.event.id IN :eventId AND r.status = 'CONFIRMED'")
    List<Request> findAllConfirmedByEventIdIn(List<Long> eventId, Sort sort);

    List<Request> findRequestsByRequesterId(long requesterId);

    List<Request> findAllByEventAndStatus(Event event, RequestStatus requestStatus);

    @Query("select r from Request r " +
            "where r.status = 'CONFIRMED' " +
            "and r.event.id IN (:events)")
    List<Request> findConfirmedToListEvents(@Param("events") List<Long> events);
}
