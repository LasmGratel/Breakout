package cc.lasmgratel.breakout;

import cc.lasmgratel.breakout.render.RenderThread;

public class Breakout {
    public static void main(String[] args) {
        new Thread(new RenderThread()).start();
    }
}
