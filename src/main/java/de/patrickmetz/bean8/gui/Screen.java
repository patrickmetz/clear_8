/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 04.03.19 20:00.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel implements de.patrickmetz.bean8.emulator.Screen {

    private static final Color COLOR_BACKGROUND = Color.white;
    private static final Color COLOR_PIXEL = Color.darkGray;

    private final static int SCREEN_HEIGHT = 32;
    private final static int SCREEN_WIDTH = 64;

    private boolean[][] screenData;
    private int screenScale = 8;

    Screen() {
        screenData = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];

        setBorder(BorderFactory.createLineBorder(Color.gray));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        setPreferredSize(new Dimension(
                SCREEN_WIDTH * screenScale,
                SCREEN_HEIGHT * screenScale
        ));

        setDoubleBuffered(true);
    }

    public void paintComponent(java.awt.Graphics graphics) {
        drawScreen(graphics); // prepares the graphics
    }

    @Override
    public void update(boolean[][] data) {
        this.screenData = data;
        repaint(); // shows the prepared graphics
    }

    private void drawScreen(Graphics graphics) {
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            for (int y = 0; y < SCREEN_HEIGHT; y++) {
                graphics.setColor(
                        screenData[x][y] ? COLOR_PIXEL : COLOR_BACKGROUND
                );

                // one scaled pixel
                graphics.fillRect(
                        x * screenScale,
                        y * screenScale,
                        screenScale,
                        screenScale
                );
            }
        }
    }
}
