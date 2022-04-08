package de.patrickmetz.clear_8.gui.component.output;

import javax.swing.*;
import java.awt.*;

final public class Display extends JPanel implements de.patrickmetz.clear_8.emulator.Display {

    private final static int SCREEN_HEIGHT = 32;
    private final static int SCREEN_WIDTH = 64;

    private static Color colorBackground;
    private static Color colorPixel;

    private boolean[][] screenData;
    private int screenScale = 8;
    private int updateCount;

    public Display() {
        screenData = new boolean[SCREEN_WIDTH][SCREEN_HEIGHT];
        setDoubleBuffered(true);

        colorBackground = new Color(255, 255, 255);
        colorPixel = new Color(45, 71, 141);

        setBorder(BorderFactory.createLineBorder(Color.lightGray));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        setPreferredSize(new Dimension(
                SCREEN_WIDTH * screenScale,
                SCREEN_HEIGHT * screenScale
        ));
    }

    @Override
    public int getUpdateCount() {
        return updateCount;
    }

    /**
     * prepares the graphics for drawing
     */
    public void paintComponent(java.awt.Graphics graphics) {
        drawScreen(graphics);
    }

    /**
     * draws the prepared graphics as soon as Swing allows it
     */
    @Override
    public void update(boolean[][] data) {
        updateCount++;
        this.screenData = data;
        repaint();
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
