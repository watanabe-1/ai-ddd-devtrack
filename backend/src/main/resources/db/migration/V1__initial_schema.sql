create table learning_goals (
    id uuid primary key,
    title varchar(200) not null,
    description text,
    start_date date not null,
    target_date date,
    status varchar(30) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table study_sessions (
    id uuid primary key,
    learning_goal_id uuid not null,
    study_date date not null,
    duration_minutes integer not null check (duration_minutes > 0),
    content text not null,
    note text,
    created_at timestamp not null,
    updated_at timestamp not null
);

create index idx_study_sessions_learning_goal_id on study_sessions (learning_goal_id);
create index idx_study_sessions_study_date on study_sessions (study_date);

create table certifications (
    id uuid primary key,
    qualification_name varchar(200) not null,
    related_learning_goal_id uuid,
    planned_exam_date date,
    status varchar(30) not null,
    created_at timestamp not null,
    updated_at timestamp not null
);

create table exam_attempts (
    id uuid primary key,
    certification_id uuid not null references certifications (id) on delete cascade,
    exam_date date not null,
    result_date date not null,
    outcome varchar(30) not null,
    note text
);

create index idx_certifications_planned_exam_date on certifications (planned_exam_date);
create index idx_exam_attempts_certification_id on exam_attempts (certification_id);
