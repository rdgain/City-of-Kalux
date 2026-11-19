# City of Kalux

City of Kalux is a keyboard-controlled, story-driven Java game with a Swing interface. See [GAMEPLAY.md](./GAMEPLAY.md) for the controls and story guide.

## Requirements

- JDK 8 or newer
- Windows command prompt or PowerShell for the supplied build scripts

## Build

From the repository root, run:

```text
build.cmd
```

The executable JAR is created at `dist\City-of-Kalux.jar`. The build targets Java 8 APIs and bytecode and packages the images, maps, notes, and sounds in the JAR.

## Run

```text
java -jar dist\City-of-Kalux.jar
```

The JAR can be launched from any working directory by passing its full path. A graphical desktop is required to play.

## Headless gameplay checks

Run `test\run-headless-tests.cmd` to compile the project for Java 8 and execute the gameplay-rule checks without opening a window or initializing game assets/audio.

The application window, renderer, and keyboard input are implemented with Java Swing.
