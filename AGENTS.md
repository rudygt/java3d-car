# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/test` holds the Java3D application code; the entry point is `test.Main`.
- `src/main/resources/3d` contains textures, skybox images, and track assets used at runtime.
- `target` is Maven output (jars and compiled classes).
- `README.md` and `trackBuilder.md` document usage and track building notes.

## Build, Test, and Development Commands
- `mvn package assembly:single` builds the fat jar with dependencies into `target/Java3dCar-1.0-SNAPSHOT-jar-with-dependencies.jar`.
- `java -jar target/Java3dCar-1.0-SNAPSHOT-jar-with-dependencies.jar` runs the packaged game (JDK 11 through 21; the manifest carries the `Add-Exports` entries JOGL needs on JDK 16+).
- `mvn exec:java` runs in-process in the Maven JVM; on JDK 16+ it needs `MAVEN_OPTS="--add-exports=java.desktop/sun.awt=ALL-UNNAMED --add-exports=java.desktop/sun.java2d=ALL-UNNAMED"`.
- `lib/` is a project-local Maven repository vendoring JogAmp Java3D 1.7.1 (`org.jogamp.java3d`), which is not published to Maven Central. JOGL/GlueGen 2.6.0 come from Central.

## Coding Style & Naming Conventions
- Use 4-space indentation and standard Java brace style (opening brace on the same line).
- Package names stay lowercase (e.g., `test`), class names are `PascalCase`, methods/fields are `camelCase`.
- Keep resource filenames descriptive and lowercase; preserve existing directory structure under `src/main/resources/3d`.

## Testing Guidelines
- No automated tests are currently present; there is no `src/test/java` tree.
- If adding tests, place them under `src/test/java` and follow the `*Test.java` naming pattern so Maven Surefire can detect them.

## Commit & Pull Request Guidelines
- Commit messages in history are short, descriptive summaries (e.g., “antigravity track builder refactor”). Follow that style with imperative, lowercase summaries.
- PRs should describe the change, include run instructions if behavior changes, and attach a screenshot for visual updates (e.g., new track/texture assets).

## Configuration & Assets
- The code compiles at source/target 8 and runs on JDK 11 through 21 (verified) on the JogAmp Java3D 1.7.1 + JOGL 2.6.0 stack.
- Asset changes should be reflected in `README.md` or `trackBuilder.md` if they affect gameplay or controls.
