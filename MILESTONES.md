# City of Kalux - Project Milestones

This plan covers gameplay documentation, a code-quality review and repair plan, Java 8+ compatibility, the Swing interface requirement, and an executable JAR. Milestone 1 is complete; its gameplay guide is in [GAMEPLAY.md](./GAMEPLAY.md). The remaining milestones are planned, not yet performed.

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

**Status:** Planned

**Goal:** Build and run the game with Java 8, while preserving compatibility with newer supported JDKs.

**Proposed approach:** Identify the project's actual language/API requirements and establish a repeatable build configured for Java 8 bytecode and APIs. Avoid newer JDK APIs and syntax unless a compatible alternative is provided.

**Tasks:**

- Select and document a build method appropriate to this source tree.
- Compile with Java 8 source/API compatibility and verify the resulting class-file target.
- Run the game on Java 8 and at least one newer JDK.
- Resolve only compatibility/build failures and relevant defects identified during validation.
- Document prerequisites and build/run commands.

**Review:**

- Check the configured source and target compatibility, dependency versions, and any APIs used.
- Review the Java 8 build output and launch results on both the minimum and newer JDK.

**Acceptance criteria:**

- A clean checkout compiles successfully for Java 8 using documented commands.
- The application launches and the core gameplay paths work on Java 8 and a newer JDK.
- No Java 9+ runtime/API dependency is required to run the Java 8 build.

## Milestone 4 - Confirm and preserve the Swing interface

**Status:** Planned

**Goal:** Ensure the game's desktop interface remains implemented with Java Swing, as requested.

**Proposed approach:** Audit the current UI and input path, then keep or adapt it to Swing components and event handling. The current game already has a Swing `JFrame` window and a `JComponent` renderer; this milestone verifies the whole interface rather than assuming it needs replacement.

**Tasks:**

- Verify all interactive screens, overlays, rendering, and keyboard input are Swing-based.
- Identify any non-Swing UI dependency or entry point and remove/replace it if present.
- Check window creation, focus, repainting, and input behavior on supported JDKs.
- Document the interface technology and any remaining UI limitations.

**Review:**

- Inspect the UI dependency/build configuration and launch the game outside the IDE.
- Confirm controls and interface overlays remain usable after the compatibility/build work.

**Acceptance criteria:**

- The application interface is delivered through Java Swing; JavaFX is not required.
- The game window, renderer, controls, notebook, map, inventory, and achievements remain usable.
- The Swing requirement is explicit in the project documentation.

## Milestone 5 - Produce an executable JAR

**Status:** Planned

**Goal:** Deliver a runnable JAR that does not require the IDE or a manually assembled classpath.

**Proposed approach:** Package compiled classes with a manifest naming `game.KaluxGame` as the entry point, and make all runtime assets (images, maps, notes, and sounds) available reliably from the JAR. Prefer classpath resources or another explicit packaging strategy over relying on the process working directory.

**Tasks:**

- Configure a reproducible clean build and JAR packaging step.
- Include the manifest main class and all required runtime resources.
- Run the JAR from outside the repository root and verify it does not depend on IDE configuration.
- Check the JAR with the Java 8 runtime and a newer JDK.
- Document the artifact location and launch command.

**Review:**

- Inspect the JAR contents and manifest.
- Launch the packaged artifact in a clean working directory and verify images, maps, notes, sound, and gameplay load correctly.
- Confirm the artifact meets the Java 8+ target established in Milestone 3.

**Acceptance criteria:**

- A clean build produces a documented, executable JAR with `game.KaluxGame` as its entry point.
- `java -jar <artifact>.jar` starts the game outside the IDE and independently of the repository's working directory.
- Required assets and data are present and readable; the JAR works on Java 8 and a newer JDK.
