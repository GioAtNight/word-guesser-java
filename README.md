# Word Guesser

A Wordle-style word guessing game built in **Java** with **Swing**, developed in **Apache NetBeans** for my Programming in Java course (COM-209) at Saint Leo University.

Players create an account, then guess a hidden word in six attempts. After each guess, every letter is colored to show whether it is in the right spot, in the word but misplaced, or not in the word at all.

## Features

- **Single player:** choose a word length and try to solve the word in six attempts.
- **Two-player (PvP) mode:** a second player logs in and both race on the same word.
- **Accounts:** register and log in, with each player's progress saved between sessions.
- **Stats and leaderboard:** tracks games played, wins, losses, and PvP wins and losses, with a leaderboard to compare players.
- **Large dictionary:** about 13,500 words in `data/words.txt`, covering multiple word lengths.
- **Correct duplicate-letter handling:** the guess evaluator counts repeated letters properly, so the colors match standard Wordle rules.

## Tech Stack

- Java (Swing for the GUI)
- Apache NetBeans (Ant project)
- Plain-text file storage for words and player data

## Project Structure

```
src/wordguesser/
├── root/Main/Main.java   # Entry point
├── model/                # User and LetterState
├── service/              # GameEngine (guess logic), WordService, UserStore
└── ui/                   # Swing panels: login, register, menu, game, PvP, stats, leaderboard
data/
├── words.txt             # Word list
└── users.txt             # Player accounts (created at runtime, not tracked in git)
```

## How to Run

**In NetBeans:**
1. Clone this repository.
2. In NetBeans, choose **File > Open Project** and select the cloned folder.
3. Click **Run**.

**From the command line** (from the project root, so the `data/` folder is found):
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out wordguesser.root.Main
```

The project is configured for a recent JDK. If NetBeans reports a Java version mismatch, change the source level under **Project Properties > Sources**.

## What I Learned

- Structuring a Java application into model, service, and UI layers.
- Building a multi-screen Swing interface with layouts and event handling.
- Implementing game logic that correctly handles repeated letters.
- Reading and writing files to persist data between runs.

## Known Limitations and Future Improvements

- Passwords are stored in plain text in `users.txt`. A real application should hash them (for example with bcrypt or PBKDF2).
- Data is kept in flat text files; a database would scale better.
- Adding unit tests for `GameEngine` would be a good next step.

## Author

Giovanni Sassano, Cybersecurity major, Saint Leo University
