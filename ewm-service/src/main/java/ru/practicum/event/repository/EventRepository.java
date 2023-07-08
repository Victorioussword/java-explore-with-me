package ru.practicum.event.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.utils.enums.State;

import java.time.LocalDateTime;
import java.util.List;


public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

    Event findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateIsAfterAndEventDateIsBefore(List<Long> users,
                                                                                                       List<State> states,
                                                                                                       List<Long> categories,
                                                                                                       LocalDateTime rangeStart,
                                                                                                       LocalDateTime rangeEnd,
                                                                                                       Pageable pageable);

    @Query("SELECT event " +
            "FROM Event event " +
            "WHERE (lower(event.annotation) LIKE lower(concat('%', :text, '%'))" +
            " OR lower(event.description) LIKE lower(concat('%', :text, '%')) OR :text IS NULL) " +
            "AND (event.category.id IN :categories OR :categories IS NULL) " +
            "AND (event.paid=:paid OR :paid IS NULL) " +
            "AND (event.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND (event.state = :state) " +
            "ORDER BY event.eventDate")
    List<Event> searchEventPub(String text,
                               List<Long> categories,
                               Boolean paid,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               State state,
                               Pageable pageable);

    Long countAllByCategoryId(long catId);

    List<Event> findAllBy(PageRequest pageRequest);

}
