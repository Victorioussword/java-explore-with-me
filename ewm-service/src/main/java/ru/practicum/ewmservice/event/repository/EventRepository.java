package ru.practicum.ewmservice.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {


    List<Event> findAllBy(Pageable pageable);

    List<Event> findAllByInitiatorId(long initiatorId, Pageable pageable);

    List<Event> findEventsByIdIn(List<Long> ids);

    Event findEventsById(long id);


    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE " +
            "(" +
            ":text IS NULL " +
            "OR LOWER(e.description) LIKE CONCAT('%', :text, '%') " +
            "OR LOWER(e.annotation) LIKE CONCAT('%', :text, '%')" +
            ")" +
            "AND (:states IS NULL OR e.state IN (:states)) " +
            "AND (:categories IS NULL OR e.category.id IN (:categories)) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (CAST(: rangeStartAS date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (CAST(:rangeEnd AS date) IS NULL OR e.eventDate <= :rangeEnd) " +
            "order by e.eventDate")
    List<Event> search(String text,
                       List<Long> categories,
                       Boolean paid,
                       LocalDateTime rangeStart,
                       LocalDateTime rangeEnd,
                       State states,
                       Pageable pageable);


//    @Query("SELECT e " +
//            "FROM Event e " +
//            "WHERE (lower(e.annotation) LIKE lower(concat('%', :text, '%')) OR lower(e.description) LIKE lower(concat('%', :text, '%')) OR :text IS NULL) " +
//            "AND (e.category.id IN :categories OR :categories IS NULL) " +
//            "AND (e.paid=:paid OR :paid IS NULL) " +
//            "AND (e.eventDate BETWEEN :startSearch AND :endSearch) " +
//            "AND (e.state = :state) " +
//            "ORDER BY e.eventDate")
//    List<Event> search2(String text,
//                       List<Long> categories,
//                       Boolean paid,
//                       LocalDateTime startSearch,
//                       LocalDateTime endSearch,
//                       State published,
//                       Pageable pageable);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(List<Long> users, List<State> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

}
