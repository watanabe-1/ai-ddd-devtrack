import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FormEvent } from "react";

import { apiGet, apiPost } from "../../api/client";
import type { RecordStudySessionRequest } from "../../api/types";

export function StudySessionsPage() {
  const queryClient = useQueryClient();
  const goals = useQuery({
    queryKey: ["learning-goals"],
    queryFn: () => apiGet("/api/learning-goals/active"),
  });
  const sessions = useQuery({
    queryKey: ["study-sessions", "recent"],
    queryFn: () => apiGet("/api/study-sessions/recent"),
  });
  const recordSession = useMutation({
    mutationFn: (body: RecordStudySessionRequest) => apiPost("/api/study-sessions", body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["study-sessions", "recent"] }),
  });

  function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    recordSession.mutate({
      learningGoalId: String(form.get("learningGoalId")),
      studyDate: String(form.get("studyDate")),
      durationMinutes: Number(form.get("durationMinutes")),
      content: String(form.get("content")),
      note: String(form.get("note") ?? ""),
    });
    event.currentTarget.reset();
  }

  return (
    <section>
      <header className="pageHeader">
        <h2>Study Sessions</h2>
      </header>
      <form onSubmit={onSubmit} className="form">
        <select name="learningGoalId" required>
          <option value="">Learning Goal</option>
          {goals.data?.map((goal) => (
            <option key={goal.id} value={goal.id}>
              {goal.title ?? goal.id}
            </option>
          ))}
        </select>
        <input name="studyDate" type="date" required />
        <input name="durationMinutes" type="number" min="1" placeholder="Minutes" required />
        <input name="content" placeholder="Content" required />
        <input name="note" placeholder="Note" />
        <button type="submit">Record</button>
      </form>
      {recordSession.error && <p className="error">{recordSession.error.message}</p>}
      <ul className="list">
        {sessions.data?.map((session) => (
          <li key={session.id}>
            <strong>{session.content ?? session.id}</strong>
            <span>
              {session.studyDate ?? "-"} / {session.durationMinutes ?? 0} min
            </span>
          </li>
        ))}
      </ul>
    </section>
  );
}
