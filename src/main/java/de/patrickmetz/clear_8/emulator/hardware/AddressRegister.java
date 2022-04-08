package de.patrickmetz.clear_8.emulator.hardware;

final class AddressRegister {

    private int memory;

    int read() {
        return memory;
    }

    void write(int value) {
        memory = value;
    }

}