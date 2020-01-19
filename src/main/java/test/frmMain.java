package test;


import org.scijava.java3d.utils.behaviors.vp.OrbitBehavior;
import org.scijava.java3d.utils.geometry.Box;
import org.scijava.java3d.utils.geometry.Sphere;
import org.scijava.java3d.utils.geometry.Text2D;
import org.scijava.java3d.utils.picking.PickIntersection;
import org.scijava.java3d.utils.picking.PickResult;
import org.scijava.java3d.utils.picking.PickTool;
import org.scijava.java3d.utils.universe.SimpleUniverse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.scijava.java3d.AmbientLight;
import org.scijava.java3d.Appearance;
import org.scijava.java3d.Background;
import org.scijava.java3d.BackgroundSound;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.BranchGroup;
import org.scijava.java3d.Canvas3D;
import org.scijava.java3d.DirectionalLight;
import org.scijava.java3d.MediaContainer;
import org.scijava.java3d.Node;
import org.scijava.java3d.PointSound;
import org.scijava.java3d.Switch;
import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;

import org.scijava.vecmath.Color3f;
import org.scijava.vecmath.Point2f;
import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.Point3f;
import org.scijava.vecmath.Vector3d;
import org.scijava.vecmath.Vector3f;


public class frmMain extends JFrame {

    private BranchGroup mEscena = null;
    private SimpleUniverse mUniverso = null;

    OrbitBehavior mOrbit = null;

    private JPanel jPanel1 = new JPanel();

    private JLabel mTimer = new JLabel();
    private JLabel mSpeed = new JLabel();

    Canvas3D canvas = null;

    CarBehavior CB = null;
    private BorderLayout borderLayout1 = new BorderLayout();
    private JLabel mLaps = new JLabel();

