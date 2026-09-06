import { execFileSync } from "node:child_process";
import { readFileSync } from "node:fs";
import { join, relative } from "node:path";
import { fileURLToPath } from "node:url";

const root = fileURLToPath(new URL("../", import.meta.url));
const filePatterns = ["*.java", "*.ts", "*.tsx", "*.js", "*.jsx", "*.yml", "*.yaml"];
const files = execFileSync("git", ["ls-files", ...filePatterns], {
  cwd: root,
  encoding: "utf8",
})
  .split(/\r?\n/u)
  .filter(Boolean);
const todoPattern = /\b(?:TODO|FIXME)\b(?!\([^)]+,\s*\d{4}-\d{2}-\d{2}\):\s*\S)/u;
const commentPattern = /^\s*(?:\/\/|\/\*|\*|#)/u;
const violations = [];

for (const file of files) {
  const text = readFileSync(join(root, file), "utf8");
  const lines = text.split(/\r?\n/u);
  lines.forEach((line, index) => {
    if (commentPattern.test(line) && todoPattern.test(line)) {
      violations.push(
        `${relative(root, file)}:${index + 1}: TODO/FIXME must use "TODO(owner, YYYY-MM-DD): reason"`,
      );
    }
  });
}

if (violations.length > 0) {
  console.error(violations.join("\n"));
  process.exitCode = 1;
}
