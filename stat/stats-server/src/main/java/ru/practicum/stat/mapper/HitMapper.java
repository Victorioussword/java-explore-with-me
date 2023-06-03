package ru.practicum.stat.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.stat.dto.InputHitDto;
import ru.practicum.stat.dto.OutputHitDto;
import ru.practicum.stat.model.Hit;

@UtilityClass
public class HitMapper {

    public static Hit toHit(InputHitDto inputHitDto) {
        return new Hit(null,
                inputHitDto.getApp(),
                inputHitDto.getUri(),
                inputHitDto.getIp(),
                inputHitDto.getTimestamp());
    }


    public static OutputHitDto toOutputHitDto(Hit hit) {
        return new OutputHitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}