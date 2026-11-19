# City of Kalux - Project Milestones

This plan covers gameplay documentation, a code-quality review and repair plan, Java 8+ compatibility, the Swing interface requirement, and an executable JAR. Milestones 1 and 2 are complete; the gameplay guide is in [GAMEPLAY.md](./GAMEPLAY.md). Milestones 3-5 have been implemented, with runtime and interactive validation limits recorded below.

## Milestone 1 - Document gameplay

**Status:** Complete

**Goal:** Describe the game's implemented premise, gameplay loop, locations, progression, and keyboard controls in a player-facing document.

**Proposed approach:** Trace the main game loop, input handling, story/event progression, inventory and travel behavior, UI, and bundled story notes. Document observable behavior and clearly distinguish implementation gaps from intended features.

**Tasks:**

- Review the game entry point, view, player and character interactions, input controller, maps, and story notes.
- Record the premise, mechanics, control bindings, locations, story sequence, and conditional capture behavior.
- Note important limits where the implementation does not establish a feature, rather than inventing rules.
- Add and review [GAMEPLAY.md](./GAMEPLAY.md).

**Review:**

- Cross-check each documented control and progression claim against its implementation.
- Check that locations and story details agree with the bundled game text.
- Ensure spoilers and implementation caveats are clearly labeled.

**Acceptance criteria:**

- The guide explains the objective, how to explore and progress, all five travel destinations, inventory and dialogue interactions, and the controls needed to play.
- It does not claim unsupported gameplay outcomes.
- The guide exists in the repository as a standalone Markdown document.

## Milestone 2 - Review errors and plan fixes

**Status:** Complete

**Goal:** Identify and fix confirmed defects with focused regression checks.

**Approach:** Establish a compile baseline, inspect runtime-sensitive paths, then make surgical fixes and verify the affected behaviors.

**Tasks:**

- Fixed the achievement reward spawning on every tick, the unbounded pursuit scans, and the zero-threshold steal division.
- Added classpath-first loading for images, maps, and sounds, retaining filesystem fallback for development. Corrected the case mismatch for `paintH.png` and made required-asset failures explicit.
- Moved Swing setup and timed game updates to the event-dispatch thread.
- Ran focused regression checks for one-time reward spawning, zero-threshold stealing, and pursuit at a map corner.

**Review:**

- `javac --release 8` compiled all sources successfully on JDK 17.
- A temporary JAR containing the assets loaded the game model from a working directory outside the repository.
- A live interactive GUI playthrough and execution on a Java 8 runtime were not available; those remain for later validation.

**Findings and resolutions:**

- **High - Bonus character spawned every tick:** guarded by the character slot so the reward is added once. A five-tick check verified the character list grew by one.
- **High - Pursuit scans indexed outside the map:** both scan ranges are now clamped to the grid dimensions and use the correct horizontal coordinate. A pursuit check at the top-left corner completed without an index exception.
- **High - Steal payout divided by zero:** the payout uses a minimum divisor of one; a forced zero-threshold steal completed and paid the expected amount.
- **Medium - Assets depended on the working directory:** images, maps, and sounds now load from classpath resources first, with filesystem fallback. A packaged smoke run from outside the repository loaded the game resources; the image-name case mismatch was corrected.
- **Medium risk - Swing model access crossed threads:** window creation is scheduled on the EDT and a Swing `Timer` performs updates there, serializing input, updates, and painting on the EDT.

**Acceptance criteria:**

- All identified defects are fixed and the focused regression checks pass.
- Java 8 API compilation and packaged classpath-resource loading are verified.
- GUI behavior and an actual Java 8 runtime remain explicit follow-up checks; Java 8 runtime compatibility is not claimed complete here.

Headless gameplay checks are in [GameRulesTests.java](./test/game/GameRulesTests.java). Run `test\run-headless-tests.cmd` from the repository root.

## Milestone 3 - Support Java 8 and newer

**Status:** Partially complete - Java 8 API/bytecode compatibility is verified on JDK 17; a Java 8 runtime check is pending.

