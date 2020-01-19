package test;


import org.scijava.java3d.utils.behaviors.vp.OrbitBehavior;
import org.scijava.java3d.utils.universe.SimpleUniverse;

import java.awt.event.KeyEvent;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Enumeration;

import org.scijava.java3d.Behavior;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Node;
import org.scijava.java3d.PickInfo;
import org.scijava.java3d.PickSegment;
import org.scijava.java3d.Switch;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;
import org.scijava.java3d.WakeupCondition;
import org.scijava.java3d.WakeupOnElapsedFrames;

import javax.swing.JLabel;

import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Vector3d;


public class CarBehavior extends Behavior {

    OrbitBehavior mBehavior = null;

    private WakeupOnElapsedFrames framewake = null;
    private WakeupCondition wakeupCondition = null;

    private long mFrameCounter = 0;
    private long mCurrentTime = 0;
    private double mFps = 0.0;

    KeyboardInput mInput = null;

    /* teclas */

    boolean mKeyUp = false;
    boolean mKeyDown = false;
    boolean mKeyLeft = false;
    boolean mKeyRight = false;

    boolean mKeyA = false;
    boolean mKeyZ = false;

    boolean mKeyS = false;
    boolean mKeyD = false;

    boolean mKeyW = false;
    boolean mKeyE = false;

    boolean mKeyR = false;
    boolean mKeyF = false;

    boolean mKeyF1 = false;
    boolean mKeyF2 = false;
    boolean mKeyF3 = false;
    boolean mKeyF4 = false;

    boolean mKeyF5 = false;
    boolean mKeyF6 = false;
    
    boolean mKeyF7 = false;
    boolean mKeyF8 = false;
    /* fin teclas */

    /* posicion del plano base */

    TransformGroup mPlanoBase = null;

    double mDx = 0.0;
    double mDy = 0.0;
    double mDz = 0.0;

    /* fin posicion del plano base */


    boolean mHayCambioDeEstado = false;

    /* ruedas */

    Node mRuedaDelanteraDerecha = null;
    Node mRuedaDelanteraIzquierda = null;
    Node mRuedaTraseraDerecha = null;
    Node mRuedaTraseraIzquierda = null;

    /* fin ruedas */

    TransformGroup mGiroY = null;
    double mAnguloY = 0.0;
    double mDAY = 0.0;

    TransformGroup mGiroZ = null;
    double mAnguloZ = 0.0;
    double mDAZ = 0.0;

    TransformGroup mGiroX = null;
    double mAnguloX = 0.0;
    double mDAX = 0.0;

    /* Z pos adj  */
    boolean mSeguirTerreno = true;

    long mStartFrames = 0;
    /* mEscena */

    BranchGroup mEscena = null;

    double mDistanciaEntreEjesVertical = 1.85;
    double mDistanicaEntreEjesHorizontal = 1.0;
    double mRadioDeLaRueda = 0.2413;

    /* modelo de movimiento */

    double mVelocidad = 0.0;
    double mVelocidadMaxima = 12.5; // m/s
    double mVelocidadMaximaEnReversa = 6.25; // m/s
    double mAceleracion = 0.005; // m/s^2
    double mAceleracionFrenado = 0.05; // m/s^2

    Estado mEstado = Estado.Zero;
    double mTiempo = 0.0;

    /* fin modelo de movimiento  */

    /* timon */

    Estado mEstadoTimon = Estado.Zero;

    TransformGroup mTimonRuedaDelanteraDerecha = null;
    TransformGroup mTimonRuedaDelanteraIzquierda = null;

    double mAnguloTimon = 0.0;
    double mAnguloDePasoDelTimonPorSegundo = 0.4;
    double mAnguloDePasoDelTimon = 0.005;
    double mDAnguloTimon = 0.0;

    /* fin timon */

    /* giro de las ruedas */

    TransformGroup mGiroRuedaDelanteraDerecha = null;
    TransformGroup mGiroRuedaDelanteraIzquierda = null;
    TransformGroup mGiroRuedaTraseraDerecha = null;
    TransformGroup mGiroRuedaTraseraIzquierda = null;

    double mAnguloDeLasRuedas = 0.0;
    double mDAnguloDeLasRuedas = 0.0;

    /* fin giro de las ruedas */

    TransformGroup mDebugBall = null;


    /* Camara */

    CameraManager mCamara = null;
    SimpleUniverse mUniverso = null;

    boolean mMoverCamara = true;

    /* fin Camara */

    Node mCuerpoDelCarro = null;

    /* view controller */

    boolean mCorriendo = false;

    boolean mPreCorriendo = false;

    boolean mPostCorriendo = false;

    int mPreCorriendoEstado = 0;

    JLabel mTimerLabel = null;

    JLabel mSpeedLabel = null;
    
    JLabel mLaps = null;

    long mTiempoDeInicio = 0;

    Date mTimeSpan = new Date();

    DecimalFormat mKPHF = new DecimalFormat("00.00");

    int mLapNumber = 1;

    int mNumLaps = 2;
    Switch mTextos = null;

    SimpleDateFormat formatter = new SimpleDateFormat("mm:ss:S");
    
    

    public void incrementarNumeroDeVuelta() {

        mLapNumber++;        
        if (mLapNumber == mNumLaps + 1) {
            System.out.println("FIN DEL JUEGO");
            detenerG();
        }

    }

    public void detenerG() {
        mPostCorriendo = true;
        mTiempoDeInicio = System.nanoTime();
    }

