import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { FormEvent } from "react";
import { apiGet, apiPost } from "../../api/client";
import type { Certification } from "../../types/api";

export function CertificationsPage() {
  const queryClient = useQueryClient();
  const certifications = useQuery({ queryKey: ["certifications"], queryFn: () => apiGet<Certification[]>("/api/certifications") });
  const register = useMutation({
    mutationFn: (body: unknown) => apiPost<Certification>("/api/certifications", body),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ["certifications"] }),
  });

  function onSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const form = new FormData(event.currentTarget);
    register.mutate({ qualificationName: form.get("qualificationName") });
    event.currentTarget.reset();
  }

  return (
    <section>
      <header className="pageHeader"><h2>Certifications</h2></header>
      <form onSubmit={onSubmit} className="form">
        <input name="qualificationName" placeholder="Qualification name" required />
        <button type="submit">Register</button>
      </form>
      {register.error && <p className="error">{register.error.message}</p>}
      <ul className="list">
        {certifications.data?.map((certification) => (
          <li key={certification.id}>
            <strong>{certification.qualificationName}</strong>
            <span>{certification.status}</span>
          </li>
        ))}
      </ul>
    </section>
  );
}

