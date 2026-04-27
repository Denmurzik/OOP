package org.example;

import org.example.view.ErrorDialog;

/**
 * Мэйн.
 */
public class Main {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> ErrorDialog.show(e));
        SnakeApp.main(args);
    }
}