    public void iniciar() {

        mPreCorriendo = true;
        mCorriendo = false;        
        mPostCorriendo = false;
        
        mLapNumber = 1;

        mTiempoDeInicio = System.nanoTime();

        detener();

        Transform3D posinicial = new Transform3D();        

        this.mGiroY.setTransform(posinicial);
        this.mGiroZ.setTransform(posinicial);
        this.mGiroX.setTransform(posinicial);
        this.mTimonRuedaDelanteraDerecha.setTransform(posinicial);
        this.mTimonRuedaDelanteraIzquierda.setTransform(posinicial);
        
        posinicial.setTranslation(new Vector3d(0,-0.2,0));
        this.mPlanoBase.setTransform(posinicial);

        this.mAnguloTimon = 0.0;
        this.mAnguloX = 0.0;
        this.mAnguloY = 0.0;
        this.mAnguloZ = 0.0;


        mBehavior.setRotationCenter(new Point3d(0, 0, 0));
        mCamara.rotarY(mAnguloTimon);
        mCamara.actualizar();
        
        mTextos.setWhichChild( Switch.CHILD_NONE );

    }

    public void reiniciar() {

        mCorriendo = false;
        mPreCorriendo = false;
        mPostCorriendo = false;
        
        mLapNumber = 1;
        
        detener();

        Transform3D posinicial = new Transform3D();        

        this.mGiroY.setTransform(posinicial);
        this.mGiroZ.setTransform(posinicial);
        this.mGiroX.setTransform(posinicial);
        this.mTimonRuedaDelanteraDerecha.setTransform(posinicial);
        this.mTimonRuedaDelanteraIzquierda.setTransform(posinicial);

        posinicial.setTranslation(new Vector3d(0,-0.2,0));
        this.mPlanoBase.setTransform(posinicial);
        
        this.mAnguloTimon = 0.0;
        this.mAnguloX = 0.0;
        this.mAnguloY = 0.0;
        this.mAnguloZ = 0.0;


        mBehavior.setRotationCenter(new Point3d(0, 0, 0));
        mCamara.rotarY(mAnguloTimon);
        mCamara.actualizar();
        
        mTextos.setWhichChild( Switch.CHILD_NONE );
        
    }
    
    public CarBehavior() {

        framewake = new WakeupOnElapsedFrames(0);
        wakeupCondition = framewake;
        //wakeupCondition = new WakeupOnElapsedTime( (long)( 1000 / 30 ) );

    }

    public void initialize() {
        wakeupOn(wakeupCondition);
    }

    public void processStimulus(Enumeration enumeration) {

        mFrameCounter++;

        if (mStartFrames < 10) {
            mStartFrames++;
        }

        long temp = System.nanoTime();

        if (mStartFrames > 2) {

            long dt = temp - mCurrentTime;

            mFps = 1000000000.0 / (double)dt;

            if (mPreCorriendo) {

                dt = temp - mTiempoDeInicio;
                dt = dt / 1000000;
                if (dt >= 1000 && dt < 2000) {
                    this.mTextos.setWhichChild(0);
                } else if (dt >= 2000 && dt < 3000) {
                    this.mTextos.setWhichChild(1);
                } else if (dt >= 3000 && dt < 4000) {
                    this.mTextos.setWhichChild(2);
                } else if (dt >= 4000 && dt < 5000) {
                    this.mTextos.setWhichChild(3);
                } else if (dt >= 5000) {
                    this.mTextos.setWhichChild(Switch.CHILD_NONE);
                    mTiempoDeInicio = System.nanoTime();
                    mPreCorriendo = false;
                    mCorriendo = true;
                }

            } else if (mCorriendo) {

                dt = temp - mTiempoDeInicio;

                mTimeSpan.setTime(dt / 1000000);

                mAnguloDePasoDelTimon = mAnguloDePasoDelTimonPorSegundo / mFps;

                if (mPostCorriendo) {

                    this.mTextos.setWhichChild(4);

                    dt = temp - mTiempoDeInicio;
                    dt = dt / 1000000;

                    if (dt > 2000) {
                        this.mTextos.setWhichChild(Switch.CHILD_NONE);
                        mCorriendo = false;
                        mPostCorriendo = false;
                        detener();
                    }

                }
            }

            animar();
        }

        mCurrentTime = temp;

        wakeupOn(wakeupCondition);

    }

