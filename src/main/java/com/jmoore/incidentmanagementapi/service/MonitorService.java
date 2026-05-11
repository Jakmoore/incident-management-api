package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.exception.MonitorNotFoundException;
import com.jmoore.incidentmanagementapi.mapper.MonitorMapper;
import com.jmoore.incidentmanagementapi.model.dto.CreateMonitorRequestDto;
import com.jmoore.incidentmanagementapi.model.dto.MonitorResponseDto;
import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorMapper mapper;
    private final MonitorRepository monitorRepository;

    public MonitorResponseDto createMonitor(CreateMonitorRequestDto request) {
        log.info("Processing create monitor request for URL: {}", request.getUrl());

        Monitor toCreate = mapper.toEntity(request);
        toCreate.setActive(true);
        toCreate.setNextRunAt(LocalDateTime.now().plusSeconds(toCreate.getIntervalSeconds()));

        Monitor saved = monitorRepository.save(toCreate);

        return mapper.toResponse(saved);
    }

    public List<MonitorResponseDto> getAll() {
        log.info("Processing get all monitors request");
        return monitorRepository.findAll().stream().map(mapper::toResponse).toList();
    }

    public MonitorResponseDto getById(Long id) {
        log.info("Processing get monitor request for ID: {}", id);
        Monitor retrieved = monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));

        return mapper.toResponse(retrieved);
    }
}
