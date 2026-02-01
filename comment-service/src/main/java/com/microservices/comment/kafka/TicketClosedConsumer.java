import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.microservices.comment.service.CommentService;

@Component
public class TicketClosedConsumer {

  private final CommentService commentService;

  public TicketClosedConsumer(CommentService commentService) {
    this.commentService = commentService;
  }

  @KafkaListener(topics = "ticket.closed", groupId = "comment-service")
  public void onTicketClosed(TicketClosedEvent event) {
    commentService.createSystemComment(
        event.ticketId(),
        event.closedBy() + " closed the ticket"
    );
  }
}
