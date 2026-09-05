import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter, NavLink, Route, Routes } from "react-router-dom";
import { CertificationsPage } from "./features/certifications/CertificationsPage";
import { DashboardPage } from "./features/dashboard/DashboardPage";
import { LearningGoalsPage } from "./features/learning-goals/LearningGoalsPage";
import { StudySessionsPage } from "./features/study-sessions/StudySessionsPage";
import "./styles.css";

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="app">
          <aside className="sidebar">
            <h1>DevTrack</h1>
            <nav>
              <NavLink to="/">Dashboard</NavLink>
              <NavLink to="/learning-goals">Learning Goals</NavLink>
              <NavLink to="/study-sessions">Study Sessions</NavLink>
              <NavLink to="/certifications">Certifications</NavLink>
            </nav>
          </aside>
          <main>
            <Routes>
              <Route path="/" element={<DashboardPage />} />
              <Route path="/learning-goals" element={<LearningGoalsPage />} />
              <Route path="/study-sessions" element={<StudySessionsPage />} />
              <Route path="/certifications" element={<CertificationsPage />} />
            </Routes>
          </main>
        </div>
      </BrowserRouter>
    </QueryClientProvider>
  </React.StrictMode>,
);

