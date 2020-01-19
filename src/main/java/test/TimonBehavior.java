package test;

import org.scijava.java3d.utils.picking.PickIntersection;
import org.scijava.java3d.utils.picking.PickResult;
import org.scijava.java3d.utils.picking.PickTool;
import org.scijava.java3d.utils.universe.SimpleUniverse;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;

import java.util.Enumeration;

import org.scijava.java3d.Behavior;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.PickInfo;
import org.scijava.java3d.PickRay;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.WakeupCondition;
import org.scijava.java3d.WakeupCriterion;
import org.scijava.java3d.WakeupOnAWTEvent;
import org.scijava.java3d.WakeupOnElapsedFrames;
import org.scijava.java3d.WakeupOr;

import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector2d;
import org.scijava.vecmath.Vector3d;
import org.scijava.vecmath.Vector3f;

public class TimonBehavior extends Behavior {

    private WakeupOnElapsedFrames framewake = null;

    private WakeupCriterion[] wakeupArray = new WakeupCriterion[1];
    private WakeupCondition wakeupCondition = null;

    private double mAnguloActual = 0;

    private double mAnguloPaso = 0.02;

    TransformGroup mTimonI = null;
    TransformGroup mTimonD = null;

    TransformGroup mCarro = null;

    double mVelocidad = 0; // metros / segundo
    double mAceleracion = 0.05f; // metros / segundo ^ 2
    double mDAceleracion = 0.1f; // metros / segundo ^ 2

    double mTiempo = 0;
    double mTiempoD = 0;

    boolean mAcelerando = false;
    boolean mDAcelerando = false;
    boolean mReversa = false;

    boolean mKeyUp = false;
    boolean mKeyDown = false;

    boolean mKeyLeft = false;
    boolean mKeyRight = false;

    TransformGroup mRuedaDI = null;
    TransformGroup mRuedaDD = null;
    TransformGroup mRuedaTI = null;
    TransformGroup mRuedaTD = null;

    TransformGroup mTimonHorizontal = null;

    TransformGroup mTimonVertical = null;

    TransformGroup mTimonX = null;

    double mAnguloRuedas = 0.0;

    double mRadioRueda = 0.65;

    double mDAngulo = 0.0;

    double mRps = 0.0;

    double mRadps = 0.0;

    double mVelMaxima = 20.0; // m / s
    double mVelMaximaR = 5.0; // m / s

    private Estado mEstado = Estado.Zero;

    private Estado mEstadoTimon = Estado.Zero;

    double mDireccion = 0.0; // THETA

    double mDireccionV = 0.0; // PHI

    double mDireccionX = 0.0; // X rot

    double fps = 30;

    boolean alternar = false;

    SimpleUniverse mUniverso = null;
    BranchGroup mEscena = null;

    long mFrameCounter = 0;

    long mCurrentTime = 0;

    KeyboardInput mInput = null;

    CamaraM mCamara = null;

    boolean mAKey = false;
    boolean mZKey = false;

    boolean mSKey = false;
    boolean mDKey = false;

    double mAnguloHorizonte = 0.0;

    boolean mDebug = false;

    TransformGroup mDebugBall = null;
    TransformGroup mDebugBallB = null;

    TransformGroup mEjes = null;

    double mDX = 0.0;
    double mDY = 0.0;
    double mDZ = 0.0;

    double mDAV = 0.0; // diferencial de angulo en el timon vertical

    double mDAX = 0.0; // diferencial de angulo en el timon eje X

    boolean mEstadoValido = true;

    PickTool picker = null;

    public TimonBehavior(TransformGroup tgI, TransformGroup tgD) {
        mTimonI = tgI;
        mTimonD = tgD;

        //wakeupOne = new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED);

        framewake = new WakeupOnElapsedFrames(0);

        wakeupCondition = framewake;

    }

    public void initialize() {
        //Establish initial wakeup criteria
        wakeupOn(wakeupCondition);
    }

