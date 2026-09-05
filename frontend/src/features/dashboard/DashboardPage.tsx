import { useQuery } from "@tanstack/react-query";

import { apiGet } from "../../api/client";
import type { Dashboard } from "../../types/api";

export function DashboardPage() {
  const { data, isLoading, error } = useQuery({
    queryKey: ["dashboard"],
    queryFn: () => apiGet<Dashboard>("/api/dashboard"),
  });

  if (isLoading) return <p>Loading...</p>;
  if (error) return <p className="error">{error.message}</p>;
  if (!data) return null;

  return (
    <section>
      <header className="pageHeader">
        <h2>Dashboard</h2>
      </header>
      <div className="metrics">
        <div>
          <span>Active Goals</span>
          <strong>{data.activeGoals.length}</strong>
        </div>
        <div>
          <span>This Week</span>
          <strong>{data.thisWeekStudyMinutes} min</strong>
        </div>
        <div>
          <span>Upcoming Exams</span>
          <strong>{data.upcomingExams.length}</strong>
        </div>
      </div>
      <div className="grid">
        <section>
          <h3>Active Learning Goals</h3>
          <ul>
            {data.activeGoals.map((goal) => (
              <li key={goal.id}>{goal.title}</li>
            ))}
          </ul>
        </section>
        <section>
          <h3>Recent Study Sessions</h3>
          <ul>
            {data.recentStudySessions.map((session) => (
              <li key={session.id}>
                {session.studyDate} / {session.durationMinutes} min / {session.content}
              </li>
            ))}
          </ul>
        </section>
      </div>
    </section>
  );
}
