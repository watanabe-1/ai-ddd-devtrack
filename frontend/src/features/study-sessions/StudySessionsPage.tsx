import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FormEvent } from "react";

import { apiGet, apiPost } from "../../api/client";
import type { LearningGoal, StudySession } from "../../types/api";

export function StudySessionsPage() {
  const queryClient = useQueryClient();
  const goals = useQuery({
    queryKey: ["learning-goals"],
    queryFn: () => apiGet<LearningGoal[]>("/api/learning-goals/active"),
  });
  const sessions = useQuery({
    queryKey: ["study-sessions", "recent"],
    queryFn: () => apiGet<StudySession[]>("/api/study-sessions/recent"),
  });
  const recordSession = useMutation({
    mutationFn: (body: unknown) => apiPost<StudySession>("/api/study-sessions", body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["study-sessions", "recent"] }),
  });

  function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    recordSession.mutate({
      learningGoalId: form.get("learningGoalId"),
      studyDate: form.get("studyDate"),
      durationMinutes: Number(form.get("durationMinutes")),
      content: form.get("content"),
      note: form.get("note"),
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
              {goal.title}
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
            <strong>{session.content}</strong>
            <span>
              {session.studyDate} / {session.durationMinutes} min
            </span>
          </li>
        ))}
      </ul>
    </section>
  );
}