    void detectarChoque() {

        Point3d bomperDerecha = new Point3d(2.5, 0.4, 0.4);
        Point3d finBomperDerecha = new Point3d(2.5, 0.4, 0.65);

        Point3d bomperIzquierda = new Point3d(2.5, 0.4, -0.4);
        Point3d finBomperIzquierda = new Point3d(2.5, 0.4, -0.65);

        Point3d bomperTDerecha = new Point3d(-0.7, 0.1, 0.4);
        Point3d finTBomperDerecha = new Point3d(-0.7, 0.1, 0.65);

        Point3d bomperTIzquierda = new Point3d(-0.7, 0.1, -0.4);
        Point3d finTBomperIzquierda = new Point3d(0.7, 0.1, -0.65);

        Transform3D trans = new Transform3D();

        mCuerpoDelCarro.getLocalToVworld(trans);

        trans.transform(bomperDerecha);
        trans.transform(finBomperDerecha);

        trans.transform(bomperIzquierda);
        trans.transform(finBomperIzquierda);

        trans.transform(bomperTDerecha);
        trans.transform(finTBomperDerecha);

        trans.transform(bomperTIzquierda);
        trans.transform(finTBomperIzquierda);

        //trans = new Transform3D();

        //trans.setTranslation( new Vector3d( bomperTIzquierda.getX() , bomperTIzquierda.getY() , bomperTIzquierda.getZ() ) );

        //mDebugBall.setTransform( trans );

        boolean hayColision = false;

        try {

            PickSegment b = new PickSegment(bomperDerecha, finBomperDerecha);

            PickInfo res =
                mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.NODE, b);

            if (res == null) {

            } else {

                hayColision = true;
                mDAY = Math.PI / 8;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        try {

            PickSegment b =
                new PickSegment(bomperIzquierda, finBomperIzquierda);

            PickInfo res =
                mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.NODE, b);

            if (res == null) {

            } else {
                hayColision = true;
                mDAY = -Math.PI / 8;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        try {

            PickSegment b =
                new PickSegment(bomperTIzquierda, finTBomperIzquierda);

            PickInfo res =
                mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.NODE, b);

            if (res == null) {

            } else {
                hayColision = true;

                Point3d origenRuedaTraseraDerecha = new Point3d(0, 0, 1);

                Transform3D t = new Transform3D();

                mRuedaTraseraDerecha.getLocalToVworld(t);

                t.transform(origenRuedaTraseraDerecha);

                mPlanoBase.getTransform(t);
                Vector3d posicion = new Vector3d();
                t.get(posicion);

                mDx = origenRuedaTraseraDerecha.getX() - posicion.x;
                mDy = origenRuedaTraseraDerecha.getY() - posicion.y;
                mDz = origenRuedaTraseraDerecha.getZ() - posicion.z;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

        try {

            PickSegment b = new PickSegment(bomperTDerecha, finTBomperDerecha);

            PickInfo res =
                mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.NODE, b);

            if (res == null) {

            } else {
                hayColision = true;

                Transform3D t = new Transform3D();

                Point3d origenRuedaTraseraIzquierda = new Point3d(0, 0, -1);

                mRuedaTraseraIzquierda.getLocalToVworld(t);

                t.transform(origenRuedaTraseraIzquierda);

                mPlanoBase.getTransform(t);
                Vector3d posicion = new Vector3d();
                t.get(posicion);

                mDx = origenRuedaTraseraIzquierda.getX() - posicion.x;
                mDy = origenRuedaTraseraIzquierda.getY() - posicion.y;
                mDz = origenRuedaTraseraIzquierda.getZ() - posicion.z;
                //posicion.x += mDx;
                //posicion.y += mDy;
                //posicion.z += mDz;

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }


        if (hayColision) {
            System.out.println("Colision");
            //detener();
        }

    }

    void detener() {

        if (mEstado != Estado.Zero) {
            mDx = -mDx;
            mDy = -mDy;
            mDz = -mDz;
            mVelocidad = 0;
            mTiempo = 0;
            mDAX = 0;
            mDAY = 0;
            mDAZ = 0;

            mEstado = Estado.Zero;
        }

    }

    void movimiento() {

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

        /*if (mKeyUp) {

            mDx = 0.01;
            mHayCambioDeEstado = true;
        }*/

        /*if (mKeyDown) {

            mDx = -0.01;
            mHayCambioDeEstado = true;
        }*/

        /*        if (mKeyLeft) {

            mDz = -0.01;
            mHayCambioDeEstado = true;
        }

        if (mKeyRight) {

            mDz = 0.01;
            mHayCambioDeEstado = true;
        } */

        if (mKeyA) {

            mDy = 0.01;
            mHayCambioDeEstado = true;
        }

        if (mKeyZ) {

            mDy = -0.01;
            mHayCambioDeEstado = true;

        }

    }

    void giro() {

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

        if (mKeyS) {

            mDAY = 0.01;
            mHayCambioDeEstado = true;

        }

        if (mKeyD) {

            mDAY = -0.01;
            mHayCambioDeEstado = true;

        }

        if (mKeyW) {

            mDAX = -0.01;
            mHayCambioDeEstado = true;

        }

        if (mKeyE) {

            mDAX = 0.01;
            mHayCambioDeEstado = true;

        }

        if (mKeyR) {

            mDAZ = 0.01;
            mHayCambioDeEstado = true;

        }

        if (mKeyF) {

            mDAZ = -0.01;
            mHayCambioDeEstado = true;

        }

    }

    void timon() {

        double angulo =
            (mAnguloTimon / (Math.PI / 9)) * (mVelocidad / mVelocidadMaxima) *
            ((Math.PI / 2) / mFps);

        if (mAnguloTimon != 0) {

            if (mEstado == Estado.Up || mEstado == Estado.FromUpToZero) {
                mDAY = angulo;
            }

            if (mEstado == Estado.Down || mEstado == Estado.FromDownToZero) {
                mDAY = -angulo;
            }

        }

    }

    void seguirTerreno() {

        try {

            Vector3d direccionRayo = new Vector3d(0, -1, 0);

            Point3d origenRuedaDelanteraDerecha =
                new Point3d(0, mRadioDeLaRueda * 2, 0);
            Point3d origenRuedaDelanteraIzquierda =
                new Point3d(0, mRadioDeLaRueda * 2, 0);
            Point3d origenRuedaTraseraDerecha =
                new Point3d(0, mRadioDeLaRueda * 2, 0);
            Point3d origenRuedaTraseraIzquierda =
                new Point3d(0, mRadioDeLaRueda * 2, 0);

            Point3d finRuedaDelanteraDerecha = new Point3d(0, -1.5, 0);
            Point3d finRuedaDelanteraIzquierda = new Point3d(0, -1.5, 0);
            Point3d finRuedaTraseraDerecha = new Point3d(0, -1.5, 0);
            Point3d finRuedaTraseraIzquierda = new Point3d(0, -1.5, 0);


            Transform3D t = new Transform3D();

            t.rotZ(mAnguloDeLasRuedas);
            t.invert();

            t.transform(origenRuedaDelanteraDerecha);
            t.transform(origenRuedaDelanteraIzquierda);
            t.transform(origenRuedaTraseraDerecha);
            t.transform(origenRuedaTraseraIzquierda);

            t.transform(finRuedaDelanteraDerecha);
            t.transform(finRuedaDelanteraIzquierda);
            t.transform(finRuedaTraseraDerecha);
            t.transform(finRuedaTraseraIzquierda);

            mRuedaDelanteraDerecha.getLocalToVworld(t);
            t.transform(origenRuedaDelanteraDerecha);
            t.transform(finRuedaDelanteraDerecha);

            mRuedaDelanteraIzquierda.getLocalToVworld(t);
            t.transform(origenRuedaDelanteraIzquierda);
            t.transform(finRuedaDelanteraIzquierda);

            mRuedaTraseraDerecha.getLocalToVworld(t);
            t.transform(origenRuedaTraseraDerecha);
            t.transform(finRuedaTraseraDerecha);

            mRuedaTraseraIzquierda.getLocalToVworld(t);
            t.transform(origenRuedaTraseraIzquierda);
            t.transform(finRuedaTraseraIzquierda);

            Point3d sueloRuedaDelanteraDerecha = null;
            Point3d sueloRuedaDelanteraIzquierda = null;
            Point3d sueloRuedaTraseraDerecha = null;
            Point3d sueloRuedaTraseraIzquierda = null;

            try {

                //PickRay r = new PickRay(origenRuedaDelanteraDerecha, direccionRayo);

                PickSegment r =
                    new PickSegment(origenRuedaDelanteraDerecha, finRuedaDelanteraDerecha);

                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                sueloRuedaDelanteraDerecha = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(sueloRuedaDelanteraDerecha);

            } catch (Exception ex) {
                System.out.println("Error suelo Rueda Delantera Derecha ");
            } finally {
            }

            try {

                /*PickRay r =
                    new PickRay(origenRuedaDelanteraIzquierda, direccionRayo);*/

                PickSegment r =
                    new PickSegment(origenRuedaDelanteraIzquierda, finRuedaDelanteraIzquierda);

                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                sueloRuedaDelanteraIzquierda =
                        res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(sueloRuedaDelanteraIzquierda);

            } catch (Exception ex) {
                System.out.println("Error suelo Rueda Delantera Izquierda");
            } finally {
            }

            try {

                /*PickRay r =
                    new PickRay(origenRuedaTraseraDerecha, direccionRayo);*/

                PickSegment r =
                    new PickSegment(origenRuedaTraseraDerecha, finRuedaTraseraDerecha);

                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                sueloRuedaTraseraDerecha = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(sueloRuedaTraseraDerecha);

            } catch (Exception ex) {
                System.out.println("Error suelo Rueda Trasera Derecha ");
            } finally {
            }

            try {

                /*PickRay r =
                    new PickRay(origenRuedaTraseraIzquierda, direccionRayo);*/

                PickSegment r =
                    new PickSegment(origenRuedaTraseraIzquierda, finRuedaTraseraIzquierda);

                PickInfo res =
                    mEscena.pickClosest(PickInfo.PICK_GEOMETRY, PickInfo.LOCAL_TO_VWORLD |
                                        PickInfo.CLOSEST_INTERSECTION_POINT |
                                        PickInfo.CLOSEST_GEOM_INFO, r);
                sueloRuedaTraseraIzquierda = res.getClosestIntersectionPoint();
                res.getLocalToVWorld().transform(sueloRuedaTraseraIzquierda);

            } catch (Exception ex) {
                System.out.println("Error suelo Rueda Delantera Derecha ");
            } finally {
            }


            if (sueloRuedaDelanteraDerecha == null ||
                sueloRuedaDelanteraIzquierda == null ||
                sueloRuedaTraseraDerecha == null ||
                sueloRuedaTraseraIzquierda == null) {
                if (mStartFrames < 5) {
                    return;
                }
                mSeguirTerreno = false;
                System.out.println("Seguimiento de terreno desactivado");
                return;
            }


            /*System.out.println("Origen RDD " + origenRuedaDelanteraDerecha.toString() );
            System.out.println("Suelo RDD " + sueloRuedaDelanteraDerecha.toString() );            */

            /*Transform3D pos = new Transform3D();

            pos.setTranslation( new Vector3d( sueloRuedaDelanteraDerecha.getX() , sueloRuedaDelanteraDerecha.getY() , sueloRuedaDelanteraDerecha.getZ() ) );

            mDebugBall.setTransform( pos );*/

            /* ajuste posicion Z */

            double zDelanteraMaxima =
                Math.max(sueloRuedaDelanteraDerecha.getY(),
                         sueloRuedaDelanteraIzquierda.getY());

            double zTraseraMaxima =
                Math.max(sueloRuedaTraseraDerecha.getY(), sueloRuedaTraseraIzquierda.getY());

            t = new Transform3D();
            mPlanoBase.getTransform(t);
            Vector3d posicion = new Vector3d();
            t.get(posicion);

            double zActual = posicion.y;

            double dZ = zTraseraMaxima - zActual + mRadioDeLaRueda * 1.05;

            if (Math.abs(dZ) > 0.005) {

                mDy = dZ;
                mHayCambioDeEstado = true;

            }

            /* fin ajuste posicion Z */

            /* Angulo Z Vertical */

            double dZVertical = zDelanteraMaxima - zTraseraMaxima;

            double anguloVertical =
                Math.atan2(dZVertical, mDistanciaEntreEjesVertical);

            //System.out.println("Angulo Vertical " + anguloVertical);

            double delta = anguloVertical - mAnguloZ;

            double absDelta = Math.abs(delta);

            if (absDelta > 0.05 && absDelta <= Math.PI / 4.0) {
                mDAZ = delta;
                mHayCambioDeEstado = true;
            } else if (absDelta > Math.PI / 4.0) {
                mDAZ = (delta / absDelta) * Math.PI / 4.0;
                mHayCambioDeEstado = true;
            }

            /* fin Angulo Z Vertical */


            /* ajuste angulo X */

            double zIzquierdaMaxima = sueloRuedaDelanteraIzquierda.getY();
            double zDerechaMaxima = sueloRuedaDelanteraDerecha.getY();

            double dZHorizontal = zIzquierdaMaxima - zDerechaMaxima;

            double anguloHorizontal =
                Math.atan2(dZHorizontal, mDistanicaEntreEjesHorizontal);

            //System.out.println("Angulo Horizontal " + anguloHorizontal);

            delta = anguloHorizontal - mAnguloX;
            //System.out.println("Delta Horizontal " + delta);

            absDelta = Math.abs(delta);

            if (absDelta > 0.05 && absDelta <= Math.PI / 4) {
                mDAX = delta;
                mHayCambioDeEstado = true;
            }

            /* fin ajuste angulo X */

            //mSeguirTerreno = false;

        } catch (Exception ex) {
            System.out.println("Error en seguirTerreno");
        } finally {
        }


    }

    void adelante() {

        mTiempo += 1.0 / mFps;

        double temp = mVelocidad;
        mVelocidad = mVelocidad + mAceleracion * mTiempo;

        if (mVelocidad > mVelocidadMaxima) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mAnguloZ) * mVelocidad;

        double velocidadZY = Math.sin(mAnguloZ) * mVelocidad;

        double dx = velocidadXZ / mFps * Math.cos(mAnguloY);
        double dy = velocidadZY / mFps;
        double dz = velocidadXZ / mFps * Math.sin(mAnguloY);

        mDx = dx;
        mDy = dy;
        mDz = -dz;

        if (mDx != 0 || mDy != 0 || mDz != 0) {
            girarRuedas(true);
            mHayCambioDeEstado = true;
        }

    }

    void atras() {

        mTiempo += 1.0 / mFps;

        double temp = mVelocidad;
        mVelocidad = mVelocidad + mAceleracion * mTiempo;

        if (mVelocidad > mVelocidadMaximaEnReversa) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mAnguloZ) * mVelocidad;

        double velocidadZY = Math.sin(mAnguloZ) * mVelocidad;

        double dx = velocidadXZ / mFps * Math.cos(mAnguloY);
        double dy = velocidadZY / mFps;
        double dz = velocidadXZ / mFps * Math.sin(mAnguloY);

        mDx = -dx;
        mDy = -dy;
        mDz = dz;

        if (mDx != 0 || mDy != 0 || mDz != 0) {
            girarRuedas(false);
            mHayCambioDeEstado = true;
        }

    }

    void desAdelante() {

        mTiempo += 1.0 / mFps;

        double temp = mVelocidad;

        if (mKeyDown) {
            mVelocidad = mVelocidad - (mAceleracionFrenado * 2) * mTiempo;
        } else {
            mVelocidad = mVelocidad - mAceleracionFrenado * mTiempo;
        }

        if (mVelocidad > mVelocidadMaxima) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mAnguloZ) * mVelocidad;

        double velocidadZY = Math.sin(mAnguloZ) * mVelocidad;

        double dx = velocidadXZ / mFps * Math.cos(mAnguloY);
        double dy = velocidadZY / mFps;
        double dz = velocidadXZ / mFps * Math.sin(mAnguloY);

        mDx = dx;
        mDy = dy;
        mDz = -dz;

        if (mDx != 0 || mDy != 0 || mDz != 0) {
            girarRuedas(true);
            mHayCambioDeEstado = true;
        }

        if (mVelocidad <= 0) {
            mHayCambioDeEstado = false;
            mEstado = Estado.Zero;
            mVelocidad = 0;
            mTiempo = 0;
            mDx = 0.0;
            mDy = 0.0;
            mDz = 0.0;
            mDAnguloDeLasRuedas = 0.0;
        }

    }

