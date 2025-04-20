# AeroScript: A Domain-Specific Language for Drone Missions

**AeroScript** is a domain-specific language (DSL) designed to define and execute mock drone missions. The main components of the program are the grammar that generates the lexer and parser, the interpreter, and the classes for actions, reactions, and executions.

---

## **Features**

- **Custom Grammar**: The AeroScript grammar is defined using ANTLR, enabling the generation of a lexer and parser for the language.
- **Mission Execution**: Supports defining simple drone missions with actions like moving, ascending, descending, and reacting to events such as low battery or obstacles.
- **REPL Interface**: Includes a REPL for interactive execution and debugging of AeroScript programs.
- **Simulation**: Tracks drone state, including position, altitude, battery level, and distance traveled.
---

## **Prerequisites**

To build and run this project, you need the following:

1. **Java Development Kit (JDK)**: Version 11 or higher.
   - [Download JDK](https://www.oracle.com/java/technologies/javase-downloads.html)
2. **Gradle**: Build tool for compiling and running the project.
   - [Install Gradle](https://gradle.org/install/)
   - Alternatively, use the Gradle Wrapper (`gradlew`) included in the project.
3. **ANTLR**: Used for generating the lexer and parser from the AeroScript grammar.
   - ANTLR runtime is included as a dependency in the project.
4. **JLine**: Library for building the REPL interface.
   - JLine is included as a dependency in the project.

---

## **How to Build and Run**
1. ```./gradlew build``` to generate build files.
2. ```java -jar build\libs\aeroscript-1.0.jar src\test\resources\program.aero``` to run a script containing instructions.
3. Type **help** once the REPL is running to get a list of commands.

## Project structure
- **Grammar:** The AeroScript grammar is defined in src/main/antlr/AeroScript.g4.
- **Interpreter:** The interpreter parses and executes AeroScript programs (Interpreter.java).
- **Runtime:** Includes classes for managing drone state, memory, and execution (Program.java).
- **Actions and Reactions:** Core logic for drone actions and reactions is implemented in the ast.stmt package (acMove.java, Reaction.java, etc.).
- **REPL:** Interactive REPL for testing and debugging AeroScript programs (REPL.java).
- **Tests:** Not implemented in this version

## Example instructions
```java
-> RandomTour {
    on message [start_tour] -> TakeOff
    TakeOff {
        ascend by 20
        on low battery -> EmergencyLanding
        move by 10
    } -> Explore
    Explore {
        move to point (random[0, 100], random[0, 100]) at speed 10
        on low battery -> EmergencyLanding
        on message [end_tour] -> ReturnToBase
    }
    ReturnToBase { return to base }
    EmergencyLanding { descend to ground }
}
```

## Modifying the drone instructions
- Modifications to the instructions for the drone mission can be done in src/test/runtime/program.aero

## Limitations and Potential Problems
- Adding mutiple missions to program.aero causes an exception.
