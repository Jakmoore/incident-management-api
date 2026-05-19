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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorService {

    private final MonitorMapper mapper;
    private final MonitorRepository monitorRepository;

    @Transactional
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

        Monitor retrieved = getEntityById(id);

        return mapper.toResponse(retrieved);
    }

    @Transactional
    public MonitorResponseDto enableMonitor(Long id) {
        log.info("Processing enable monitor request for ID {}", id);

        return updateActive(id, true);
    }

    @Transactional
    public MonitorResponseDto disableMonitor(Long id) {
        log.info("Processing disable monitor request for ID {}", id);

        return updateActive(id, false);
    }

    @Transactional
    public MonitorResponseDto updateMonitorConfiguration(Long id, MonitorRequestDto request) {
        log.info("Processing update monitor configuration for ID: {}", id);

        Monitor retrieved = getEntityById(id);

        mapper.updateEntityFromDto(request, retrieved);
        monitorRepository.save(retrieved);

        return mapper.toResponse(retrieved);
    }

    @Transactional
    public void deleteMonitor(Long id) {
        log.info("Processing delete monitor request for ID: {}", id);

        Monitor ignored = getEntityById(id);

        monitorRepository.deleteById(id);
    }

    // Internal use only endpoints -------------------------------------------
    public Monitor getEntityById(Long id) {
        return monitorRepository.findById(id).orElseThrow(() -> new MonitorNotFoundException(id));
    }

    @Transactional
    public void updateNextRunAt(Monitor monitor) {
        monitor.setNextRunAt(calculateNextRunAt(monitor.getIntervalSeconds()));

        monitorRepository.save(monitor);
    }

    private MonitorResponseDto updateActive(Long id, boolean active) {
        Monitor retrieved = getEntityById(id);
        retrieved.setActive(active);

        monitorRepository.save(retrieved);

        return mapper.toResponse(retrieved);
    }

    private LocalDateTime calculateNextRunAt(int intervalSeconds) {
        return LocalDateTime.now().plusSeconds(intervalSeconds);
    }
}