**Goal:** Build and run the game with Java 8, while preserving compatibility with newer supported JDKs.

**Proposed approach:** Identify the project's actual language/API requirements and establish a repeatable build configured for Java 8 bytecode and APIs. Avoid newer JDK APIs and syntax unless a compatible alternative is provided.

**Tasks:**

- Added `build.cmd` for this source tree and documented its usage in [README.md](./README.md).
- Compiled with Java 8 API compatibility and verified the packaged game class uses class-file version 52.
- Built on JDK 17.0.10; the JAR has not been run on Java 8 because that runtime is not installed here.
- No compatibility failure was found by the Java 8-targeted build or the headless gameplay checks.

**Review:**

- `build.cmd` uses `javac --release 8` on newer JDKs and `-source 1.8 -target 1.8` on JDK 8.
- The JDK 17 build completed and `javap` reported major version 52 for `game.KaluxGame`.
- A Java 8 runtime launch and full newer-JDK gameplay session remain unverified.

**Acceptance criteria and remaining validation:**

- Java 8 API/bytecode build: verified on JDK 17; a clean build using an actual JDK 8 remains unverified.
- Core gameplay on Java 8 and a newer JDK: headless gameplay rules pass on JDK 17; runtime sessions remain unverified.
- No Java 9+ API dependency is indicated by the `--release 8` build, but an actual Java 8 runtime check is still required.

## Milestone 4 - Confirm and preserve the Swing interface

**Status:** Implementation confirmed; interactive GUI validation is pending.

**Goal:** Ensure the game's desktop interface remains implemented with Java Swing, as requested.

**Proposed approach:** Audit the current UI and input path, then keep or adapt it to Swing components and event handling. The current game already has a Swing `JFrame` window and a `JComponent` renderer; this milestone verifies the whole interface rather than assuming it needs replacement.

**Tasks:**

- Confirmed the window and renderer use Swing (`JFrame`, `JComponent`), with a Swing `Timer` for updates.
- Kept the keyboard listener on the focusable game view and requested focus after showing the window.
- Documented Swing as the interface technology in [README.md](./README.md).
- A live check of controls, overlays, and focus has not been performed.

**Review:**

- Source inspection confirms there is no JavaFX dependency or alternate UI entry point.
- `javac --release 8` compiled the Swing code successfully.
- Window usability and keyboard behavior still require an interactive playthrough.

**Acceptance criteria and remaining validation:**

- Swing implementation and documentation: verified.
- Usability of the window, renderer, controls, notebook, map, inventory, and achievements: not yet interactively verified.

## Milestone 5 - Produce an executable JAR

**Status:** JAR built and packaged; launch from an external working directory is pending.

**Goal:** Deliver a runnable JAR that does not require the IDE or a manually assembled classpath.

**Proposed approach:** Package compiled classes with a manifest naming `game.KaluxGame` as the entry point, and make all runtime assets (images, maps, notes, and sounds) available reliably from the JAR. Prefer classpath resources or another explicit packaging strategy over relying on the process working directory.

**Tasks:**

- Added the reproducible `build.cmd` packaging step; it creates `dist\City-of-Kalux.jar`.
- Configured `game.KaluxGame` as the manifest entry point and packaged classes plus images, maps, notes, and sounds.
- Inspected the JAR: it contains 194 entries, including the main class and required runtime assets; the game class is Java 8 bytecode.
- Documented the build and launch command in [README.md](./README.md).
- Launch from an external working directory and runtime checks on Java 8 remain pending.

**Review:**

- JAR contents and required assets were inspected; `jar cfe` sets the executable main class.
- The JAR was built successfully on JDK 17, and the headless gameplay checks pass.
- An external-directory application launch and actual Java 8 runtime check remain unverified.

**Acceptance criteria and remaining validation:**

- Build output, Java 8 bytecode target, packaged assets, and documented launch command: verified.
- `java -jar <artifact>.jar` launch from outside the repository and execution on Java 8: pending runtime validation.
