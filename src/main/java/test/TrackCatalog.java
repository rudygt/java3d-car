package test;

import org.scijava.java3d.Node;

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
}
