package de.patrickmetz.clear_8.gui.output;

import de.patrickmetz.clear_8.globals.Config;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

final public class DisplayImpl extends JPanel implements Display {

    private Color colorBackground;
    private Color colorPixel;

    private boolean[][] screenData;
    private int         updateCount;

    public DisplayImpl() {
        screenData = new boolean[Config.Gui.DISPLAY_WIDTH][Config.Gui.DISPLAY_HEIGHT];
        setDoubleBuffered(true);

        colorBackground = new Color(
                Config.Gui.DISPLAY_BACKGROUND_COLOR_RED,
                Config.Gui.DISPLAY_BACKGROUND_COLOR_GREEN,
                Config.Gui.DISPLAY_BACKGROUND_COLOR_BLUE
        );

        colorPixel = new Color(
                Config.Gui.DISPLAY_PIXEL_COLOR_RED,
                Config.Gui.DISPLAY_PIXEL_COLOR_GREEN,
                Config.Gui.DISPLAY_PIXEL_COLOR_BLUE
        );

        setBorder(BorderFactory.createLineBorder(Color.lightGray));

        setAlignmentX(Component.CENTER_ALIGNMENT);
        setAlignmentY(Component.CENTER_ALIGNMENT);

        setPreferredSize(new Dimension(Config.Gui.DISPLAY_WIDTH * Config.Gui.DISPLAY_SCALE, Config.Gui.DISPLAY_HEIGHT * Config.Gui.DISPLAY_SCALE));
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
        for (int x = 0; x < Config.Gui.DISPLAY_WIDTH; x++) {
            for (int y = 0; y < Config.Gui.DISPLAY_HEIGHT; y++) {
                graphics.setColor(screenData[x][y] ? colorPixel : colorBackground);

                // one scaled pixel
                graphics.fillRect(x * Config.Gui.DISPLAY_SCALE, y * Config.Gui.DISPLAY_SCALE, Config.Gui.DISPLAY_SCALE, Config.Gui.DISPLAY_SCALE);
            }
        }
    }
}
