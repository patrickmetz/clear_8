/*
 * Developed by Patrick Metz <patrickmetz@web.de>.
 * Last modified 03.03.19 20:21.
 * Copyright (c) 2019. All rights reserved.
 */

package de.patrickmetz.bean8.gui;

import javax.swing.*;
import java.awt.*;

public class Screen extends JPanel implements de.patrickmetz.bean8.emulator.Screen {

    private final static int SCREEN_HEIGHT = 32;
    private final static int SCREEN_WIDTH = 64;
    private java.awt.Graphics graphics;
    private int screenFactor = 8;

    Screen() {
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
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            for (int y = 0; y < SCREEN_HEIGHT; y++) {
                drawPixel(x, y, data[x][y]);
            }
        }

        System.out.println("draw");
    }

    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        graphics = g;

        repaint();
    }

    private void drawPixel(int x, int y, boolean state) {
        graphics.setColor(state ? Color.black : Color.white);

        graphics.fillRect(
                x * screenFactor,
                y * screenFactor,
                screenFactor,
                screenFactor
        );
    }
}
