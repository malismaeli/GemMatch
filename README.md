# CS Gem Match  

## 🧩 Game Description

GemMatch is a simple match-three game. You click on two adjacent gems to swap them. If this swap creates a line of **three or more matching gems**, they break and increase your score (labeled as “Gem Matches”). The number of **moves** is tracked, and the game ends after **20 moves**, challenging you to get the highest score within that limit.

---

## 📂 Project Structure

### 🔹 `Gem`
- Contains **getters and setters** for gem properties.

### 🔹 `GemMatch` (Main class)
- Entry point of the game.
- Sets up the frame, formats the layout, and attaches panels (labels/buttons on top, game panel centered).

### 🔹 `GamePanel`
- Core of the UI where the game is played.
- Implements `MouseListener` for gem swapping.
- Handles image loading and drawing based on a 2D array.
- **Randomness:** Uses current time in milliseconds as seed to vary each new game.
- **Animation Handling:**
  - Splits gem removal and dropping phases.
  - Creates a "hole" effect using cropped background matching gem size/position.

### 🔹 `GemManager`
- Back-end logic and game state manager.
- Fills initial board ensuring **no initial matches**.
- Handles **swaps**, **match detection**, **removal**, and **dropping gems**.
- **Move Tracking:** Game ends after 20 valid moves.
- **Matching Logic:**
  - **Horizontal:** Count consecutive gems row-wise, store in a set.
  - **Vertical:** Count consecutive gems column-wise, store in a set.
  - **Removal:** Nullify matched cells and drop new gems from the top.

---

## ✨ Features

- **Scalable Panel:** Gems resize dynamically with the window.
- **Custom Gem Images:** Loaded via `BufferedImage`, matched to symbols in the 2D board.
- **Background Image:** Cropped dynamically to show holes after gem matches.

---

## 💡 Feature Requests / Future Work

- **Game Continuation:** Option to keep playing after 20 moves or restart.
- **Special Gem Combos:**
  - **4-match:** Creates a bomb that clears the entire row or column of that color.
  - **5-match:** Creates a mega-bomb that clears **all gems** of that color.
- **Hint System:**
  - Use Gomoku-like logic to suggest the best move.
  - Highlight suggested move on the panel.
- **Animations:**
  - Smooth swapping and falling animations (requires major refactor).
- **Sounds:**
  - Play sound on matches (e.g., in `GamePanel`), though not implemented yet due to Swing limitations.

---

