package com.jmoore.incidentmanagementapi.scheduling;

import com.jmoore.incidentmanagementapi.model.entity.Monitor;
import com.jmoore.incidentmanagementapi.repository.MonitorRepository;
import com.jmoore.incidentmanagementapi.service.MonitorExecutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Profile("!dev")
@RequiredArgsConstructor
public class MonitorScheduler {

    private final MonitorRepository monitorRepository;
    private final MonitorExecutorService executerService;

    @Scheduled(fixedDelay = 30000)
    public void dispatchMonitors() {
        List<Monitor> dueMonitors = monitorRepository.findByActiveTrueAndNextRunAtBefore(LocalDateTime.now());
        dueMonitors.forEach(executerService::runMonitor);
    }
}