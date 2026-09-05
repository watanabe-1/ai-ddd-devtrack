export type LearningGoal = {
  id: string;
  title: string;
  description: string;
  startDate: string;
  targetDate: string | null;
  status: string;
};

export type StudySession = {
  id: string;
  learningGoalId: string;
  studyDate: string;
  durationMinutes: number;
  content: string;
  note: string;
};

export type Certification = {
  id: string;
  qualificationName: string;
  relatedLearningGoalId: string | null;
  plannedExamDate: string | null;
  status: string;
  attempts: ExamAttempt[];
};

export type ExamAttempt = {
  id: string;
  examDate: string;
  resultDate: string;
  outcome: string;
  note: string;
};

export type Dashboard = {
  activeGoals: { id: string; title: string }[];
  thisWeekStudyMinutes: number;
  recentStudySessions: {
    id: string;
    learningGoalId: string;
    studyDate: string;
    durationMinutes: number;
    content: string;
  }[];
  upcomingExams: {
    id: string;
    qualificationName: string;
    plannedExamDate: string | null;
  }[];
};
