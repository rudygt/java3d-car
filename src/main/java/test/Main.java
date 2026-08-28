package test;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

public class Main {
    public Main() {
    }

    public static void main(String[] args) {

        // Java3D's JOGL renderer and Windows display scaling don't mix: the
        // GL viewport can end up at the logical size while the surface is
        // physical pixels, so the scene only fills part of the canvas. Opt
        // out of AWT scaling before the toolkit loads to get Java-8-style
        // 1:1 pixels. Must run before any AWT/Swing class is initialized;
        // pass -Dsun.java2d.uiScale=... on the command line to override.
        if (System.getProperty("sun.java2d.uiScale") == null) {
            System.setProperty("sun.java2d.uiScale", "1");
        }

        frmMain m = new frmMain( );

        m.setVisible(true);
    }

}
