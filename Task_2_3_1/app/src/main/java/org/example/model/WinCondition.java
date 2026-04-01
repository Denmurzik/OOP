package org.example.model;

/**
 * Условие победы.
 */
public interface WinCondition {


    boolean checkWin(Snake snake, int score);
}