    public frmMain() {
        try {
            jbInit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {

        setUndecorated(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, (int)screenSize.getWidth(), (int)screenSize.getHeight());
        this.getContentPane().setLayout(new BorderLayout());        
        this.setTitle("Java3D");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.black);
        jPanel1.setBackground(Color.black);
        jPanel1.setLayout(borderLayout1);
        mTimer.setBackground(Color.black);
        mTimer.setFont(new Font("Tahoma", 1, 30));
        mTimer.setForeground(new Color(226, 117, 109));
        mTimer.setText("00:00:000");
        mTimer.setOpaque(true);
        mSpeed.setHorizontalAlignment(SwingConstants.RIGHT);
        mSpeed.setFont(new Font("Tahoma", 1, 30));
        mSpeed.setForeground(new Color(148, 255, 148));
        mSpeed.setBackground(Color.black);
        mSpeed.setOpaque(true);
        mSpeed.setText("00.00 KM/h");
        mSpeed.setHorizontalAlignment(SwingConstants.CENTER);
        mLaps.setText("Vuelta :");
        mLaps.setBackground(Color.black);
        mLaps.setOpaque(true);
        mLaps.setFont(new Font("Tahoma", 1, 30));
        mLaps.setForeground(Color.yellow);
        mLaps.setHorizontalAlignment(SwingConstants.LEFT);
        jPanel1.add(mTimer, BorderLayout.EAST);
        jPanel1.add(mSpeed, BorderLayout.CENTER);
        jPanel1.add(mLaps, BorderLayout.WEST);
        this.getContentPane().add(jPanel1, BorderLayout.NORTH);

        // crear el panel 3d
        agregarPanel3D();

    }

    private void agregarPanel3D() {

        Canvas3D panel =
            new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        canvas = panel;

        canvas.setDoubleBufferEnable(true);

        boolean anti = canvas.getSceneAntialiasingAvailable();

        SimpleUniverse universo = new SimpleUniverse(panel);

        mUniverso = universo;


        BranchGroup escena = new BranchGroup();

        mEscena = escena;

        universo.getViewingPlatform().setNominalViewingTransform();


        OrbitBehavior orbit = new OrbitBehavior(panel);

        orbit.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0,
                                                                 0.0),
                                                     Double.POSITIVE_INFINITY));

        universo.getViewingPlatform().setViewPlatformBehavior(orbit);

        mOrbit = orbit;

        escena.setCapability(BranchGroup.ALLOW_DETACH);

        mEscena = escena;

        universo.getViewingPlatform().setNominalViewingTransform();

        universo.getViewer().getView().setBackClipDistance(3000);

        Background bg = new Background();
        bg.setApplicationBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                                                   55));
        BranchGroup backGeoBranch = new BranchGroup();


        /*PhysicalBody myBody = new PhysicalBody();
        PhysicalEnvironment myEnvironment = new PhysicalEnvironment();
        JavaSoundMixer myMixer = new JavaSoundMixer(myEnvironment);
        myMixer.initialize();
        mUniverso.getViewer().getView().setPhysicalBody(myBody);
        mUniverso.getViewer().getView().setPhysicalEnvironment(myEnvironment);

        mEscena.addChild(this.addObjectSound("c:\\3d\\audio\\loop.aif",
                                             105));*/
        /*Sphere sphereObj =
            new Sphere(1.0f, Sphere.GENERATE_NORMALS | Sphere.GENERATE_NORMALS_INWARD |
                       Sphere.GENERATE_TEXTURE_COORDS |
                       Sphere.GENERATE_TEXTURE_COORDS_Y_UP, 45 , appL );

        Box bgBox = new Box( 0.5f, Box.GENERATE_NORMALS | Box.GENERATE_NORMALS_INWARD |
                       Box.GENERATE_TEXTURE_COORDS |
                       Box.GENERATE_TEXTURE_COORDS_Y_UP, 45 , appL );*/

        //      Appearance backgroundApp = sphereObj.getAppearance();
        //backGeoBranch.addChild(sphereObj);
        /*backGeoBranch.addChild(bgBox);
        bg.setGeometry(backGeoBranch);*/


        /*backGeoBranch.addChild(b);
        bg.setGeometry(backGeoBranch);*/
        mEscena.addChild(MapBuilder.getSkyBox());


        //        backgroundApp.setTexture(appL.getTexture());

        //normalSetup(panel);

        /* version 2 */


        /* fin version 2 */


        //mEscena.addChild(MapBuilder.test());
        //  mEscena.addChild( Tools.rotarZ( Math.PI / 12.0 , MapBuilder.getSegmentoRecto( 20 )  )  );

        dosetup(panel);

        /*TransformGroup dball = new TransformGroup();
        dball.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );
        dball.addChild( new Sphere(0.1f) );
        dball.setPickable( false );
        //car.setDebugBall( dball );

        dball.setCollidable( true );

        mEscena.addChild( dball );*/

        Node piso = MapBuilder.getGroundPlane();

        mEscena.addChild(Tools.trasladar(0, -9.2f, 0, piso));


        Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);

        BoundingSphere bounds =
            new BoundingSphere(new Point3d(0, 0, 0), 1000.0);

        AmbientLight ambientLightNode = new AmbientLight(lightColor);
        ambientLightNode.setInfluencingBounds(bounds);
        mEscena.addChild(ambientLightNode);

        Vector3f light1Direction = new Vector3f(1.0f, 1.0f, -5f);

        DirectionalLight light1 =
            new DirectionalLight(lightColor, light1Direction);
        light1.setInfluencingBounds(bounds);

        mEscena.addChild(light1);

        mEscena.compile();

        mUniverso.addBranchGraph(mEscena);

        //testPick();

        /*
        try {

            Scene x = frmMain.loadScene("c:\\3d\\850nR.obj");

            Scene y = frmMain.loadScene("c:\\3d\\rueda4.obj");

            Color3f lightColor = new Color3f(1.0f, 1.0f, 1.0f);

            BranchGroup bg = x.getSceneGroup();

            BranchGroup rg = y.getSceneGroup();

            Transform3D escalar = new Transform3D();

            TransformGroup g = new TransformGroup();

            g.setCapability( TransformGroup.ALLOW_TRANSFORM_WRITE );

            mGrupo = g;
            //escalar.setScale(0.001);

            g.setTransform( escalar );

            g.addChild( rg );


            mEscena = new BranchGroup() ;

            mEscena.addChild( bg );
            mEscena.addChild( g );

            BoundingSphere bounds =
                new BoundingSphere(new Point3d(0, 0, 0), 100.0);

            AmbientLight ambientLightNode = new AmbientLight(lightColor);
            ambientLightNode.setInfluencingBounds(bounds);
            mEscena.addChild(ambientLightNode);

            Vector3f light1Direction = new Vector3f(1.0f, 1.0f, -5f);

            DirectionalLight light1 =
                new DirectionalLight(lightColor, light1Direction);
            light1.setInfluencingBounds(bounds);

            mEscena.addChild(light1);


            mUniverso.addBranchGraph(mEscena);

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }

*/
        this.add(panel, BorderLayout.CENTER);

    }

    private void dosetup(Canvas3D panel) {
        Node RuedaDelanteraDerecha = CarBuilder.getRueda();
        Node RuedaDelanteraIzquierda = CarBuilder.getRueda();
        Node RuedaTraseraDerecha = CarBuilder.getRueda();
        Node RuedaTraseraIzquierda = CarBuilder.getRueda();

        TransformGroup giroRuedaDelanteraDerecha = new TransformGroup();
        giroRuedaDelanteraDerecha.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup giroRuedaDelanteraIzquierda = new TransformGroup();
        giroRuedaDelanteraIzquierda.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup giroRuedaTraseraDerecha = new TransformGroup();
        giroRuedaTraseraDerecha.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup giroRuedaTraseraIzquierda = new TransformGroup();
        giroRuedaTraseraIzquierda.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        giroRuedaDelanteraDerecha.addChild(RuedaDelanteraDerecha);
        giroRuedaDelanteraIzquierda.addChild(RuedaDelanteraIzquierda);
        giroRuedaTraseraDerecha.addChild(RuedaTraseraDerecha);
        giroRuedaTraseraIzquierda.addChild(RuedaTraseraIzquierda);

        TransformGroup timonRuedaDelanteraDerecha = new TransformGroup();
        timonRuedaDelanteraDerecha.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        TransformGroup timonRuedaDelanteraIzquierda = new TransformGroup();
        timonRuedaDelanteraIzquierda.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        timonRuedaDelanteraDerecha.addChild(giroRuedaDelanteraDerecha);
        timonRuedaDelanteraIzquierda.addChild(giroRuedaDelanteraIzquierda);

        TransformGroup giroX = new TransformGroup();
        giroX.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        giroX.addChild(Tools.trasladar(0, 0, 0.50f, giroRuedaTraseraDerecha));
        giroX.addChild(Tools.trasladar(0, 0, -0.50f,
                                       giroRuedaTraseraIzquierda));

        giroX.addChild(Tools.trasladar(1.85f, 0, 0.50f,
                                       timonRuedaDelanteraDerecha));
        giroX.addChild(Tools.trasladar(1.85f, 0, -0.50f,
                                       timonRuedaDelanteraIzquierda));

        giroX.addChild(Tools.trasladar(-0.6f, 0.50f, 0.18f,
                                       Tools.rotarY(Math.PI / 2,
                                                    CarBuilder.getRueda())));

        Node carBody = CarBuilder.getBody();
        giroX.addChild(carBody);

        TransformGroup giroZ = new TransformGroup();
        giroZ.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        giroZ.addChild(giroX);

        TransformGroup giroY = new TransformGroup();
        giroY.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        giroY.addChild(giroZ);

        TransformGroup planoBase = new TransformGroup();
        planoBase.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        planoBase.addChild(giroY);

        mEscena.addChild(planoBase);

        CarBehavior car = new CarBehavior();

        //Node pista = MapBuilder.getPistaB(); //MapBuilder.getPistaA();
        Node pista = MapBuilder.getPistaB();

        BranchGroup bg2 = new BranchGroup();

        bg2.addChild(Tools.trasladar(0, -0.65f, 0, pista));

        bg2.compile();

        mUniverso.addBranchGraph(bg2);

        car.setEscena(bg2);

        car.setTimerLabel(mTimer);

        car.setSpeedLabel(mSpeed);
        
        car.setLaps( mLaps );

        CB = car;

        car.setGiroX(giroX);
        car.setGiroZ(giroZ);
        car.setGiroY(giroY);

        car.setPlanoBase(planoBase);

        car.setRuedaDelanteraDerecha(RuedaDelanteraDerecha);
        car.setRuedaDelanteraIzquierda(RuedaDelanteraIzquierda);

        car.setRuedaTraseraDerecha(RuedaTraseraDerecha);
        car.setRuedaTraseraIzquierda(RuedaTraseraIzquierda);

        car.setGiroRuedaDelanteraDerecha(giroRuedaDelanteraDerecha);
        car.setGiroRuedaDelanteraIzquierda(giroRuedaDelanteraIzquierda);
        car.setGiroRuedaTraseraDerecha(giroRuedaTraseraDerecha);
        car.setGiroRuedaTraseraIzquierda(giroRuedaTraseraIzquierda);

        car.setTimonRuedaDelanteraDerecha(timonRuedaDelanteraDerecha);
        car.setTimonRuedaDelanteraIzquierda(timonRuedaDelanteraIzquierda);

        car.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                                                   Double.POSITIVE_INFINITY));

        car.setUniverso(mUniverso);
        car.setCuerpoDelCarro(carBody);
        car.setBehavior(mOrbit);

        Appearance fl = Tools.cargarTextura("c:\\3d\\fline.png");
        Appearance negro = Tools.generarApariencia(Color.BLACK);

        Box b =
            new Box(0.20f, 0.1f, 2.5f, Box.GENERATE_NORMALS | Box.GENERATE_TEXTURE_COORDS,
                    negro);

        b.getShape(Box.TOP).setAppearance(fl);
        b.setCollidable(true);
        b.setPickable(false);
        mEscena.addChild(Tools.trasladar(-1, -0.445, 0, b));

        CollisionDetector de = new CollisionDetector(b);
        de.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0),
                                                  Double.POSITIVE_INFINITY));
        de.setEnable(true);
        de.setCarB(car);

        mEscena.addChild(de);

        KeyboardInput in = new KeyboardInput();

        this.addKeyListener(in);

        panel.addKeyListener(in);

        car.setInput(in);

        mEscena.addChild(car);

        /* texto inicio */
        Color3f amarillo = new Color3f();
        amarillo.set(Color.YELLOW);
        Color3f rojo = new Color3f();
        rojo.set(Color.RED);
        Color3f naranja = new Color3f();
        naranja.set(Color.ORANGE);
        Color3f verde = new Color3f();
        verde.set(Color.GREEN);
        Text2D msgCero =
            new Text2D("INICIO!", verde, "Tahoma", 150, Font.BOLD);
        Text2D msgUno = new Text2D("1", naranja, "Tahoma", 150, Font.BOLD);
        Text2D msgDos = new Text2D("2", amarillo, "Tahoma", 150, Font.BOLD);
        Text2D msgTres = new Text2D("3", rojo, "Tahoma", 150, Font.BOLD);
        Text2D msgFin = new Text2D("FIN", verde, "Tahoma", 150, Font.BOLD);

        Switch sw = new Switch(Switch.CHILD_NONE);
        sw.setCapability(Switch.ALLOW_SWITCH_WRITE);

        sw.addChild(msgTres);
        sw.addChild(msgDos);
        sw.addChild(msgUno);
        sw.addChild(msgCero);
        sw.addChild(msgFin);

        car.setTextos(sw);

        mEscena.addChild(Tools.trasladar(4, 1.0, 0,
                                         Tools.rotarY(-Math.PI / 2, sw)));
        car.setEnable(true);
        
        car.reiniciar();
        
    }

    private void jButton1_actionPerformed(ActionEvent e) {
        // boton 1
        CB.setSeguirTerreno(!CB.isSeguirTerreno());
    }

    private void jButton2_actionPerformed(ActionEvent e) {
        // boton 2
        CB.setMoverCamara(!CB.isMoverCamara());
    }


}