    void desAtras() {

        mTiempo += 1.0 / mFps;

        double temp = mVelocidad;
        mVelocidad = mVelocidad - mAceleracionFrenado * mTiempo;

        if (mVelocidad > mVelocidadMaximaEnReversa) {
            mVelocidad = temp;
        }

        double velocidadXZ = Math.cos(mAnguloZ) * mVelocidad;

        double velocidadZY = Math.sin(mAnguloZ) * mVelocidad;

        double dx = velocidadXZ / mFps * Math.cos(mAnguloY);
        double dy = velocidadZY / mFps;
        double dz = velocidadXZ / mFps * Math.sin(mAnguloY);

        mDx = -dx;
        mDy = -dy;
        mDz = dz;

        if (mDx != 0 || mDy != 0 || mDz != 0) {
            mHayCambioDeEstado = true;
            girarRuedas(false);
        }

        if (mVelocidad <= 0) {
            mEstado = Estado.Zero;
            mVelocidad = 0;
            mTiempo = 0;
            mDx = 0.0;
            mDy = 0.0;
            mDz = 0.0;
        }

    }

    void izquierda() {

        if (mAnguloTimon < Math.PI / 9) {

            mDAnguloTimon = mAnguloDePasoDelTimon;
            mHayCambioDeEstado = true;

        }

    }

