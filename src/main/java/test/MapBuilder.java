package test;

import org.scijava.java3d.utils.geometry.Cylinder;
import org.scijava.java3d.utils.geometry.GeometryInfo;

import org.scijava.java3d.utils.geometry.NormalGenerator;
import org.scijava.java3d.utils.geometry.Triangulator;

import org.scijava.java3d.utils.picking.PickTool;

import java.awt.Color;

import org.scijava.java3d.Appearance;
import org.scijava.java3d.BoundingSphere;
import org.scijava.java3d.GeometryArray;
import org.scijava.java3d.Group;
import org.scijava.java3d.Node;

import org.scijava.java3d.QuadArray;
import org.scijava.java3d.Shape3D;

import org.scijava.java3d.Transform3D;

import org.scijava.java3d.TransformGroup;

import org.scijava.vecmath.Point3d;
import org.scijava.vecmath.TexCoord2f;
import org.scijava.vecmath.Vector3d;
import org.scijava.vecmath.Vector3f;

public class MapBuilder {

    static double ancho = 6.0;
    static double semiAncho = ancho / 2.0;
    static double alto = 1.0;
    static double grosor = 0.2;

    static Point3d p0 = new Point3d(0, 0, -semiAncho);
    static Point3d p1 = new Point3d(0, 0, semiAncho);
    static Point3d p2 = new Point3d(0, alto, semiAncho);
    static Point3d p3 = new Point3d(0, alto, semiAncho - grosor);
    static Point3d p4 = new Point3d(0, grosor, semiAncho - grosor);
    static Point3d p5 = new Point3d(0, grosor, -(semiAncho - grosor));
    static Point3d p6 = new Point3d(0, alto, -(semiAncho - grosor));
    static Point3d p7 = new Point3d(0, alto, -semiAncho);

    static Point3d[] puntosBase = { p0, p1, p2, p3, p4, p5, p6, p7 };

    static Transform3D actual = new Transform3D();
    static Transform3D siguiente = new Transform3D();

    static Appearance appPista = null;

    static TexCoord2f t0 = new TexCoord2f( 1.0f , 68.0f / 166.0f );
    static TexCoord2f t1 = new TexCoord2f( 1.0f , 0.99f );
    static TexCoord2f t2 = new TexCoord2f( 0.0f , 0.99f );
    static TexCoord2f t3 = new TexCoord2f( 0.0f , 68.0f / 166.0f );
    
    static TexCoord2f t4 = new TexCoord2f( 0.001f , 0.0f );
    static TexCoord2f t5 = new TexCoord2f( 132.0f / 678.0f  , 0.0f );
    static TexCoord2f t6 = new TexCoord2f( 132.0f / 678.0f , 66.0f / 166.0f );
    static TexCoord2f t7 = new TexCoord2f( 0.001f , 66.0f / 166.0f );
    
    static TexCoord2f tA = new TexCoord2f( 0 , 0 );
    static TexCoord2f tB = new TexCoord2f( 1 , 0 );
    static TexCoord2f tC = new TexCoord2f( 1 , 1 );
    static TexCoord2f tD = new TexCoord2f( 0 , 1 );
    
    static double mLastX = 0.0;
    static double mLastY = 0.0;
    static double mLastZ = 0.0;
    
    static Node mLastNode = null;
    
    static void updateLast() {
        
        if( mLastNode == null ) {
            return;
        }
        
        Point3d p = new Point3d( 0 , 0 , 0 );        
        
        Transform3D t = new Transform3D();
        mLastNode.getLocalToVworld( t );
        
        actual.transform( p );
        t.transform( p );        
        
        mLastX = p.getX();
        mLastY = p.getY();
        mLastZ = p.getZ();
                        
    }
    
