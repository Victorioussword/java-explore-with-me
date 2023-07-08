package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.stat.dto.OutputHitDto;
import ru.practicum.stat.dto.ViewStatDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StatClient extends BaseClient {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final LocalDateTime START_DEFAULT =
            LocalDateTime.parse("1971-01-01 01:01:01", dateTimeFormatter);
    private static final LocalDateTime END_DEFAULT =
            LocalDateTime.parse("2999-01-01 01:01:01", dateTimeFormatter);

    @Autowired
    public StatClient(@Value("${stats-server-uri}") String serverUrl, RestTemplateBuilder builder) {

        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> saveStat(OutputHitDto hitDto) {
        return post("/hit", hitDto);
    }


    public List<ViewStatDto> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null) {
            start = START_DEFAULT;
        }
        if (end == null) {
            end = END_DEFAULT;
        }


        Map<String, Object> parameters = Map.of(
                "start", start.toString().replace("T", " "),
                "end", end.toString().replace("T", " "),
                "uris", uris,
                "unique", unique
        );
        ResponseEntity<Object> objectResponseEntity = get("/stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);


        List<ViewStatDto> viewStatDto = new ObjectMapper().convertValue(objectResponseEntity.getBody(), new TypeReference<List<ViewStatDto>>() {
        });

        log.info(" Body {}", objectResponseEntity.getBody());

        return viewStatDto;

    }
}