    void derecha() {

        if (mAnguloTimon > -Math.PI / 9) {

            mDAnguloTimon = -mAnguloDePasoDelTimon;
            mHayCambioDeEstado = true;

        }

    }

    void desIzquierda() {

        if (mAnguloTimon > 0) {

            if (mKeyRight) {
                mDAnguloTimon = -mAnguloDePasoDelTimon * 2;
            } else {
                mDAnguloTimon = -mAnguloDePasoDelTimon;
            }

            mHayCambioDeEstado = true;

        } else {

            mAnguloTimon = 0;
            mDAnguloTimon = 0;
            mEstadoTimon = Estado.Zero;

        }

    }

    void desDerecha() {

        if (mAnguloTimon < 0) {

            if (mKeyLeft) {
                mDAnguloTimon = mAnguloDePasoDelTimon * 2;
            } else {
                mDAnguloTimon = mAnguloDePasoDelTimon;
            }

            mHayCambioDeEstado = true;

        } else {

            mAnguloTimon = 0;
            mDAnguloTimon = 0;
            mEstadoTimon = Estado.Zero;

        }

    }

    private void girarRuedas(boolean pAdelante) {

        if (mVelocidad <= 0) {
            return;
        }

        double Rps = mVelocidad / (2 * Math.PI * mRadioDeLaRueda);

        double Radps = Rps * (Math.PI * 2);

        double dAngulo = Radps / mFps;

        if (pAdelante) {
            mDAnguloDeLasRuedas = -dAngulo;
        } else {
            mDAnguloDeLasRuedas = dAngulo;
        }

    }

