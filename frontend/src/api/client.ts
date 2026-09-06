import type { paths } from "./generated/schema";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8080";

type MethodPath<Method extends string> = {
  [Path in keyof paths]: Method extends keyof paths[Path]
    ? Exclude<paths[Path][Method], undefined> extends never
      ? never
      : Path
    : never;
}[keyof paths];

type Operation<Path extends keyof paths, Method extends keyof paths[Path]> = Exclude<
  paths[Path][Method],
  undefined
>;

type JsonContent<Response> = Response extends { content: infer Content }
  ? Content extends { "*/*": infer Data }
    ? Data
    : Content extends { "application/json": infer Data }
      ? Data
      : never
  : never;

type SuccessResponse<ApiOperation> = ApiOperation extends { responses: infer Responses }
  ? 200 extends keyof Responses
    ? JsonContent<Responses[200]>
    : 201 extends keyof Responses
      ? JsonContent<Responses[201]>
      : never
  : never;

type RequestBody<ApiOperation> = ApiOperation extends {
  requestBody: { content: { "application/json": infer Body } };
}
  ? Body
  : undefined;

export async function apiGet<Path extends MethodPath<"get">>(
  path: Path,
): Promise<SuccessResponse<Operation<Path, "get">>> {
  const response = await fetch(`${API_BASE_URL}${path}`);
  if (!response.ok) {
    throw new Error(await readError(response));
  }
  return response.json();
}

export async function apiPost<Path extends MethodPath<"post">>(
  path: Path,
  body?: RequestBody<Operation<Path, "post">>,
): Promise<SuccessResponse<Operation<Path, "post">>> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: body === undefined ? undefined : JSON.stringify(body),
  });
  if (!response.ok) {
    throw new Error(await readError(response));
  }
  return response.json();
}

async function readError(response: Response): Promise<string> {
  try {
    const data = await response.json();
    return data.message ?? response.statusText;
  } catch {
    return response.statusText;
  }
}
