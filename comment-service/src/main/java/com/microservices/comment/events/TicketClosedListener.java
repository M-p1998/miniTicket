package com.microservices.comment.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TicketClosedListener {

  @KafkaListener(topics = "ticket.closed", groupId = "comment-service")
  public void onTicketClosed(Object event) {
    log.info("✅ Received ticket.closed event: {}", event);
  }
}
