package test;

import org.jogamp.java3d.Group;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.Transform3D;
import org.jogamp.java3d.TransformGroup;
import org.jogamp.vecmath.Vector3d;

/**
 * Alternate TrackBuilder that uses simplified (top-surface-only) primitives.
 */
public class SimpleTrackBuilder {
    private Group root;
    private Transform3D cursor;

    public SimpleTrackBuilder() {
        this.root = new Group();
        this.cursor = new Transform3D();
    }

    private SimpleTrackBuilder add(Node node, Transform3D delta) {
        TransformGroup tg = new TransformGroup(cursor);
        tg.addChild(node);
        root.addChild(tg);
        if (delta != null) {
            cursor.mul(delta);
        }
        return this;
    }

    public SimpleTrackBuilder straight(double length) {
        Node segment = MapBuilder.getSegmentoRectoSimple(length);
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(length, 0, 0));
        return add(segment, delta);
    }

    public SimpleTrackBuilder curveRight() {
        return curveRight(Math.PI / 2.0);
    }

    public SimpleTrackBuilder curveRight(double angle) {
        Node segment = MapBuilder.getCurvaDerechaSimple(angle);
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

    public SimpleTrackBuilder curveLeft() {
        return curveLeft(Math.PI / 2.0);
    }

    public SimpleTrackBuilder curveLeft(double angle) {
        Node segment = MapBuilder.getCurvaIzquierdaSimple(angle);
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

    public SimpleTrackBuilder curveRightWithHeight(double angle, int height) {
        Node segment = MapBuilder.getCurvaDerechaSimple(angle, height);
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

    public SimpleTrackBuilder curveLeftWithHeight(double angle, int height) {
        Node segment = MapBuilder.getCurvaIzquierdaSimple(angle, height);
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

    public SimpleTrackBuilder hill(double height, int length) {
        Node segment = MapBuilder.getPendienteSimple(height, length);
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(length, height, 0));
        return add(segment, delta);
    }

    public SimpleTrackBuilder helix(int height) {
        Node segment = MapBuilder.getCaracolSimple(height);
        double totalHeight = (double) height * 5;
        Transform3D delta = new Transform3D();
        delta.setTranslation(new Vector3d(0, totalHeight, 0));
        return add(segment, delta);
    }

    public SimpleTrackBuilder rotate(double angle) {
        Transform3D rot = new Transform3D();
        rot.rotY(angle);
        cursor.mul(rot);
        return this;
    }

    public SimpleTrackBuilder translate(double x, double y, double z) {
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3d(x, y, z));
        cursor.mul(trans);
        return this;
    }

    public Group build() {
        return root;
    }
}
