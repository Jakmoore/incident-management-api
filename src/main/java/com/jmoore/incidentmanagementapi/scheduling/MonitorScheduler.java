package com.jmoore.incidentmanagementapi.scheduling;

import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.repository.MonitorRepository;
import com.jmoore.incidentmanagementapi.service.MonitorExecuterService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MonitorScheduler {

    private final MonitorRepository monitorRepository;
    private final MonitorExecuterService executerService;

    @Scheduled(fixedDelay = 5000)
    public void dispatchMonitors() {
        List<Monitor> dueMonitors = monitorRepository.findByActiveTrueAndNextRunAtBefore(LocalDateTime.now());
        dueMonitors.forEach(executerService::runMonitor);
    }
}