package test;

import org.jogamp.java3d.Node;

/**
 * Catalog of legacy track layouts saved for reference.
 */
public class TrackCatalog {

    public static Node getOriginalPistaA() {
        return new TrackBuilder()
                .translate(-40, 0, 0)
                .straight(50)
                .curveRight()
                .straight(10)
                .curveRight()
                .straight(50)
                .curveRight()
                .straight(10)
                .curveRight()
                .build();
    }

    public static Node getOriginalPistaB() {
        return new TrackBuilder()
                .straight(15)
                .curveRight(Math.PI / 6)
                .hill(-4, 40)
                .curveLeft(Math.PI / 6)
                .straight(30)
                .helix(1)
                .curveLeftWithHeight(Math.PI / 2, 1)
                .straight(30)
                .helix(-2)
                .curveLeft()
                .straight(20)
                .curveRight(Math.PI / 4)
                .hill(6, 60)
                .curveLeftWithHeight(3 * Math.PI / 4, 2)
                .straight(20)
                .curveRight(Math.PI)
                .curveLeft(Math.PI)
                .straight(35.6)
                .curveLeft(Math.PI / 2)
                .straight(28.01)
                .build();
    }

    public static Node getSimpleDemoTrack() {
        // Full-profile builder: road surface plus the raised brick side walls.
        // SimpleTrackBuilder renders only a flat top ribbon (no walls).
        return new TrackBuilder()
                .translate(0, 0, 0)
                .straight(40)
                .hill(4, 20)
                .hill(-4, 20)
                .curveRight(Math.PI / 2)
                .straight(25)
                .curveRight(Math.PI / 2)
                .straight(80)
                .curveRight(Math.PI / 2)
                .straight(25)
                .curveRight(Math.PI / 2)
                .build();
    }
}
