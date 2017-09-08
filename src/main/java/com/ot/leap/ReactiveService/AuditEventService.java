package com.ot.leap.ReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by ganesr1 on 9/8/17.
 */
@Service
@EnableScheduling
public class AuditEventService {

    private ConcurrentHashMap<String, AuditEvent> eventsMap = new ConcurrentHashMap<String, AuditEvent>();

    @PostConstruct
    void init() {

    }

    String[] userNames = new String[] { "Alex@ngis.com", "John@ngis.com", "Ram@ngis.com", "Peter@ngis.com" };
    String[] eventType = new String[] { "Created", "Updated", "Deleted" };

    @Scheduled(fixedDelay = 500)
    public void scheduleAuditCreation() {
        int randomUser = ThreadLocalRandom.current().nextInt(0, 4);
        int randomEvent = ThreadLocalRandom.current().nextInt(0, 2);

        String id = UUID.randomUUID().toString();
        AuditEvent event =
                new AuditEvent(id, userNames[randomUser], "Object " + UUID.randomUUID().toString() + " " + eventType[randomEvent],
                        new Date());
        eventsMap.put(id, event);
    }

    public Mono<AuditEvent> getEventById(String id) {
        return Mono.just(eventsMap.get(id));
    }

    public Flux<AuditEvent> getEvents(Optional<Long> duration, Optional<Long> maxCount) {
        Flux<AuditEvent> events = Flux.fromIterable(eventsMap.values());
        long time = duration.isPresent() ? duration.get() : 1;
        long max = maxCount.isPresent() ? maxCount.get() : Long.MAX_VALUE;
        Flux<Long> timeFlux = Flux.interval(Duration.ofSeconds(time));
        return Flux.zip(events, timeFlux)
                .map(Tuple2::getT1).take(max);
    }
}
