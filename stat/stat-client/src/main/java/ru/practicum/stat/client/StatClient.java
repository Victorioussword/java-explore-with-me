package ru.practicum.stat.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.stat.dto.InputHitDto;
import ru.practicum.stat.dto.ViewStat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {

    private static final String API_PREFIX = "/hit";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Autowired
    public StatClient(RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090" + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(InputHitDto inputHitDto) {
        //      InputHitDto inputHitDto = new InputHitDto(app, uri, ip, timestamp);
        return post("/hit", inputHitDto);
    }

    public List<ViewStat> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null) {
            start = LocalDateTime.parse("1971-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        if (end == null) {
            end = LocalDateTime.parse("2999-12-31 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
        List<ViewStat> viewStatsDto = new ObjectMapper().convertValue(objectResponseEntity.getBody(), new TypeReference<List<ViewStat>>() {
        });
        return viewStatsDto;
    }
}