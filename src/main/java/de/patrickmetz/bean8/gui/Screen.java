/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 04.03.19 18:53.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel implements de.patrickmetz.bean8.emulator.Screen {

    private final static int SCREEN_HEIGHT = 32;
    private final static int SCREEN_WIDTH = 64;
    private boolean[][] data;
    private int screenFactor = 8;

    Screen() {
        data = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];

        setBackground(new Color(200, 200, 200));
        setBorder(BorderFactory.createLineBorder(Color.gray));

        setDoubleBuffered(true);

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        setPreferredSize(new Dimension(
                SCREEN_WIDTH * screenFactor,
                SCREEN_HEIGHT * screenFactor
        ));
    }

    @Override
    public void draw(boolean[][] data) {
        this.data = data;
        repaint();
    }

    public void paintComponent(java.awt.Graphics graphics) {
        drawScreen(graphics);
    }

    private void drawPixel(java.awt.Graphics graphics, int x, int y, boolean state) {
        graphics.setColor(state ? Color.black : Color.white);

        graphics.fillRect(
                x * screenFactor,
                y * screenFactor,
                screenFactor,
                screenFactor
        );
    }

    private void drawScreen(java.awt.Graphics graphics) {
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            for (int y = 0; y < SCREEN_HEIGHT; y++) {
                drawPixel(graphics, x, y, data[x][y]);
            }
        }
    }
}