    public void actualizarEstado() {

        mDx = 0;
        mDy = 0;
        mDz = 0;
        mDAX = 0;
        mDAY = 0;
        mDAZ = 0;
        mDAnguloDeLasRuedas = 0.0;
        mDAnguloTimon = 0.0;

        mInput.poll();

        mKeyUp =
                mInput.keyDownOnce(KeyEvent.VK_UP) || mInput.keyDown(KeyEvent.VK_UP);
        mKeyDown =
                mInput.keyDownOnce(KeyEvent.VK_DOWN) || mInput.keyDown(KeyEvent.VK_DOWN);
        mKeyLeft =
                mInput.keyDownOnce(KeyEvent.VK_LEFT) || mInput.keyDown(KeyEvent.VK_LEFT);
        mKeyRight =
                mInput.keyDownOnce(KeyEvent.VK_RIGHT) || mInput.keyDown(KeyEvent.VK_RIGHT);

        mKeyA =
                mInput.keyDownOnce(KeyEvent.VK_A) || mInput.keyDown(KeyEvent.VK_A);
        mKeyZ =
                mInput.keyDownOnce(KeyEvent.VK_Z) || mInput.keyDown(KeyEvent.VK_Z);

        mKeyS =
                mInput.keyDownOnce(KeyEvent.VK_S) || mInput.keyDown(KeyEvent.VK_S);
        mKeyD =
                mInput.keyDownOnce(KeyEvent.VK_D) || mInput.keyDown(KeyEvent.VK_D);

        mKeyW =
                mInput.keyDownOnce(KeyEvent.VK_W) || mInput.keyDown(KeyEvent.VK_W);
        mKeyE =
                mInput.keyDownOnce(KeyEvent.VK_E) || mInput.keyDown(KeyEvent.VK_E);

        mKeyR =
                mInput.keyDownOnce(KeyEvent.VK_R) || mInput.keyDown(KeyEvent.VK_R);
        mKeyF =
                mInput.keyDownOnce(KeyEvent.VK_F) || mInput.keyDown(KeyEvent.VK_F);


        mKeyF1 = mInput.keyDownOnce(KeyEvent.VK_F1);

        mKeyF2 = mInput.keyDownOnce(KeyEvent.VK_F2);

        mKeyF3 = mInput.keyDownOnce(KeyEvent.VK_F3);

        mKeyF4 = mInput.keyDownOnce(KeyEvent.VK_F4);

        mKeyF5 = mInput.keyDownOnce(KeyEvent.VK_F5);

        mKeyF6 = mInput.keyDownOnce(KeyEvent.VK_F6);
        
        mKeyF7 = mInput.keyDownOnce(KeyEvent.VK_F7);

        mKeyF8 = mInput.keyDownOnce(KeyEvent.VK_F8);

        if (mInput.keyDownOnce(KeyEvent.VK_X)) {
            System.exit(0);
        }
        
        if( mKeyF7 ) {
            mSeguirTerreno = !mSeguirTerreno;
        } else if( mKeyF8 ) {
            mMoverCamara = !mMoverCamara;
        }

        if (mKeyF5) {
            iniciar();
        } else if( mKeyF6 ) {
            reiniciar();
        }

        if (mKeyF1) {
            mCamara.modoA();
        } else if (mKeyF2) {
            mCamara.modoB();
        } else if (mKeyF3) {
            mCamara.modoC();
        } else if (mKeyF4) {
            mCamara.modoD();
        }


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

        giro();

        timon();

        movimiento();

    }

