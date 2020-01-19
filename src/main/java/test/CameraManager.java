package test;

import org.scijava.java3d.utils.universe.SimpleUniverse;

import org.scijava.java3d.Node;
import org.scijava.java3d.Transform3D;

import org.scijava.java3d.TransformGroup;

import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector3d;

public class CameraManager {

    Point3d mPosicionRelativaDeLaCamara = new Point3d(-6, 2, 0);

    Point3d mCentroRelativo = new Point3d(1.0, 1.0, 0);

    double mAnguloY = 0.0;

    TransformGroup mCamara = null;

    Node mCarBody = null;
    
    boolean mInvertirAngulo = false;

    public void modoA() {
        mPosicionRelativaDeLaCamara = new Point3d(-6, 2, 0);
        mCentroRelativo = new Point3d(1.0, 1.0, 0);
        mInvertirAngulo = false;
    }

    public void modoB() {
        mPosicionRelativaDeLaCamara = new Point3d(-3, 2, 0);
        mCentroRelativo = new Point3d(1.5, 1.0, 0);
        mInvertirAngulo = false;
    }

    public void modoC() {
        mPosicionRelativaDeLaCamara = new Point3d(0.37, 1.1, 0);
        mCentroRelativo = new Point3d(1.8, 0.9, 0);
        mInvertirAngulo = true;
    }

    public void modoD() {
        mPosicionRelativaDeLaCamara = new Point3d(-3, 3, 5);
        mCentroRelativo = new Point3d(1.0, 1.0, 0);
        mInvertirAngulo = false;
    }

    public CameraManager(SimpleUniverse mUniverso) {

        mCamara = mUniverso.getViewingPlatform().getViewPlatformTransform();

    }

    public void rotarY(double mAngulo) {
        mAnguloY = mAngulo;
    }

    public void actualizar() {

        Point3d objetivo = (Point3d)mCentroRelativo.clone();
        Point3d poscamara = (Point3d)mPosicionRelativaDeLaCamara.clone();
        
        Transform3D carT = new Transform3D();

        mCarBody.getLocalToVworld(carT);        
                
        Transform3D rot = new Transform3D();
        
        if( mInvertirAngulo ) {
            rot.rotY(-mAnguloY * 1.8 );        
        } else {
            rot.rotY(mAnguloY);            
        }
        
        rot.transform(poscamara);
        
        carT.transform( objetivo );
        carT.transform( poscamara );
        
        Transform3D camara = new Transform3D();

        camara.lookAt(poscamara, objetivo , new Vector3d(0, 1, 0));
                                
        camara.invert();
                        
        mCamara.setTransform(camara);

        /*Point3d posAbs =  (Point3d)mPosicionAbsoluta.clone();

        Vector3d centro = (Vector3d)this.mCentroRelativo.clone();

        posAbs.add( centro );

        mPosicionCamara = (Point3d)posAbs.clone();

        Vector3d posRel = (Vector3d)mPosicionRelativaDeLaCamara.clone();

        Transform3D rot = new Transform3D();

        rot.rotY( mAnguloY );

        rot.transform( posRel );

        rot.rotZ( -mAnguloZ );

        rot.transform( posRel );

        rot.rotX( -mAnguloX );

        rot.transform( posRel );

        mPosicionCamara.add( posRel );

        Transform3D camara = new Transform3D();

        camara.lookAt( mPosicionCamara , posAbs ,
                      new Vector3d(0, 1, 0));

        camara.invert();
        mCamara.setTransform(camara);*/

    }

    public void setCarBody(Node mCarBody) {
        this.mCarBody = mCarBody;
    }

    public Node getCarBody() {
        return mCarBody;
    }
}
