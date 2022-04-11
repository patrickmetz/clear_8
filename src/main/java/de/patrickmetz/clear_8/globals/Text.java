package de.patrickmetz.clear_8.globals;

public class Text {

    public static class Cli {
        public static final String APPLICATION_NAME = "clear_8.jar";

        public static final String GAME = "game";
        public static final String HELP = "help";
        public static final String IPS  = "ips";
        public static final String VIP  = "vip";

        public static final String HELP_HELP = "Prints help for the command line options.";
        public static final String HELP_IPS  = "CPU instructions per second. Controls emulation speed. (100, 200, ...)";
        public static final String HELP_GAME = "Path to a game file. (C:\\my_folder\\my_game)";
        public static final String HELP_VIP  = "Use Cosmac VIP CPU if true; or Super Chip CPU otherwise. (true / false)";
    }

    public static class Gui {
        public static final String APPLICATION_NAME = "clear_8";

        public static final String CPU_SCHIP    = "Super Chip";
        public static final String CPU_VIP      = "Cosmac VIP";
        public static final String TOOL_TIP_CPU = "CPU type";

        public static final String TOOL_TIP_CPU_INSTRUCTIONS = "CPU instructions per second";

        public static final String LOAD_ROM = "Load game";

        public static final String STATE_PAUSE  = "Pause";
        public static final String STATE_RESUME = "Resume";
        public static final String STATE_STOP   = "Stop";

        public static final String FPS = "fps";
    }
}