    public void animar() {

        actualizarEstado();
        if (mSeguirTerreno && mHayCambioDeEstado && mCorriendo) {
            seguirTerreno();
            detectarChoque();
            aplicarCambioDeEstado();
        }
        actualizarUI();

    }

    public void aplicarCambioDeEstado() {

        if (mHayCambioDeEstado) {

            if (mDx != 0 || mDy != 0 || mDz != 0) {
                Transform3D t = new Transform3D();
                mPlanoBase.getTransform(t);
                Vector3d posicion = new Vector3d();
                t.get(posicion);
                posicion.x += mDx;
                posicion.y += mDy;
                posicion.z += mDz;
                t.set(posicion);
                mPlanoBase.setTransform(t);

                if (mMoverCamara) {
                    mBehavior.setRotationCenter(new Point3d(posicion.x,
                                                            posicion.y,
                                                            posicion.z));
                }
            }

            if (mDAX != 0) {
                Transform3D t = new Transform3D();
                mAnguloX += mDAX;
                t.rotX(mAnguloX);
                mGiroX.setTransform(t);
            }

            if (mDAY != 0) {
                Transform3D t = new Transform3D();
                mAnguloY += mDAY;
                t.rotY(mAnguloY);
                mGiroY.setTransform(t);

            }

            if (mDAZ != 0) {
                Transform3D t = new Transform3D();
                mAnguloZ += mDAZ;
                t.rotZ(mAnguloZ);
                mGiroZ.setTransform(t);

            }

            if (mDAnguloDeLasRuedas != 0) {

                Transform3D giro = new Transform3D();

                mAnguloDeLasRuedas += mDAnguloDeLasRuedas;

                giro.rotZ(mAnguloDeLasRuedas);

                if (!(mGiroRuedaDelanteraDerecha == null ||
                      mGiroRuedaDelanteraIzquierda == null ||
                      mGiroRuedaTraseraDerecha == null ||
                      mGiroRuedaTraseraIzquierda == null)) {

                    mGiroRuedaDelanteraDerecha.setTransform(giro);
                    mGiroRuedaTraseraIzquierda.setTransform(giro);
                    mGiroRuedaDelanteraIzquierda.setTransform(giro);
                    mGiroRuedaTraseraDerecha.setTransform(giro);

                }

            }

            if (mDAnguloTimon != 0) {

                Transform3D t = new Transform3D();

                mAnguloTimon += mDAnguloTimon;
                t.rotY(mAnguloTimon);

                mTimonRuedaDelanteraDerecha.setTransform(t);
                mTimonRuedaDelanteraIzquierda.setTransform(t);


            }

            if (mMoverCamara) {

                if (mEstado == Estado.Down ||
                    mEstado == Estado.FromDownToZero) {
                    mCamara.rotarY(Math.PI - mAnguloTimon * 0.4);
                } else {
                    mCamara.rotarY(mAnguloTimon * 0.4);
                }
                mCamara.actualizar();

            }
            mHayCambioDeEstado = false;

        }
    }

    public void actualizarUI() {

        if (mTimerLabel != null) {

            if (mPostCorriendo || ! mCorriendo ) {
                mTimerLabel.setText("00:00:000");
            } else {
                mTimerLabel.setText(formatter.format(this.mTimeSpan));
            }

        }


        if (mSpeedLabel != null) {

            if (mPostCorriendo || !mCorriendo ) {
                mSpeedLabel.setText("00.00 KM/h");
            } else {
                double kph = mVelocidad * 3600.0 / 1000.0;

                mSpeedLabel.setText(mKPHF.format(kph) + " KM/h");
            }
            
        }
        
        if( mLaps != null ) {
            
            if (mPostCorriendo || !mCorriendo ) {
                mLaps.setText("Vuelta:");
            } else {
                mLaps.setText("Vuelta: " + this.mLapNumber + " de: " + this.mNumLaps );
            }
            
        }

    }

    public void setInput(KeyboardInput mInput) {
        this.mInput = mInput;
    }

    public KeyboardInput getInput() {
        return mInput;
    }

    public void setPlanoBase(TransformGroup mPlanoBase) {
        this.mPlanoBase = mPlanoBase;
    }

    public TransformGroup getPlanoBase() {
        return mPlanoBase;
    }

    public void setRuedaDelanteraDerecha(Node mRuedaDelanteraDerecha) {
        this.mRuedaDelanteraDerecha = mRuedaDelanteraDerecha;
    }

    public Node getRuedaDelanteraDerecha() {
        return mRuedaDelanteraDerecha;
    }

    public void setRuedaDelanteraIzquierda(Node mRuedaDelanteraIzquierda) {
        this.mRuedaDelanteraIzquierda = mRuedaDelanteraIzquierda;
    }

    public Node getRuedaDelanteraIzquierda() {
        return mRuedaDelanteraIzquierda;
    }

    public void setRuedaTraseraDerecha(Node mRuedaTraseraDerecha) {
        this.mRuedaTraseraDerecha = mRuedaTraseraDerecha;
    }

    public Node getRuedaTraseraDerecha() {
        return mRuedaTraseraDerecha;
    }