    //Override Behavior's stimulus method to handle the event.

    public void processStimulus(Enumeration criteria) {

        /* WakeupOnAWTEvent ev;
        WakeupCriterion genericEvt;
        AWTEvent[] events;

        while (criteria.hasMoreElements()) {
            genericEvt = (WakeupCriterion)criteria.nextElement();

            if (genericEvt instanceof WakeupOnAWTEvent) {
                ev = (WakeupOnAWTEvent)genericEvt;
                events = ev.getAWTEvent();
                //processAWTEvent(events);
            }
        }*/

        //Set wakeup criteria for next time

        mFrameCounter++;
        long temp = System.currentTimeMillis();

        if ((temp - mCurrentTime) >= 1000) {

            mCurrentTime = temp;

            fps = mFrameCounter;

            mFrameCounter = 0;

            System.out.println("FPS: " + fps);

        }

        actualizarEstado();
        animar();
        wakeupOn(wakeupCondition);


    }

    //Process a keyboard event

    private void processAWTEvent(AWTEvent[] events) {
        for (int n = 0; n < events.length; n++) {
            if (events[n] instanceof KeyEvent) {
                KeyEvent eventKey = (KeyEvent)events[n];

                if (eventKey.getID() == KeyEvent.KEY_PRESSED) {

                    int keyCode = eventKey.getKeyCode();

                    switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        //mAnguloActual += mAnguloPaso;
                        mKeyLeft = true;
                        break;

                    case KeyEvent.VK_RIGHT:
                        //mAnguloActual -= mAnguloPaso;
                        mKeyRight = true;
                        break;

                    case KeyEvent.VK_UP:

                        mKeyUp = true;
                        break;

                    case KeyEvent.VK_DOWN:

                        mKeyDown = true;
                        break;
                    }

                } else if (eventKey.getID() == KeyEvent.KEY_RELEASED) {

                    int keyCode = eventKey.getKeyCode();


                    switch (keyCode) {
                    case KeyEvent.VK_LEFT:
                        //mAnguloActual += mAnguloPaso;
                        mKeyLeft = false;
                        break;

                    case KeyEvent.VK_RIGHT:
                        //mAnguloActual -= mAnguloPaso;
                        mKeyRight = false;
                        break;

                    case KeyEvent.VK_UP:

                        mKeyUp = false;
                        break;

                    case KeyEvent.VK_DOWN:

                        mKeyDown = false;
                        break;

                    }

                }

            }
        }
    }

    void actualizarEstado() {

        mInput.poll();

        mKeyUp =
                mInput.keyDownOnce(KeyEvent.VK_UP) || mInput.keyDown(KeyEvent.VK_UP);
        mKeyDown =
                mInput.keyDownOnce(KeyEvent.VK_DOWN) || mInput.keyDown(KeyEvent.VK_DOWN);
        mKeyLeft =
                mInput.keyDownOnce(KeyEvent.VK_LEFT) || mInput.keyDown(KeyEvent.VK_LEFT);
        mKeyRight =
                mInput.keyDownOnce(KeyEvent.VK_RIGHT) || mInput.keyDown(KeyEvent.VK_RIGHT);

        mAKey =
                mInput.keyDownOnce(KeyEvent.VK_A) || mInput.keyDown(KeyEvent.VK_A);
        mZKey =
                mInput.keyDownOnce(KeyEvent.VK_Z) || mInput.keyDown(KeyEvent.VK_Z);

        mSKey =
                mInput.keyDownOnce(KeyEvent.VK_S) || mInput.keyDown(KeyEvent.VK_S);
        mDKey =
                mInput.keyDownOnce(KeyEvent.VK_D) || mInput.keyDown(KeyEvent.VK_D);


        if (mKeyRight) {

            switch (mEstadoTimon) {
            case Zero:
                mEstadoTimon = Estado.Up;
                break;

            case FromUpToZero:
                mEstadoTimon = Estado.Up;
                break;

            case Down:
                mEstadoTimon = Estado.FromDownToZero;
                break;

            }


        } else if (mKeyLeft) {

            switch (mEstadoTimon) {
            case Zero:
                mEstadoTimon = Estado.Down;
                break;

            case FromDownToZero:
                mEstadoTimon = Estado.Down;
                break;

            case Up:
                mEstadoTimon = Estado.FromUpToZero;
                break;

            }


        } else {

            switch (mEstadoTimon) {
            case Up:
                mEstadoTimon = Estado.FromUpToZero;
                break;

            case Down:
                mEstadoTimon = Estado.FromDownToZero;
                break;

            }
        }

        if (mKeyUp) {

            switch (mEstado) {
            case Zero:
                mEstado = Estado.Up;
                break;

            case FromUpToZero:
                mEstado = Estado.Up;
                break;

            case Down:
                mEstado = Estado.FromDownToZero;
                break;

            }


        } else if (mKeyDown) {

            switch (mEstado) {
            case Zero:
                mEstado = Estado.Down;
                break;

            case FromDownToZero:
                mEstado = Estado.Down;
                break;

            case Up:
                mEstado = Estado.FromUpToZero;
                break;

            }

        } else {

            switch (mEstado) {
            case Up:
                mEstado = Estado.FromUpToZero;
                mTiempo = 0;
                break;

            case Down:
                mEstado = Estado.FromDownToZero;
                mTiempo = 0;
                break;

            }

        }
    }

    private void girarRuedas(boolean pAdelante) {

        if (mVelocidad <= 0) {
            return;
        }

        mRps = mVelocidad / (2 * Math.PI * mRadioRueda);

        mRadps = mRps * (Math.PI * 2);

        mDAngulo = mRadps / fps;

        if (pAdelante) {
            mAnguloRuedas -= mDAngulo;
        } else {
            mAnguloRuedas += mDAngulo;
        }

        Transform3D giro = new Transform3D();

        giro.rotZ(mAnguloRuedas);

        if (!(mRuedaDI == null || mRuedaDD == null || mRuedaTI == null ||
              mRuedaTD == null)) {

            mRuedaDI.setTransform(giro);
            mRuedaDD.setTransform(giro);
            mRuedaTI.setTransform(giro);
            mRuedaTD.setTransform(giro);

        }

    }

    public void actualizarCamara() {

        Transform3D posicion = new Transform3D();

        mCarro.getTransform(posicion);

        Vector3d pos = new Vector3d();

        posicion.get(pos);

        System.out.println("Posicion " + pos.toString());

        mCamara.mover(pos);

        if (mEstado == Estado.Down || mEstado == Estado.FromDownToZero) {
            mCamara.rotar(Math.PI + mDireccion - mAnguloActual * 0.6);
        } else {
            mCamara.rotar(mDireccion + mAnguloActual * 0.6);
        }

        mCamara.actualizar();

    }

    void timonVertical() {

        if (!(mDX != 0 || mDY != 0 || mDZ != 0)) {
            return;
        }
        
        if (mDebug || true) {

            Point3d puntoRDD = new Point3d(0, 0, 0);
            Point3d puntoRDI = new Point3d(0, 0, 0);

            Point3d puntoRTD = new Point3d(0, 0, 0);
            Point3d puntoRTI = new Point3d(0, 0, 0);

            Transform3D trans = new Transform3D();

            this.mRuedaDD.getLocalToVworld( trans );
            trans.transform( puntoRDD );
            
            this.mRuedaDI.getLocalToVworld( trans );
            trans.transform( puntoRDI );
            
            this.mRuedaTD.getLocalToVworld( trans );
            trans.transform( puntoRTD );
            
            this.mRuedaTI.getLocalToVworld( trans );
            trans.transform( puntoRTI );
            
            Point3d puntoRDIN = (Point3d)puntoRDI.clone();
            Point3d puntoRDDN = (Point3d)puntoRDD.clone();
            Point3d puntoRTIN = (Point3d)puntoRTI.clone();
            Point3d puntoRTDN = (Point3d)puntoRTD.clone();

            /* Ruedas Traseras */

            Double zRi = null;
            Double zRd = null;

            try {

                PickRay r = new PickRay(puntoRTI, new Vector3d(0, -1, 0));
                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                Point3d punto = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(punto);
                puntoRTIN = punto;
                zRi = punto.getY();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }

            try {

                PickRay r = new PickRay(puntoRTD, new Vector3d(0, -1, 0));
                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                Point3d punto = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(punto);
                puntoRTDN = punto;
                zRd = punto.getY();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }

            double zO = puntoRTD.getY();
            double zMax = Math.max(zRi, zRd);

            if (mEstado == Estado.Zero) {
                                
                
            } else {

                if (zRi == null || zRd == null) {
                    
                    this.mEstadoValido = false;
                    
                } else if (zMax > (zO )) {
                    mDY = zMax - (zO );
                } else if (zMax < (zO)) {
                    mDY = zMax - (zO );
                }

            }
            /* Fin Ruedas Traseras */

            /* Ajuste del Angulo Vertical */

            Double zDi = null;
            Double zDd = null;

            try {

                PickRay r = new PickRay(puntoRDI, new Vector3d(0, -1, 0));
                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                Point3d punto = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(punto);
                puntoRDIN = (Point3d)punto.clone();
                zDi = punto.getY();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }

            try {

                PickRay r = new PickRay(puntoRDD, new Vector3d(0, -1, 0));
                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                Point3d punto = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(punto);
                puntoRDDN = (Point3d)punto.clone();
                zDd = punto.getY();

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }

            double zDO = puntoRDD.getY();
            double zDMax = Math.max(zDi, zDd);

            if (mEstado == Estado.Zero) {
                
            } else {

                if (zDi == null || zDd == null) {
                    
                    this.mEstadoValido = false;
                    
                } else if (zDMax > (zDO - 0.6)) {

                    double ang = (zDMax - (zDO - 0.6)) / 4.6;

                    ang = Math.asin(ang);

                    //mDAV = ang;

                } else if (zDMax < (zDO - 0.6)) {

                    double ang = (zDMax - (zDO - 0.6)) / 4.6;

                    ang = Math.asin(ang);

                   // mDAV = ang;

                }

            }

            /* Fin Ajuste del Angulo Vertical */

            /* Ajuste Angulo en el eje X */


            double dZ = puntoRDIN.getY() - puntoRDDN.getY();
            System.out.println("punto A " + puntoRDIN.toString() + "\npunto B " + puntoRDDN.toString() );
            System.out.println("Diferencial en Z " + dZ);
            double atan2 = Math.atan2(dZ, 1);
            if (mEstado != Estado.Zero) {

                if (Math.abs(Math.abs(mDireccionX) - Math.abs(atan2)) >
                    0.001) {

                   /* mDireccionX = atan2;
                    System.out.println( "DireccionX " + atan2 );*/

                    /*mDireccionX = atan2;

                    Transform3D t = new Transform3D();

                    t.rotX(mDireccionX);

                    mTimonX.setTransform(t);*/

                }

            }

        }

        /* Fin Ajuste Angulo en el Eje X */

        /*try {

                Point3d actual = (Point3d)puntoRDI.clone();

                PickRay r = new PickRay(puntoRDI, new Vector3d(0, -1, 0));

                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);

                Point3d punto = res.getClosestIntersectionPoint();

                res.getLocalToVWorld().transform(punto);

                double d = actual.getY() - punto.getY();

                if( d > 0.2 ) {

                    double ang = d / 4.6 ;

                    ang = Math.asin( ang );

                    ang = ang / 1000 ;

                    mDireccionV -= ang;

                    Transform3D t = new Transform3D();
                    t.rotZ(mDireccionV);

                    mTimonVertical.setTransform(t);

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }*/


        mDebug = false;

        if (mAKey) {

            mDireccionV += 0.02;

            Transform3D t = new Transform3D();
            t.rotZ(mDireccionV);

            mTimonVertical.setTransform(t);

        } else if (mZKey) {

            mDireccionV -= 0.02;

            Transform3D t = new Transform3D();
            t.rotZ(mDireccionV);

            mTimonVertical.setTransform(t);

        }

        if (mSKey) {

            mDireccionX += 0.02;

            Transform3D t = new Transform3D();
            t.rotX(mDireccionX);

            mTimonX.setTransform(t);

        } else if (mDKey) {

            this.mDireccionX -= 0.02;

            Transform3D t = new Transform3D();
            t.rotX(mDireccionX);

            mTimonX.setTransform(t);

        }

    }

    void timon() {

        /*if (mVelocidad <= 0) {
            return;
        }*/

        /*        double distancia = mVelocidad / fps;

        double distanciaH = Math.sin(mAnguloActual) * distancia;

        double distanciaV = 6.3 - 1.7;

        double tanA = distanciaH / distanciaV;

        double angulo = Math.atan(tanA); */

        double angulo =
            (mAnguloActual / (Math.PI / 9)) * (mVelocidad / mVelMaxima) *
            ( (Math.PI / 6) / fps);

        if (mAnguloActual != 0) {

            if (mEstado == Estado.Up || mEstado == Estado.FromUpToZero) {
                mDireccion += angulo;
            }

            if (mEstado == Estado.Down || mEstado == Estado.FromDownToZero) {
                mDireccion -= angulo;
            }

            Transform3D t3d = new Transform3D();
            t3d.rotY(mDireccion);
            mTimonHorizontal.setTransform(t3d);

        }

    }

    void aplicarCambioDeEstado() {

        if (mEstadoValido == true) {

            Transform3D pos = new Transform3D();

            mCarro.getTransform(pos);

            Vector3f punto = new Vector3f();

            pos.get(punto);

            punto.x += mDX;
            punto.y += mDY;
            punto.z += mDZ;

            pos.set(punto);

            mCarro.setTransform(pos);


            Transform3D tx = new Transform3D();

            tx.rotX(mDireccionX);

            mTimonX.setTransform(tx);


            if (mDAV != 0) {
                mDireccionV += mDAV;

                mDAV = 0;

                Transform3D t = new Transform3D();

                t.rotZ(mDireccionV);

                mTimonVertical.setTransform(t);
            }

        }

    }

    void adelante() {

        mTiempo += 1.0 / fps;

        double temp = mVelocidad;
        mVelocidad = mVelocidad + mAceleracion * mTiempo;

        if (mVelocidad > mVelMaxima) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mDireccionV) * mVelocidad;
        double velocidadZY = Math.sin(mDireccionV) * mVelocidad;

        double dx = velocidadXZ / fps * Math.cos(mDireccion);
        double dy = velocidadZY / fps;
        double dz = velocidadXZ / fps * Math.sin(mDireccion);

        mDX = dx;
        mDY = dy;
        mDZ = -dz;

        /*Transform3D pos = new Transform3D();

        mCarro.getTransform(pos);

        Vector3f punto = new Vector3f();

        pos.get(punto);

        punto.x += dx;
        punto.y += dy;
        punto.z -= dz;

        pos.set(punto);

        mCarro.setTransform(pos);*/

        girarRuedas(true);

    }

    void atras() {

        mTiempo += 1.0 / fps;

        double temp = mVelocidad;
        mVelocidad = mVelocidad + mAceleracion * mTiempo;

        if (mVelocidad > mVelMaximaR) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mDireccionV) * mVelocidad;
        double velocidadZY = Math.sin(mDireccionV) * mVelocidad;

        double dx = velocidadXZ / fps * Math.cos(mDireccion);
        double dy = velocidadZY / fps;
        double dz = velocidadXZ / fps * Math.sin(mDireccion);

        mDX = -dx;
        mDY = -dy;
        mDZ = dz;
        /*Transform3D pos = new Transform3D();

        mCarro.getTransform(pos);

        Vector3f punto = new Vector3f();

        pos.get(punto);

        punto.x -= dx;
        punto.y -= dy;
        punto.z += dz;

        pos.set(punto);

        mCarro.setTransform(pos);*/

        girarRuedas(false);

    }

    void desAdelante() {

        mTiempo += 1.0 / fps;

        mVelocidad = mVelocidad - mDAceleracion * mTiempo;

        double velocidadXZ = Math.cos(mDireccionV) * mVelocidad;
        double velocidadZY = Math.sin(mDireccionV) * mVelocidad;

        double dx = velocidadXZ / fps * Math.cos(mDireccion);
        double dy = velocidadZY / fps;
        double dz = velocidadXZ / fps * Math.sin(mDireccion);

        mDX = dx;
        mDY = dy;
        mDZ = -dz;

        /*Transform3D pos = new Transform3D();

        mCarro.getTransform(pos);

        Vector3f punto = new Vector3f();

        pos.get(punto);

        punto.x += dx;
        punto.y += dy;
        punto.z -= dz;

        pos.set(punto);

        mCarro.setTransform(pos);*/

        girarRuedas(true);

        if (mVelocidad <= 0) {
            mEstado = Estado.Zero;
            mVelocidad = 0;
            mTiempo = 0;
            mDX = 0.0;
            mDY = 0.0;
            mDZ = 0.0;
        }

    }

    void desAtras() {

        mTiempo += 1.0 / fps;

        mVelocidad = mVelocidad - mDAceleracion * mTiempo;

        double velocidadXZ = Math.cos(mDireccionV) * mVelocidad;
        double velocidadZY = Math.sin(mDireccionV) * mVelocidad;

        double dx = velocidadXZ / fps * Math.cos(mDireccion);
        double dy = velocidadZY / fps;
        double dz = velocidadXZ / fps * Math.sin(mDireccion);

        mDX = -dx;
        mDY = -dy;
        mDZ = dz;
        /*Transform3D pos = new Transform3D();

        mCarro.getTransform(pos);

        Vector3f punto = new Vector3f();

        pos.get(punto);

        punto.x -= dx;
        punto.y -= dy;
        punto.z += dz;

        pos.set(punto);

        mCarro.setTransform(pos);*/

        girarRuedas(false);

        if (mVelocidad <= 0) {
            mEstado = Estado.Zero;
            mVelocidad = 0;
            mTiempo = 0;
            mDX = 0.0;
            mDY = 0.0;
            mDZ = 0.0;
        }

    }

    void izquierda() {

        if (mAnguloActual < Math.PI / 9) {
            this.mAnguloActual += mAnguloPaso;

            Transform3D t3d = new Transform3D();

            t3d.rotY(mAnguloActual);

            mTimonI.setTransform(t3d);
            mTimonD.setTransform(t3d);
        }

    }

    void derecha() {

        if (mAnguloActual > -Math.PI / 9) {

            this.mAnguloActual -= mAnguloPaso;

            Transform3D t3d = new Transform3D();

            t3d.rotY(mAnguloActual);

            mTimonI.setTransform(t3d);
            mTimonD.setTransform(t3d);

        }

    }

    void desIzquierda() {

        if (mAnguloActual > 0) {

            this.mAnguloActual -= mAnguloPaso;

        } else {

            mAnguloActual = 0;
            mEstadoTimon = Estado.Zero;

        }

        Transform3D t3d = new Transform3D();

        t3d.rotY(mAnguloActual);

        mTimonI.setTransform(t3d);
        mTimonD.setTransform(t3d);

    }

    void desDerecha() {

        if (mAnguloActual < 0) {

            this.mAnguloActual += mAnguloPaso;

        } else {

            mAnguloActual = 0;
            mEstadoTimon = Estado.Zero;

        }

        Transform3D t3d = new Transform3D();

        t3d.rotY(mAnguloActual);

        mTimonI.setTransform(t3d);
        mTimonD.setTransform(t3d);

    }

    public void animar() {

        mEstadoValido = true;

        switch (mEstadoTimon) {
        case Up:
            derecha();
            break;

        case Down:
            izquierda();
            break;

        case FromUpToZero:
            desDerecha();
            break;

        case FromDownToZero:
            desIzquierda();
            break;
        }

        switch (mEstado) {
        case Up:
            adelante();
            break;

        case Down:
            atras();
            break;

        case FromUpToZero:
            desAdelante();
            break;

        case FromDownToZero:
            desAtras();
            break;
        }

        timon();

        timonVertical();
        //actualizarCamara();

        aplicarCambioDeEstado();

    }

    public void setCarro(TransformGroup mCarro) {
        this.mCarro = mCarro;
    }

    public TransformGroup getCarro() {
        return mCarro;
    }

    public void setRuedaDI(TransformGroup mRuedaDI) {
        this.mRuedaDI = mRuedaDI;
    }

    public TransformGroup getRuedaDI() {
        return mRuedaDI;
    }

    public void setRuedaDD(TransformGroup mRuedaDD) {
        this.mRuedaDD = mRuedaDD;
    }

    public TransformGroup getRuedaDD() {
        return mRuedaDD;
    }

    public void setRuedaTI(TransformGroup mRuedaTI) {
        this.mRuedaTI = mRuedaTI;
    }

    public TransformGroup getRuedaTI() {
        return mRuedaTI;
    }

    public void setRuedaTD(TransformGroup mRuedaTD) {
        this.mRuedaTD = mRuedaTD;
    }

    public TransformGroup getRuedaTD() {
        return mRuedaTD;
    }

    public void setTimonHorizontal(TransformGroup mTimonHorizontal) {
        this.mTimonHorizontal = mTimonHorizontal;
    }

    public TransformGroup getTimonHorizontal() {
        return mTimonHorizontal;
    }

    public void setUniverso(SimpleUniverse mUniverso) {
        this.mUniverso = mUniverso;
    }

    public SimpleUniverse getUniverso() {
        return mUniverso;
    }

    public void setEscena(BranchGroup mEscena) {
        picker = new PickTool(mEscena);
        this.mEscena = mEscena;
    }

    public BranchGroup getEscena() {
        return mEscena;
    }

    public void setInput(KeyboardInput mInput) {
        this.mInput = mInput;
    }

    public KeyboardInput getInput() {
        return mInput;
    }

    public void setCamara(CamaraM mCamara) {
        this.mCamara = mCamara;
    }

    public CamaraM getCamara() {
        return mCamara;
    }

    public void setTimonVertical(TransformGroup mTimonVertical) {
        this.mTimonVertical = mTimonVertical;
    }

    public TransformGroup getTimonVertical() {
        return mTimonVertical;
    }

    public void setTimonX(TransformGroup mTimonX) {
        this.mTimonX = mTimonX;
    }

    public TransformGroup getTimonX() {
        return mTimonX;
    }

    public void setDebug(boolean mDebug) {
        this.mDebug = mDebug;
    }

    public boolean isDebug() {
        return mDebug;
    }

    public void setDebugBall(TransformGroup mDebugBall) {
        this.mDebugBall = mDebugBall;
    }

    public TransformGroup getDebugBall() {
        return mDebugBall;
    }

    public void setEjes(TransformGroup mEjes) {
        this.mEjes = mEjes;
    }

    public TransformGroup getEjes() {
        return mEjes;
    }

    public void setDebugBallB(TransformGroup mDebugBallB) {
        this.mDebugBallB = mDebugBallB;
    }

    public TransformGroup getDebugBallB() {
        return mDebugBallB;
    }

    public enum Estado {
        Up,
        FromUpToZero,
        Zero,
        FromDownToZero,
        Down;
    }
}
