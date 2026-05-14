package com.jmoore.incidentmanagementapi.service;

import com.jmoore.incidentmanagementapi.exception.MonitorNotFoundException;
import com.jmoore.incidentmanagementapi.mapper.MonitorMapper;
import com.jmoore.incidentmanagementapi.model.dto.MonitorRequestDto;
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

    public MonitorResponseDto createMonitor(MonitorRequestDto request) {
        log.info("Processing create monitor request for URL: {}", request.getUrl());

        Monitor toCreate = mapper.toEntity(request);
        toCreate.setActive(true);
        toCreate.setNextRunAt(calculateNextRunAt(request.getIntervalSeconds()));

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

    public MonitorResponseDto enableMonitor(Long id) {
        log.info("Processing enable monitor request for ID {}", id);

        return updateActive(id, true);
    }

    public MonitorResponseDto disableMonitor(Long id) {
        log.info("Processing disable monitor request for ID {}", id);

        return updateActive(id, false);
    }

    public MonitorResponseDto updateMonitorConfiguration(Long id, MonitorRequestDto request) {
        log.info("Processing update monitor configuration for ID: {}", id);

        Monitor retrieved = monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));

        mapper.updateEntityFromDto(request, retrieved);
        monitorRepository.save(retrieved);

        return mapper.toResponse(retrieved);
    }

    public void deleteMonitor(Long id) {
        log.info("Processing delete monitor request for ID: {}", id);

        monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));
        monitorRepository.deleteById(id);
    }

    // Internal use only endpoints -------------------------------------------
    private MonitorResponseDto updateActive(Long id, boolean active) {
        Monitor retrieved = monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));
        retrieved.setActive(active);

        monitorRepository.save(retrieved);

        return mapper.toResponse(retrieved);
    }

    public Monitor getEntityById(Long id) {
        return monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));
    }

    public void updateNextRunAt(Monitor monitor) {
        monitor.setNextRunAt(calculateNextRunAt(monitor.getIntervalSeconds()));

        monitorRepository.save(monitor);
    }

    private LocalDateTime calculateNextRunAt(int intervalSeconds) {
        return LocalDateTime.now().plusSeconds(intervalSeconds);
    }
}