    public void setRuedaTraseraIzquierda(Node mRuedaTraseraIzquierda) {
        this.mRuedaTraseraIzquierda = mRuedaTraseraIzquierda;
    }

    public Node getRuedaTraseraIzquierda() {
        return mRuedaTraseraIzquierda;
    }

    public void setGiroY(TransformGroup mGiroY) {
        this.mGiroY = mGiroY;
    }

    public TransformGroup getGiroY() {
        return mGiroY;
    }

    public void setGiroZ(TransformGroup mGiroZ) {
        this.mGiroZ = mGiroZ;
    }

    public TransformGroup getGiroZ() {
        return mGiroZ;
    }

    public void setGiroX(TransformGroup mGiroX) {
        this.mGiroX = mGiroX;
    }

    public TransformGroup getGiroX() {
        return mGiroX;
    }

    public void setSeguirTerreno(boolean mSeguirTerreno) {
        this.mSeguirTerreno = mSeguirTerreno;
    }

    public boolean isSeguirTerreno() {
        return mSeguirTerreno;
    }

    public void setEscena(BranchGroup mEscena) {
        this.mEscena = mEscena;
    }

    public BranchGroup getEscena() {
        return mEscena;
    }

    public void setDebugBall(TransformGroup mDebugBall) {
        this.mDebugBall = mDebugBall;
    }

    public TransformGroup getDebugBall() {
        return mDebugBall;
    }

    public void setGiroRuedaDelanteraDerecha(TransformGroup mGiroRuedaDelanteraDerecha) {
        this.mGiroRuedaDelanteraDerecha = mGiroRuedaDelanteraDerecha;
    }

    public TransformGroup getGiroRuedaDelanteraDerecha() {
        return mGiroRuedaDelanteraDerecha;
    }

    public void setGiroRuedaDelanteraIzquierda(TransformGroup mGiroRuedaDelanteraIzquierda) {
        this.mGiroRuedaDelanteraIzquierda = mGiroRuedaDelanteraIzquierda;
    }

    public TransformGroup getGiroRuedaDelanteraIzquierda() {
        return mGiroRuedaDelanteraIzquierda;
    }

    public void setGiroRuedaTraseraDerecha(TransformGroup mGiroRuedaTraseraDerecha) {
        this.mGiroRuedaTraseraDerecha = mGiroRuedaTraseraDerecha;
    }

    public TransformGroup getGiroRuedaTraseraDerecha() {
        return mGiroRuedaTraseraDerecha;
    }

    public void setGiroRuedaTraseraIzquierda(TransformGroup mGiroRuedaTraseraIzquierda) {
        this.mGiroRuedaTraseraIzquierda = mGiroRuedaTraseraIzquierda;
    }

    public TransformGroup getGiroRuedaTraseraIzquierda() {
        return mGiroRuedaTraseraIzquierda;
    }

    public void setTimonRuedaDelanteraDerecha(TransformGroup mTimonRuedaDelanteraDerecha) {
        this.mTimonRuedaDelanteraDerecha = mTimonRuedaDelanteraDerecha;
    }

    public TransformGroup getTimonRuedaDelanteraDerecha() {
        return mTimonRuedaDelanteraDerecha;
    }

    public void setTimonRuedaDelanteraIzquierda(TransformGroup mTimonRuedaDelanteraIzquierda) {
        this.mTimonRuedaDelanteraIzquierda = mTimonRuedaDelanteraIzquierda;
    }

    public TransformGroup getTimonRuedaDelanteraIzquierda() {
        return mTimonRuedaDelanteraIzquierda;
    }

    public void setUniverso(SimpleUniverse mUniverso) {
        mCamara = new CameraManager(mUniverso);
        this.mUniverso = mUniverso;
    }

    public SimpleUniverse getUniverso() {
        return mUniverso;
    }

    public void setCuerpoDelCarro(Node mCuerpoDelCarro) {

        if (mCamara != null) {
            mCamara.setCarBody(mCuerpoDelCarro);
        } else {
            System.out.println("CAMARA ES NULL ANTES QUE EL CUERPO DEL CARRO ");
        }
        this.mCuerpoDelCarro = mCuerpoDelCarro;
    }

    public Node getCuerpoDelCarro() {
        return mCuerpoDelCarro;
    }

    public void setBehavior(OrbitBehavior mBehavior) {
        this.mBehavior = mBehavior;
    }

    public OrbitBehavior getBehavior() {
        return mBehavior;
    }

    public void setMoverCamara(boolean mMoverCamara) {
        this.mMoverCamara = mMoverCamara;
    }

    public boolean isMoverCamara() {
        return mMoverCamara;
    }

    public void setTimerLabel(JLabel mTimerLabel) {
        this.mTimerLabel = mTimerLabel;
    }

    public JLabel getTimerLabel() {
        return mTimerLabel;
    }

    public void setSpeedLabel(JLabel mSpeedLabel) {
        this.mSpeedLabel = mSpeedLabel;
    }

    public JLabel getSpeedLabel() {
        return mSpeedLabel;
    }

    public void setTextos(Switch mTextos) {
        this.mTextos = mTextos;
    }

    public Switch getTextos() {
        return mTextos;
    }

    public void setLaps(JLabel mLaps) {
        this.mLaps = mLaps;
    }

    public JLabel getLaps() {
        return mLaps;
    }

    public enum Estado {
        Up,
        FromUpToZero,
        Zero,
        FromDownToZero,
        Down;
    }

}
