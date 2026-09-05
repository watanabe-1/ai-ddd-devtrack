import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FormEvent } from "react";

import { apiGet, apiPost } from "../../api/client";
import type { LearningGoal } from "../../types/api";

export function LearningGoalsPage() {
  const queryClient = useQueryClient();
  const goals = useQuery({
    queryKey: ["learning-goals"],
    queryFn: () => apiGet<LearningGoal[]>("/api/learning-goals/active"),
  });
  const createGoal = useMutation({
    mutationFn: (body: unknown) => apiPost<LearningGoal>("/api/learning-goals", body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["learning-goals"] }),
  });

  function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    createGoal.mutate({
      title: form.get("title"),
      description: form.get("description"),
      startDate: form.get("startDate"),
      targetDate: form.get("targetDate") || null,
    });
    event.currentTarget.reset();
  }

  return (
    <section>
      <header className="pageHeader">
        <h2>Learning Goals</h2>
      </header>
      <form onSubmit={onSubmit} className="form">
        <input name="title" placeholder="Title" required />
        <input name="description" placeholder="Description" />
        <input name="startDate" type="date" required />
        <input name="targetDate" type="date" />
        <button type="submit">Create</button>
      </form>
      {createGoal.error && <p className="error">{createGoal.error.message}</p>}
      <ul className="list">
        {goals.data?.map((goal) => (
          <li key={goal.id}>
            <strong>{goal.title}</strong>
            <span>{goal.status}</span>
          </li>
        ))}
      </ul>
    </section>
  );
}