    public static Shape3D getSegmento() {

        Shape3D forma = null;

        Point3d[] ladoA = new Point3d[8];
        Point3d[] ladoB = new Point3d[8];

        for (int i = 0; i < 8; i++) {

            Point3d temp = (Point3d)puntosBase[i].clone();
            actual.transform(temp);
            ladoA[i] = temp;

            temp = (Point3d)puntosBase[i].clone();
            siguiente.transform(temp);
            ladoB[i] = temp;

        }
        
        int numCaras = 8;

        QuadArray data = new QuadArray(numCaras * 4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2 | QuadArray.NORMALS );
        
        Point3d[] coordenadas = { 
                                    ladoA[0] , ladoB[0] , ladoB[1] , ladoA[1],
                                    ladoA[1] , ladoB[1] , ladoB[2] , ladoA[2],
                                    ladoA[2] , ladoB[2] , ladoB[3] , ladoA[3],
                                    ladoA[3] , ladoB[3] , ladoB[4] , ladoA[4],
                                    ladoA[4] , ladoB[4] , ladoB[5] , ladoA[5], 
                                    ladoA[5] , ladoB[5] , ladoB[6] , ladoA[6], 
                                    ladoA[6] , ladoB[6] , ladoB[7] , ladoA[7], 
                                    ladoA[7] , ladoB[7] , ladoB[0] , ladoA[0],   
                                };
        
        
        
        TexCoord2f[] coordenadasT = 
                        {
                            t4,t5,t6,t7,
                            t4,t5,t6,t7,
                            t4,t5,t6,t7,
                            t4,t5,t6,t7,
                            t0,t1,t2,t3,
                            t4,t5,t6,t7,
                            t4,t5,t6,t7,
                            t4,t5,t6,t7
                         };
        
        data.setCoordinates(0 , coordenadas);
        data.setTextureCoordinates( 0 , 0 , coordenadasT );
        
        GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        
        info.reset(data);
        
        NormalGenerator ng = new NormalGenerator();

        ng.generateNormals(info);
        
        //Appearance app = Tools.generarApariencia( Color.BLUE );
        if( appPista == null ) {
            appPista = Tools.cargarTextura("c:\\3d\\pista.png");   
        }        
        
        forma = new Shape3D( info.getGeometryArray() , appPista);
        
        forma.setCapability(Shape3D.ENABLE_PICK_REPORTING);
        PickTool.setCapabilities(forma, PickTool.INTERSECT_FULL);
        
        forma.setCollidable(false);
        
        mLastNode = forma;
        
        return forma;

    }
    
