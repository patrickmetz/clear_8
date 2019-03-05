/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 05.03.19 19:07.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import de.patrickmetz.bean8.emulator.iScreen;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel implements iScreen {

    private final static int SCREEN_HEIGHT = 32;
    private final static int SCREEN_WIDTH = 64;

    private static Color colorBackground;
    private static Color colorPixel;

    private boolean[][] screenData;
    private int screenScale = 8;

    Screen() {
        screenData = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];
        colorBackground = new Color(224, 248, 208);
        colorPixel = new Color(8, 24, 32);

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
                        screenData[x][y] ? colorPixel : colorBackground
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
