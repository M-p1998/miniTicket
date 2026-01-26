import React from "react";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import { useAuth } from "../auth/AuthProvider";
import { timeAgo } from "../utils/timeAgo";
import "../styles/pills.css";
import "../styles/TicketDetailPage.css";


type Ticket = {
  id: number;
  subject: string;
  description: string;
  status: string;
  priority: string;
  createdBy: string;
  createdAt: string;
  closedBy?: string | null;
  closedAt?: string | null;
};

type Comment = {
  id: number;
  author: string;
  message: string;
  createdAt: string;
};

export default function TicketDetailPage() {
  const { id } = useParams();
  const { token } = useAuth();

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [comments, setComments] = useState<Comment[]>([]);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!token || !id) return;

    // Load ticket
    apiFetch<Ticket>(`${GATEWAY_BASE}/api/tickets/${id}`, token)
      .then(setTicket);

    // Load comments
    apiFetch<Comment[]>(
      `${GATEWAY_BASE}/api/comments?ticketId=${id}`,
      token
    ).then(setComments);
  }, [id, token]);

  const postComment = async () => {
    // if (!message.trim() || !token) return;
    if (!message.trim() || !token || ticket?.status === "CLOSED") return;

    const created = await apiFetch<Comment>(
      `${GATEWAY_BASE}/api/comments`,
      token,
      {
        method: "POST",
        body: JSON.stringify({
          ticketId: Number(id),
          message
        })
      }
    );

    setComments([...comments, created]);
    setMessage("");
  };

  const closeTicket = async () => {
  if (!token || !id) return;

  try {
    // 1) close ticket
    const updated = await apiFetch<Ticket>(
      `${GATEWAY_BASE}/api/tickets/${id}/status?status=CLOSED`,
      token,
      { method: "PATCH" }
    );

    setTicket(updated);

    // 2) add a "system comment"
    const created = await apiFetch<Comment>(
      `${GATEWAY_BASE}/api/comments`,
      token,
      {
        method: "POST",
        body: JSON.stringify({
          ticketId: Number(id),
          message: "closed the ticket"
        })
      }
    );

    setComments((prev) => [...prev, created]);

  } catch (e) {
    console.error(e);
    alert("Failed to close ticket");
  }
};


  if (!ticket) return <p>Loading...</p>;

  return (
  <div className="page">
    <div className="ticket-detail-card">
      
      <div className="ticket-header">
        <h2>#{ticket.id}: {ticket.subject}</h2>

        {ticket.status === "OPEN" ? (
          <button className="actionBtn danger" onClick={closeTicket}>
            Close Ticket
          </button>
        ) : (
          <span className="pill closed">CLOSED</span>
        )}
      </div>

      <div>
        <strong>Description</strong>
        <div className="ticket-description">
          {ticket.description?.trim() ? ticket.description : "—"}
        </div>
      </div>

      <div className="detailRow">
        <span>Status:</span>
        <span className={`pill ${ticket.status.toLowerCase()}`}>
          {ticket.status}
        </span>
      </div>

      <div className="detailRow">
        <span>Priority:</span>
        <span className={`pill ${ticket.priority.toLowerCase()}`}>
          {ticket.priority}
        </span>
      </div>

      <div className="detailRow">
        <span>Created by:</span>
        <span>{ticket.createdBy}</span>
      </div>

      <div className="detailRow">
        <span>Created:</span>
        <span>{timeAgo(ticket.createdAt)}</span>
      </div>

      <hr />

      <div className="comments">
        <h3>Comments</h3>

        <textarea
          placeholder={
            ticket.status === "CLOSED"
              ? "Ticket is closed. Comments are disabled."
              : "Post a comment..."
          }
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          disabled={ticket.status === "CLOSED"}
        />

        <button
          className="postBtn"
          onClick={postComment}
          disabled={ticket.status === "CLOSED"}
        >
          Post
        </button>

        {comments.map((c) => (
          <div key={c.id} style={{ marginTop: 12 }}>
            <b>{c.author}</b> · {timeAgo(c.createdAt)}
            <p>{c.message}</p>
          </div>
        ))}
      </div>

    </div>
  </div>
);

}
