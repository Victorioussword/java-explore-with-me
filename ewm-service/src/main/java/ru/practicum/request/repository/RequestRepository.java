package ru.practicum.request.repository;

import java.util.List;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import ru.practicum.event.model.CountRequests;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {


    Long countByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> ids);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Long countByEventIdAndStatus(Long eventId, Status status);

    List<Request> findAllByRequesterIdAndEventId(Long userId, Long eventId);


    @Query("select new ru.practicum.event.model.CountRequests(request.event.id, count (request.id))" +
            "from Request request " +
            "where request.event in  (:eventIds)" +
            "and request.status = (:status)" +
            "group by request.id")
    List<CountRequests> getCountOfRequests(List<Event> eventIds, Status status);
}