    public static Node getSegmentoRecto( double pLongitud ) {
        
        Group g = new Group();
                        
        actual = new Transform3D();
                        
        int limite = (int)pLongitud;
        
        for( int i = 1 ; i < limite  ; i++ ) {
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( i , 0 , 0 ) );   
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );
            
        }
        
        if( (pLongitud - (double) limite ) > 0 ) {
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( pLongitud , 0 , 0 ) );               
            g.addChild( getSegmento() );
            actual.set(siguiente);
            
        }
            
        return g;
    }
    
    public static Node getPendiente( double pAltura , int pLongitud ) {
        
        Group g = new Group();
                        
        actual = new Transform3D();
        
        int limite = pLongitud + 1;
        
        double pasoAltura = pAltura / limite;
        
        for( int i = 1 ; i < limite  ; i++ ) {
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( i , i * pasoAltura , 0 ) );   
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );
            
        }
        
        
        
        return g;
        
    }
    
    public static Node getCaracol( int pAltura ) {
        
        Group g = new Group();
        
        actual = new Transform3D();
            
        Transform3D giro = new Transform3D();    
        
        int abs = Math.abs( pAltura );
            
        int numSegmentos = 60 *  abs ; 
        double maxAngle = abs * Math.PI * 2;
        double angStep = maxAngle / numSegmentos;
        double anguloRad = angStep;        
        
        double x = 0.0;
        double z = 0.0;
        
        double dx = 0.0;
        double dz = 0.0;
        double dy = (double)pAltura * 5 / (double)numSegmentos;
                    
        double radioGiro = 15.0;
        
        double xo = 0.0;        
        double zo = radioGiro;
        
        double y = 0.0;
            
        for( int i = 1 ; i <= numSegmentos ; i++  ) {
        
            anguloRad = angStep * i ;
            
            dx = Math.sin( anguloRad ) * radioGiro;
            dz = -Math.cos( anguloRad ) * radioGiro; 
            
            x = xo + dx;
            z = zo + dz;            
            y = y + dy;
            
            giro.rotY( -anguloRad );       
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( x , y , z ) );                                                
            siguiente.mul( giro );
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );
            
            //anguloRad += angStep;    
            
        }
        
        return g;
        
    }
    
    public static Node getCurvaDerecha( double pAngulo ) {
        
        Group g = new Group();
        
        actual = new Transform3D();
            
        Transform3D giro = new Transform3D();    
        
        double temp = pAngulo / ( Math.PI / 2 );
        temp = temp * 15.0;
        int numSegmentos = (int)temp; 
        
        double maxAngle = pAngulo;
        double angStep = maxAngle / numSegmentos;
        double anguloRad = angStep;        
        
        double x = 0.0;
        double z = 0.0;
        
        double dx = 0.0;
        double dz = 0.0;
        
        double radioGiro = 10.0;
        
        double xo = 0.0;        
        double zo = radioGiro;
        
            
        for( int i = 1 ; i <= numSegmentos; i++  ) {
        
            anguloRad = angStep * i;
            
            dx = Math.sin( anguloRad ) * radioGiro;
            dz = -Math.cos( anguloRad ) * radioGiro; 
            
            x = xo + dx;
            z = zo + dz;            
                 
            giro.rotY( -anguloRad );       
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( x , 0 , z ) );                                                
            siguiente.mul( giro );
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );            
            
        }
           
                 
        
        return g;
        
    }
    
    public static Node getCurvaDerecha( ) {
                
        return getCurvaDerecha( Math.PI / 2.0 );
        
    }
    
    public static Node getCurvaIzquierda( ) {
                
        return getCurvaIzquierda( Math.PI / 2.0 );
        
    }

    public static Node getCurvaIzquierda( double pAngulo ) {
            
        Group g = new Group();
        
        actual = new Transform3D();
            
        Transform3D giro = new Transform3D();    
        
        double temp = pAngulo / ( Math.PI / 2 );
        temp = temp * 15.0;
        int numSegmentos = (int)temp; 
        
        double maxAngle = pAngulo;
        double angStep = maxAngle / numSegmentos;
        double anguloRad = angStep;        
        
        double x = 0.0;
        double z = 0.0;
        
        double dx = 0.0;
        double dz = 0.0;
        
        double radioGiro = 10.0;
        
        double xo = 0.0;        
        double zo = -radioGiro;
        
            
        for( int i = 1 ; i <= numSegmentos; i++ ) {
        
            anguloRad = angStep * i;
            
            dx = Math.sin( anguloRad ) * radioGiro;
            dz = Math.cos( anguloRad ) * radioGiro; 
            
            x = xo + dx;
            z = zo + dz;            
                 
            giro.rotY( anguloRad );       
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( x , 0 , z ) );                                                
            siguiente.mul( giro );
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );            
            
        }
           
                 
        
        return g;

    }
    
    public static Node getCurvaDerecha( double pAngulo , int pAltura ) {
        
        Group g = new Group();
        
        actual = new Transform3D();
            
        Transform3D giro = new Transform3D();    
        
        double temp = pAngulo / ( Math.PI / 2 );
        temp = temp * 15.0;
        int numSegmentos = (int)temp; 
        
        double pasoAltura = (double)pAltura / numSegmentos;
        
        double maxAngle = pAngulo;
        double angStep = maxAngle / numSegmentos;
        double anguloRad = angStep;        
        
        double x = 0.0;
        double z = 0.0;
        
        double dx = 0.0;
        double dz = 0.0;
        
        double radioGiro = 10.0;
        
        double xo = 0.0;        
        double zo = radioGiro;
        
        int i = 1;
        
        while( anguloRad <= maxAngle ) {
        
            dx = Math.sin( anguloRad ) * radioGiro;
            dz = -Math.cos( anguloRad ) * radioGiro; 
            
            x = xo + dx;
            z = zo + dz;            
                 
            giro.rotY( -anguloRad );       
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( x , pasoAltura * i  , z ) );                                                
            siguiente.mul( giro );
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );
            
            anguloRad += angStep;    
            
            i++;
        }

        return g;
        
    }
    
    public static Node getCurvaIzquierda( double pAngulo , int pAltura ) {

        Group g = new Group();
            
        actual = new Transform3D();
        
        Transform3D giro = new Transform3D();    
        
        double temp = pAngulo / ( Math.PI / 2 );
        temp = temp * 15.0;
        int numSegmentos = (int)temp; 
        
        double pasoAltura = (double)pAltura / numSegmentos;
        
        double maxAngle = pAngulo;
        double angStep = maxAngle / numSegmentos;
        double anguloRad = angStep;        
        
        double x = 0.0;
        double z = 0.0;
        
        double dx = 0.0;
        double dz = 0.0;
        
        double radioGiro = 10.0;
        
        double xo = 0.0;        
        double zo = -radioGiro;
        
        int i = 1;
        
        while( anguloRad <= maxAngle ) {
        
            dx = Math.sin( anguloRad ) * radioGiro;
            dz = Math.cos( anguloRad ) * radioGiro; 
            
            x = xo + dx;
            z = zo + dz;            
                 
            giro.rotY( anguloRad );       
            
            siguiente = new Transform3D();
            siguiente.setTranslation( new Vector3d( x , i * pasoAltura , z ) );                                                
            siguiente.mul( giro );
            
            g.addChild( getSegmento() );
            
            actual.set( siguiente );
            
            anguloRad += angStep;    
            i++;
        }
        
        return g;
        
    }
      
    public static Node getPistaA() {
        
        TransformGroup mEscena = new TransformGroup();
        Transform3D escala = new Transform3D();
        escala.setScale( 1.0 );
        mEscena.setTransform( escala );
        mEscena.setCollidable(true);
        
        mEscena.addChild( Tools.trasladar( -40 , 0 , 0 , MapBuilder.getSegmentoRecto( 50 ) ) );
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , MapBuilder.getCurvaDerecha() ) ) ;
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( -Math.PI / 2.0f , MapBuilder.getSegmentoRecto( 10 ) ) ) );
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( -Math.PI / 2.0f , MapBuilder.getCurvaDerecha() ) ) ) ;
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( - Math.PI ,  MapBuilder.getSegmentoRecto( 50 ) ) ) );
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( Math.PI , MapBuilder.getCurvaDerecha() ) ) ) ;
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( - 3.0f * Math.PI / 2.0f , MapBuilder.getSegmentoRecto( 10 ) ) ) );
        updateLast();
        mEscena.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( - 3.0f * Math.PI / 2.0f , MapBuilder.getCurvaDerecha() ) ) ) ;
        
        return mEscena;
        
    }    
    
    public static Node getPistaB() {
        
        Group g = new Group();
        
        g.addChild( getSegmentoRecto( 15 ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , getCurvaDerecha( Math.PI / 6  )  ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( - Math.PI / 6 ,  getPendiente( -4 , 40 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( - Math.PI / 6 , getCurvaIzquierda( Math.PI / 6  ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , getSegmentoRecto( 30 ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , getCaracol( 1 ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , getCurvaIzquierda( Math.PI / 2 , 1) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( Math.PI / 2 , getSegmentoRecto( 30 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY(  Math.PI / 2 , getCaracol( -2 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY(  Math.PI / 2 , getCurvaIzquierda() ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( Math.PI , getSegmentoRecto( 20 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( Math.PI , getCurvaDerecha( Math.PI / 4 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( 3 * Math.PI / 4  , getPendiente( 6 , 60 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( 3 * Math.PI / 4 , getCurvaIzquierda( 3 * Math.PI / 4 , 2 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( 3 * Math.PI / 2 , getSegmentoRecto( 20 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( 3 * Math.PI / 2 , getCurvaDerecha( Math.PI ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( Math.PI / 2 , getCurvaIzquierda( Math.PI ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( -Math.PI / 2 , getSegmentoRecto( 35.6 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , Tools.rotarY( -Math.PI / 2 , getCurvaIzquierda( Math.PI / 2 ) ) ) );
        updateLast();
        g.addChild( Tools.trasladar( mLastX , mLastY , mLastZ , getSegmentoRecto(28.01) ) );
        updateLast();
        return g;
        
    }
    
    private static Shape3D getPlanoConTextura( Point3d pA , Point3d pB , Point3d pC, Point3d pD , String pTextura ) {
     
        QuadArray data = new QuadArray( 4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2 | QuadArray.NORMALS );   
        
        Point3d[] coordenadas = { pA, pB, pC, pD };
        
        TexCoord2f[] coordenadasT = { tA , tB , tC,  tD };
        
        data.setCoordinates(0 , coordenadas);
        data.setTextureCoordinates( 0 , 0 , coordenadasT );
        
        GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        
        info.reset(data);
        
        NormalGenerator ng = new NormalGenerator();

        ng.generateNormals(info);
                
        Appearance app = Tools.cargarTextura( pTextura );
        
        app.setMaterial( null );
        
        Shape3D forma = new Shape3D( info.getGeometryArray() , app );
        
        forma.setPickable( false );
        forma.setCollidable( false );
                        
        return forma;
        
    }
    
    public static Node getSkyBox() {        
        
        double lado = 300;
        
        Group g = new Group();
        
        Point3d pA = new Point3d( lado , -lado , -lado );
        Point3d pB = new Point3d( lado , -lado , lado );
        Point3d pC = new Point3d( lado , lado, lado );
        Point3d pD = new Point3d( lado , lado, -lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\frontF.jpg") );
        
        pA = new Point3d( lado , -lado , lado );
        pB = new Point3d( -lado , -lado , lado );
        pC = new Point3d( -lado , lado, lado );
        pD = new Point3d( lado , lado, lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\rightF.jpg") );
        
        pA = new Point3d( -lado , -lado , lado );
        pB = new Point3d( -lado , -lado , -lado );
        pC = new Point3d( -lado , lado, -lado );
        pD = new Point3d( -lado , lado, lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\backF.jpg") );
        
        pA = new Point3d( -lado , -lado , -lado );
        pB = new Point3d( lado , -lado , -lado );
        pC = new Point3d( lado , lado, -lado );
        pD = new Point3d( -lado , lado, -lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\leftF.jpg") );
        
        pA = new Point3d( lado , lado , -lado );
        pB = new Point3d( lado , lado , lado );
        pC = new Point3d( -lado , lado, lado );
        pD = new Point3d( -lado , lado, -lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\topA.jpg") );
        
        pA = new Point3d( -lado , -lado , lado );
        pB = new Point3d( lado , -lado , lado );
        pC = new Point3d( lado , -lado, -lado );
        pD = new Point3d( -lado , -lado, -lado );
        
        g.addChild( getPlanoConTextura( pA, pB, pC, pD , "c:\\3d\\sky\\bottomA.jpg") );
        
        return Tools.trasladar( 0 , 0 , 0 , g);        
        
    }
    
    public static Node getGroundPlane() {
        
        double lado = 300;        
        
        Point3d pA = new Point3d( -lado , 0 , lado );
        Point3d pB = new Point3d( lado , 0 , lado );
        Point3d pC = new Point3d( lado , 0, -lado );
        Point3d pD = new Point3d( -lado , 0, -lado );
        
        TexCoord2f tAl = new TexCoord2f( 0 , 0 );
        TexCoord2f tBl = new TexCoord2f( 100 , 0 );
        TexCoord2f tCl = new TexCoord2f( 100 , 100 );
        TexCoord2f tDl = new TexCoord2f( 0 , 100 );
        
        QuadArray data = new QuadArray( 4, QuadArray.COORDINATES | QuadArray.TEXTURE_COORDINATE_2 | QuadArray.NORMALS );   
        
        Point3d[] coordenadas = { pA, pB, pC, pD };
        
        TexCoord2f[] coordenadasT = { tAl , tBl , tCl,  tDl };
        
        data.setCoordinates(0 , coordenadas);
        data.setTextureCoordinates( 0 , 0 , coordenadasT );
        
        GeometryInfo info = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
        
        info.reset(data);
        
        NormalGenerator ng = new NormalGenerator();

        ng.generateNormals(info);
                
        Appearance app = Tools.cargarTextura( "c:\\3d\\ground5.png" );

        app.setMaterial( null );
            
        Shape3D forma = new Shape3D( info.getGeometryArray() , app );
                
        forma.setPickable( false );
        forma.setCollidable( false );
                        
        return forma;        
        
    }
}
