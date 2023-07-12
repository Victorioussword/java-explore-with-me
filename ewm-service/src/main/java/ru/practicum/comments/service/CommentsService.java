package ru.practicum.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ru.practicum.comments.dto.CommentDtoInput;
import ru.practicum.comments.dto.CommentDtoOutput;
import ru.practicum.comments.dto.CommentDtoUpdate;
import ru.practicum.comments.mapper.CommentMapper;
import ru.practicum.comments.model.Comment;
import ru.practicum.comments.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.ConflictException;
import ru.practicum.exceptions.ObjectNotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CommentsService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    public CommentDtoOutput create(CommentDtoInput commentDtoInput, Long creatorId, Long eventId) {
        Comment comment = CommentMapper.toComment(commentDtoInput);

        Optional<User> optionalUser = userRepository.findById(creatorId);

        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalUser.isEmpty()) {
            throw new ObjectNotFoundException("User не найден");
        }

        if (optionalEvent.isEmpty()) {
            throw new ObjectNotFoundException("Event не найден");
        }

        comment.setCreator(optionalUser.get());
        comment.setEvent(optionalEvent.get());

        CommentDtoOutput commentDtoOutput = CommentMapper.toCommentDtoOutput(commentRepository.save(comment));
        log.info("CommentsService - create().  Создан комментарий {}.", commentDtoOutput.toString());

        return commentDtoOutput;
    }


    public void delComment(Long commentId, Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User не найден");
        }
        commentRepository.deleteById(commentId);
        log.info("CommentsService - delComment().  удален комментарий {}.", commentId);
    }

    public List<CommentDtoOutput> getComments(Long eventId, int from, int size) {

        Optional<Event> optionalEvent = eventRepository.findById(eventId);

        if (optionalEvent.isEmpty()) {
            throw new ObjectNotFoundException("Event не найден");
        }

        List<Comment> comments = commentRepository.findAllByEvent(optionalEvent.get(), PageRequest.of(from, size));
        log.info("CommentsService - getComments().  Возвращен список {}.", comments.size());

        return comments.stream().map(CommentMapper::toCommentDtoOutput).collect(Collectors.toList());

    }

    public CommentDtoOutput update(CommentDtoUpdate commentDtoUpdate, Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ObjectNotFoundException("Комментарий с Id = " + commentId + " не обнаружен"));
        if (!userRepository.existsById(userId)) {
            throw new ObjectNotFoundException("User не найден");
        }

        if (comment.getCreator().getId() != userId) {
            throw new ConflictException("User не может изменить комментарий");
        }
        comment.setText(commentDtoUpdate.getText());
        comment.setUpdateTime(LocalDateTime.now());

        log.info("CommentsService - update().  Обновлен комментарий {}.", comment.toString());
        return CommentMapper.toCommentDtoOutput(comment);
    }

    public List<CommentDtoOutput> getCommentsByUser(Long userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("USer с Id = " + userId + " не обнаружен"));
        List<Comment> comments = commentRepository.findAllByCreator(user, PageRequest.of(from, size));
        log.info("CommentsService - getCommentsByUser().  Возвращен список {}.", comments.size());
        return comments.stream().map(CommentMapper::toCommentDtoOutput).collect(Collectors.toList());
    }
}