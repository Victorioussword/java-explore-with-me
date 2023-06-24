package ru.practicum.request.repository;

import java.util.List;
import ru.practicum.request.model.Status;
import ru.practicum.request.model.Request;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {


    Long countByEventId(Long eventId);

    List<Request> findByIdIn(List<Long> ids);

    List<Request> findAllByEventId(Long eventId);

    List<Request> findAllByRequesterId(Long userId);

    Long countByEventIdAndStatus(Long eventId, Status status);

    List<Request> findAllByRequesterIdAndEventId(Long userId, Long eventId);
}