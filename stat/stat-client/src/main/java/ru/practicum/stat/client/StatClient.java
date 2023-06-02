package ru.practicum.stat.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

import ru.practicum.stat.dto.InputHitDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;


public class StatClient  extends BaseClient {

    private static final String API_PREFIX = "/hit";
private static final String FORMATTER  = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    public StatClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(String app, String uri, String ip, LocalDateTime timestamp) {
        InputHitDto inputHitDto = new InputHitDto(app, uri, ip, timestamp);
        return post("/hit", inputHitDto);
    }

    public ResponseEntity<Object> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        if (start == null || end == null || start.isAfter(end)) {
            throw new IllegalArgumentException("Недопустимый временной промежуток.");
        }

        StringBuilder uriBuilder = new StringBuilder("/stats?start={start}&end={end}");
        Map<String, Object> parameters = Map.of(
                "start", start.format(DateTimeFormatter.ofPattern(FORMATTER)),
                "end", end.format(DateTimeFormatter.ofPattern(FORMATTER))
        );

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                uriBuilder.append("&uris=").append(uri);
            }
        }
        if (unique != null) {
            uriBuilder.append("&unique=").append(unique);
        }

        return get(uriBuilder.toString(), parameters);
    }
}