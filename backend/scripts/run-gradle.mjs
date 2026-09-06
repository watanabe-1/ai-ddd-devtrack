import { spawnSync } from "node:child_process";
import { dirname, resolve } from "node:path";
import { fileURLToPath } from "node:url";

const scriptDir = dirname(fileURLToPath(import.meta.url));
const backendDir = resolve(scriptDir, "..");
const gradleWrapper = process.platform === "win32" ? "gradlew.bat" : "./gradlew";

const result = spawnSync(gradleWrapper, process.argv.slice(2), {
  cwd: backendDir,
  shell: process.platform === "win32",
  stdio: "inherit",
});

if (result.error) {
  throw result.error;
}

process.exit(result.status ?? 1);
