package ru.practicum.stat.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stat.dto.HitDto;
import ru.practicum.stat.model.Hit;


@UtilityClass
public class HitMapper {

    public static HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimeStamp())
                .build();
    }

    public static Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timeStamp(hitDto.getTimestamp())
                .build();
    }

//    public static Hit toHit(InputHitDto inputHitDto) {
//        return Hit.builder()
//                .id(0L)
//                .app(inputHitDto.getApp())
//                .uri(inputHitDto.getUri())
//                .ip(inputHitDto.getIp())
//                .timeStamp(inputHitDto.getTimestamp())
//                .build();
//    }




}
