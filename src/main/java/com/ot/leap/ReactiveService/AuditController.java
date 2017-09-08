package com.ot.leap.ReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/**
 * Created by ganesr1 on 9/8/17.
 */
@RestController
public class AuditController {

    @Autowired
    AuditEventService auditEventService;

    @GetMapping("/audits/{id}")
    Mono<AuditEvent> getById(@PathVariable String id){
        return auditEventService.getEventById(id);
    }

    @GetMapping(value = "/audits", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<AuditEvent> getAllEvents(@RequestParam Optional<Long> duration, @RequestParam Optional<Long> maxCount){
        return auditEventService.getEvents(duration, maxCount);
    }
}
