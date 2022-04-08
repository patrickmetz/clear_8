package de.patrickmetz.clear_8.gui.listener;

import de.patrickmetz.clear_8.emulator.Keyboard;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/**
 * Listens to  keyboard events and translates real keys to<p>
 * chip8's original 16 hexadecimal keys (from 0x0 to 0xf)<p>
 * <p> original keys:<p>
 * 1|2|3|C <p>
 * 4|5|6|D <p>
 * 7|8|9|E <p>
 * A|0|B|F <p>
 * <p>
 * used real keys:<p>
 * 1|2|3|4<p>
 * Q|W|E|R<p>
 * A|S|D|F<p>
 * Y|X|C|V<p>
 */
final public class KeyboardListener extends KeyAdapter implements Keyboard {

    /**
     * maps real keys to the 16 original keys
     */
    private final HashMap<Integer, Integer> keyMap;

    private volatile int pressedKey;

    public KeyboardListener() {
        keyMap = new HashMap<>(16, 2);

        keyMap.put(KeyEvent.VK_1, 0x1);
        keyMap.put(KeyEvent.VK_2, 0x2);
        keyMap.put(KeyEvent.VK_3, 0x3);
        keyMap.put(KeyEvent.VK_4, 0xC);

        keyMap.put(KeyEvent.VK_Q, 0x4);
        keyMap.put(KeyEvent.VK_W, 0x5);
        keyMap.put(KeyEvent.VK_E, 0x6);
        keyMap.put(KeyEvent.VK_R, 0xD);

        keyMap.put(KeyEvent.VK_A, 0x7);
        keyMap.put(KeyEvent.VK_S, 0x8);
        keyMap.put(KeyEvent.VK_D, 0x9);
        keyMap.put(KeyEvent.VK_F, 0xE);

        keyMap.put(KeyEvent.VK_Y, 0xA);
        keyMap.put(KeyEvent.VK_X, 0x0);
        keyMap.put(KeyEvent.VK_C, 0xB);
        keyMap.put(KeyEvent.VK_V, 0xF);
    }

    @Override
    public int getNextKeyPressed() {
        while (pressedKey == -1) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ignored) {
            }
        }

        return pressedKey;
    }

    @Override
    public boolean isKeyPressed(int expectedKey) {
        return (pressedKey == expectedKey);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Integer mappedKey = keyMap.get(e.getKeyCode());

        if (mappedKey != null) {
            pressedKey = mappedKey;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Integer mappedKey = keyMap.get(e.getKeyCode());

        if (mappedKey != null) {
            pressedKey = -1;
        }
    }

}
