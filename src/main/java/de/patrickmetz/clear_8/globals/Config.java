package de.patrickmetz.clear_8.globals;

public class Config {

    public static class Cli {
        public static final int     DEFAULT_IPS = 500;
        public static final boolean DEFAULT_VIP = true;
    }

    public static class Emulator {
        public static final int FRAMES_PER_SECOND          = 60;
        public static final int FRAME_SLEEP_INTERVAL_IN_MS = 1;
    }

    public static class Gui {
        public static final int IPS_MINIMUM   = 100;
        public static final int IPS_STEP_SIZE = 100;
        public static final int IPS_MAXIMUM   = 1000;

        public static final int SCREEN_WIDTH  = 64;
        public static final int SCREEN_HEIGHT = 32;
        public static final int SCREEN_SCALE  = 8;

        public static final int SCREEN_PIXEL_COLOR_RED   = 45;
        public static final int SCREEN_PIXEL_COLOR_GREEN = 71;
        public static final int SCREEN_PIXEL_COLOR_BLUE  = 171;

        public static final int SCREEN_BACKGROUND_COLOR_RED   = 255;
        public static final int SCREEN_BACKGROUND_COLOR_GREEN = 255;
        public static final int SCREEN_BACKGROUND_COLOR_BLUE  = 255;

        public static final int FPS_UPDATE_INTERVAL_IN_MS = 1000;

        public static final int WINDOW_HORIZONTAL_GAP = 0;
        public static final int WINDOW_VERTICAL_GAP   = 0;

        public static final int TOP_PANEL_HORIZONTAL_GAP = 12;
        public static final int TOP_PANEL_VERTICAL_GAP   = 12;

        public static final int BOTTOM_PANEL_HORIZONTAL_GAP = 8;
        public static final int BOTTOM_PANEL_VERTICAL_GAP   = 0;

        public static final int CENTER_PANEL_WIDTH  = 640;
        public static final int CENTER_PANEL_HEIGHT = 480;
    }
}
