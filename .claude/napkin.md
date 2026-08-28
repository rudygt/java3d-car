# Napkin Runbook

## Curation Rules
- Re-prioritize on every read.
- Keep recurring, high-value notes only.
- Max 10 items per category.
- Each item includes date + "Do instead".

## Execution & Validation (Highest Priority)
1. **[2026-08-28] Repo lives on Windows FS; app must run on Windows**
   Do instead: build/run via WSL interop: `cmd.exe /c "mvnw.cmd -q package assembly:single -DskipTests"`, run the fat jar with JDK 11 at `C:\Program Files\Amazon Corretto\jdk11.0.7_10\bin\java.exe`.
2. **[2026-08-28] Stack is JogAmp Java3D 1.7.1 + JOGL 2.6.0; runs on JDK 11–21**
   Java3D 1.7.1 is NOT on Maven Central — vendored in `lib/` (project-local repo declared in pom). JDK 16+ needs `--add-exports java.desktop/sun.awt` + `sun.java2d` (baked into the fat-jar manifest as `Add-Exports`; without them Canvas3D dies with NPE "awtConfig is null"). Do instead: run the fat jar plainly; never remove the manifest `Add-Exports` or the `lib/` repo.
3. **[2026-08-28] Visual verification harness exists**
   Do instead: use `target/diag/run_diag.ps1` (launches jar, waits, foregrounds window, DPI-aware screenshot, dumps stdout). Call: `powershell.exe -NoProfile -ExecutionPolicy Bypass -File "C:\repos\va\java3d-car\target\diag\run_diag.ps1" -JvmArgs "..." -OutPng "..." -LogFile "..."`. Recreate from git-ignored target/ if missing.
4. **[2026-08-28] Screen captures grab whatever is on top**
   Do instead: always `SetForegroundWindow` on the app window + 2s sleep before `CopyFromScreen`, else you screenshot the terminal.

## Domain Behavior Guardrails
1. **[2026-08-28] HiDPI (user runs 1920×1080 @125%): JOGL 2.6 handles it; uiScale=1 kept as default preference**
   `Main.main` sets `sun.java2d.uiScale=1` (guarded, overridable via -D) before AWT loads → original crisp 1:1-pixel look. On JOGL 2.6 the game also renders correctly WITH scaling; on old JOGL 2.3.2 it broke (viewport filled only bottom-left ~80%). Do instead: keep the guarded default; it must stay the first thing in main.
2. **[2026-08-28] `pista.png` (678×166) is THE track texture ATLAS; `output.png` is a bad regenerated clone**
   Top region = asphalt+centerline (texcoords t0..t3), bottom-left 132×66 = large wall blocks (t4..t7). output.png has same layout but small dense bricks/dashes → walls look wrong. Do instead: always load pista.png for track appearances; map sub-regions per face, never (0,0)-(1,1) across track quads.
3. **[2026-08-28] Two track builders with same fluent API**
   `TrackBuilder` (full profile: road + brick walls, `getSegmento`) vs `SimpleTrackBuilder` (flat top ribbon only, `*Simple` methods in MapBuilder). Do instead: use `TrackBuilder` for anything that should look like the classic game; track selection is `frmMain.TRACK_ID` ("pista-a" | "pista-b" | "simple-demo").
4. **[2026-08-28] MapBuilder full-profile methods use mutable static state (`actual`/`siguiente`)**
   Do instead: don't build two tracks concurrently or interleave builder calls; build one track at a time.

## Shell & Command Reliability
1. **[2026-08-28] PowerShell output has CRLF**
   Do instead: pipe interop output through `tr -d '\r'`.
