package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatClient;
import ru.practicum.stat.dto.OutputHitDto;
import ru.practicum.stat.dto.ViewStatDto;

import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticService {

    @Value("${ewm.service.name}")
    String appName;

    public Long getViews(LocalDateTime rangeStart,
                                LocalDateTime rangeEnd,
                                String uris,
                                Boolean unique,
                                StatClient statClient) {
        List<ViewStatDto> viewStatDtos = statClient.getStat(rangeStart, rangeEnd, List.of(uris), unique);

       log.info("StatisticService - getViews возвращено viewStatDtos {}", viewStatDtos.toString());
       if (viewStatDtos.size() > 0) {

            return viewStatDtos.get(0).getHits();
      } else {
            return 0L;
        }
    }


    public Map<Long, Long> getViews2(LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            List<String> uris,
                                            Boolean unique,
                                            StatClient statClient) {


        List<ViewStatDto> viewStatDtos = statClient.getStat(rangeStart, rangeEnd, uris, unique);
        Map<Long, Long> idAndViews = new HashMap<>();

        for (int i = 0; i < viewStatDtos.size(); i++) {
            List<String> stroki = new ArrayList<>();
            String[] str = viewStatDtos.get(i).getUri().split("/");
            Collections.addAll(stroki, str);
            Long id = Long.parseLong(stroki.get(stroki.size() - 1));
            idAndViews.put(id, viewStatDtos.get(i).getHits());
        }

        log.info("getViews возвращено viewStatDtos {}", viewStatDtos.toString());

        return idAndViews;
    }


    public void saveHit(String ip, Long eventId, StatClient statClient) {



        OutputHitDto outputHitDto = new OutputHitDto();
        outputHitDto.setApp(appName); // было  outputHitDto.setApp("ewm-service")

        String timestamp = LocalDateTime.now().withNano(0).toString().replace("T", " ");
        outputHitDto.setTimestamp(timestamp);
        outputHitDto.setIp(ip);
        if (eventId == null) {
            outputHitDto.setUri("/events");
        } else {
            outputHitDto.setUri("/events/" + eventId);
        }
        statClient.saveStat(outputHitDto);
        log.info("saveHit(). Сохранено успешно");
    }

}
