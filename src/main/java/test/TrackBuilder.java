package test;

import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;

/**
 * A fluent API for building race tracks in Java3D.
 * This class abstracts the manual transformation and chaining of track
 * segments.
 */
public class TrackBuilder {
    private Group root;
    private Transform3D cursor;

    public TrackBuilder() {
        this.root = new Group();
        this.cursor = new Transform3D();
    }

    /**
     * Internal helper to add a node at the current cursor position and update the
     * cursor.
     */
    private TrackBuilder add(Node node, Transform3D delta) {
        TransformGroup tg = new TransformGroup(cursor);
        tg.addChild(node);
        root.addChild(tg);
        if (delta != null) {
            cursor.mul(delta);
        }
        return this;
    }

    public TrackBuilder straight(double length) {
        Node segment = MapBuilder.getSegmentoRecto(length);
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(length, 0, 0));
        return add(segment, delta);
    }

    public TrackBuilder curveRight() {
        return curveRight(Math.PI / 2.0);
    }

    public TrackBuilder curveRight(double angle) {
        Node segment = MapBuilder.getCurvaDerecha(angle);
        // MapBuilder's curve right ends with a specific orientation and position.
        // We need to calculate the delta based on the curve radius (default 10.0 in
        // MapBuilder)
        double radius = 10.0;
        double dx = Math.sin(angle) * radius;
        double dz = radius - Math.cos(angle) * radius;

        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(dx, 0, dz));
        Transform3D rot = new Transform3D();
        rot.rotY(-angle);
        delta.mul(rot);

        return add(segment, delta);
    }

    public TrackBuilder curveLeft() {
        return curveLeft(Math.PI / 2.0);
    }

    public TrackBuilder curveLeft(double angle) {
        Node segment = MapBuilder.getCurvaIzquierda(angle);
        double radius = 10.0;
        double dx = Math.sin(angle) * radius;
        double dz = -(radius - Math.cos(angle) * radius);

        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(dx, 0, dz));
        Transform3D rot = new Transform3D();
        rot.rotY(angle);
        delta.mul(rot);

        return add(segment, delta);
    }

    public TrackBuilder curveRightWithHeight(double angle, int height) {
        Node segment = MapBuilder.getCurvaDerecha(angle, height);
        double radius = 10.0;
        double dx = Math.sin(angle) * radius;
        double dz = radius - Math.cos(angle) * radius;

        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(dx, height, dz));
        Transform3D rot = new Transform3D();
        rot.rotY(-angle);
        delta.mul(rot);

        return add(segment, delta);
    }

    public TrackBuilder curveLeftWithHeight(double angle, int height) {
        Node segment = MapBuilder.getCurvaIzquierda(angle, height);
        double radius = 10.0;
        double dx = Math.sin(angle) * radius;
        double dz = -(radius - Math.cos(angle) * radius);

        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(dx, height, dz));
        Transform3D rot = new Transform3D();
        rot.rotY(angle);
        delta.mul(rot);

        return add(segment, delta);
    }

    public TrackBuilder hill(double height, int length) {
        Node segment = MapBuilder.getPendiente(height, length);
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(length, height, 0));
        return add(segment, delta);
    }

    public TrackBuilder helix(int height) {
        Node segment = MapBuilder.getCaracol(height);
        // Helix (caracol) in MapBuilder has a radioGiro of 15.0 and heights scales by
        // 5.
        // It's a full 360 loop (or multiples if height > 1)
        double totalHeight = (double) height * 5;

        // After a full loop (or multiple), dx and dz are 0, but dy is the total height.
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(0, totalHeight, 0));
        // Rotating by multiples of 2PI results in no orientation change.

        return add(segment, delta);
    }

    public TrackBuilder rotate(double angle) {
        Transform3D rot = new Transform3D();
        rot.rotY(angle);
        cursor.mul(rot);
        return this;
    }

    public TrackBuilder translate(double x, double y, double z) {
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(x, y, z));
        cursor.mul(trans);
        return this;
    }

    public Group build() {
        return root;
    }
}
