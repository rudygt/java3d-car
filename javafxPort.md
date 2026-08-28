# JavaFX 3D Port — Exploration Plan

**Branch:** `explore/javafx-port` · **Status:** planned (spike not started)
**Decision (2026-08-28):** explore JavaFX 3D first, as the "preservation route" —
the port that keeps this codebase recognizably itself. jMonkeyEngine (game-engine
route) and libGDX (browser route) remain future options; see the Alternatives
section at the bottom.

## Goal of the spike

A walking skeleton, not a finished port: the car drivable with arrow keys on a
straight + curve + hill track section, correct camera, working HUD. That is
enough to judge feel, effort, and fidelity before committing to full parity.

## What ports unchanged (the valuable 2004 code)

- **Loft kernel math**: the 8-point cross-section profile (`puntosBase`) extruded
  between two transforms. It emits vertex + UV arrays; only the mesh container
  changes.
- **Wheel/steering/speed physics** in `CarBehavior`: pure kinematics in SI units
  (v/r wheel spin, ±20° lock, speed-proportional yaw, reverse flip). Copy the
  formulas verbatim.
- **Track layouts** (`TrackBuilder` chains, `TrackCatalog`): pure data.
- **Textures**: `road.png` (seamless road), `pista.png` (wall atlas), skybox,
  ground — as-is.
- **vecmath**: keep `org.jogamp.java3d:vecmath` (standalone, no Java3D runtime
  dependency) so `Transform3D`/`Point3d` math code stays untouched. Drop
  `j3dcore`/`j3dutils`/JOGL on this branch's new code path.

## Architecture mapping

| Java3D (current) | JavaFX 3D |
|---|---|
| `BranchGroup` / `TransformGroup` | `Group` + `Transform` list (`Rotate`, `Translate`) |
| `Shape3D` + `QuadArray` | `MeshView` + `TriangleMesh` (each quad → 2 triangles) |
| `Appearance` + texture | `PhongMaterial` (`diffuseMap`; `selfIlluminationMap` for unlit ground/sky) |
| `Behavior` / `WakeupOnElapsedFrames` | `AnimationTimer` (nanosecond timestamp → keep the live-FPS normalization) |
| `Canvas3D` | `SubScene` (depthBuffer=true, `SceneAntialiasing.BALANCED`) |
| Swing HUD (`JLabel`s) | JavaFX labels in a `BorderPane` over the `SubScene` — simpler than today |
| `KeyboardInput` (AWT) | `Scene` key-pressed/released handlers feeding the same key-state object |
| Picking (`PickTool`) for terrain following | **own downward ray–quad intersection** (see Gotchas) |
| `Background` + skybox box | large inverted textured box, self-illuminated |
| `LinearFog` | no equivalent (no user shaders) — skip or fake late (distance-tinted haze ring) |
| Blob shadow | same trick: translucent dark ellipse (`Circle`/mesh) under the car |

## Gotchas to know before writing code

1. **Y axis points DOWN in JavaFX.** Standard fix: wrap the whole world in a
   root `Group` with `Rotate(180, Rotate.X_AXIS)` so all existing Y-up math and
   layouts work unchanged. Winding order flips with it — start with
   `CullFace.NONE` on meshes, optimize culling only at the end.
2. **`TriangleMesh` texcoord V origin is top-left** (Java3D's is bottom-left):
   v_fx = 1 − v_j3d when converting the UV constants.
3. **`PerspectiveCamera(true)`** (fixedEyeAtCameraZero) is required for a real
   3D camera; set `fieldOfView` ≈ 45° vertical to approximate the current view.
4. **No arbitrary-ray public picking API.** Terrain following should NOT use
   scene picking: while building the track, collect each slice's road quad
   (4 world-space points) into a plain list; car update does a ~30-line
   ray-down/quad intersection against it. Faster and simpler than Java3D's
   pick machinery, and it reuses geometry the loft kernel already computes.
5. **No anisotropic filtering / mipmap control** on `PhongMaterial`. The
   dedicated `road.png` already avoids atlas bleed; expect *some* distant
   shimmer, judge in the spike.
6. **Coexistence:** put the port in a new package `test.fx` with its own main
   (`test.fx.MainFx`) so the Java3D game keeps working on the same branch —
   side-by-side comparison is the point of the exploration.

## Dependencies / build

- JDK 21 + `org.openjfx:javafx-controls:21.0.x` (LTS pairing; classifier per-OS
  handled by the `javafx-maven-plugin`).
- Add `org.openjfx:javafx-maven-plugin` and a `mvn javafx:run` path; keep the
  existing assembly build for the Java3D app untouched.
- Windows dev loop: the screenshot harness (`target/diag/run_diag.ps1`) works
  for any window title — parametrize the `Java3D Car Debug` match when the FX
  window gets its own title (`Java3D Car FX`).

## Milestones

- **M1 — skeleton:** window, `SubScene`, HUD bar (Vuelta / KM/h / timer labels),
  a spinning textured cube. Proves deps, DPI, rendering.
- **M2 — track mesh:** port the loft kernel output into a `TriangleMesh` builder;
  render straight + curveRight + hill with `road.png`/`pista.png`. Verify seams.
- **M3 — car:** port `CarBuilder` boxes/cylinders (JavaFX has `Box`/`Cylinder`
  primitives) with the transform hierarchy: translate → steer (timon) → spin,
  rear wheels spin-only.
- **M4 — drive:** key input + `CarBehavior` state machine in an `AnimationTimer`;
  flat-ground driving with correct wheel spin/steer and HUD speed. **Spike done.**
- **M5 — world:** terrain-following raycast (hills/caracol), skybox, ground,
  camera modes + orbit, blob shadow.
- **M6 — game:** finish line, laps, countdown, `jpackage` installers per OS.

## Alternatives on hold

- **jMonkeyEngine 3.7** (`explore/jme-port`): full engine — real shadows, audio,
  particles, Android path. Restructures around `SimpleApplication`. Pick this if
  the goal shifts from preserving to evolving the game.
- **libGDX** (`explore/libgdx-port`): browser deploy via TeaVM — the game from a
  URL. Lower-level 3D (no scene graph), biggest rendering-port effort.
