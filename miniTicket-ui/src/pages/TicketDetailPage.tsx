import React from "react";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { apiFetch, GATEWAY_BASE } from "../api/http";
import { useAuth } from "../auth/AuthProvider";
import { timeAgo } from "../utils/timeAgo";


type Ticket = {
  id: number;
  subject: string;
  description: string;
  status: string;
  priority: string;
  createdBy: string;
  createdAt: string;
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
    if (!message.trim() || !token) return;

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

  if (!ticket) return <p>Loading...</p>;

  return (
    <div style={{ maxWidth: 800, margin: "0 auto" }}>
      <h2>#{ticket.id}: {ticket.subject}</h2>

      <p><b>Status:</b> {ticket.status}</p>
      <p><b>Priority:</b> {ticket.priority}</p>
      <p><b>Created by:</b> {ticket.createdBy}</p>
      <p><b>Created:</b> {timeAgo(ticket.createdAt)}</p>

      <hr />

      <h3>Comments</h3>

      <textarea
        placeholder="Post a comment..."
        value={message}
        onChange={e => setMessage(e.target.value)}
        style={{ width: "100%", height: 80 }}
      />
      <button onClick={postComment}>Post</button>

      {comments.map(c => (
        <div key={c.id} style={{ marginTop: 12 }}>
          <b>{c.author}</b> · {timeAgo(c.createdAt)}
          <p>{c.message}</p>
        </div>
      ))}
    </div>
  );
}
