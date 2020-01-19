package test;

import org.scijava.java3d.utils.universe.SimpleUniverse;

import org.scijava.java3d.Transform3D;
import org.scijava.java3d.TransformGroup;

import org.scijava.vecmath.Vector3d;
 
/**
 *
 * @author Joaquin
 */
public class CamaraM {
    
    private final Vector3d POS_RELATIVA = new Vector3d(0,6,10);
    
    Transform3D vistaSuperior = new Transform3D();
    Transform3D rotPacman = new Transform3D();
    Transform3D posPacman = new Transform3D();
    TransformGroup camara = null;
    /**
     *	Crea una nueva instancia de Camara
     */
    public CamaraM( SimpleUniverse su ) {
	/**
	 *  Tomamos la c?mara del universo y le vamos a aplicar una transformaci?n
	 *  para dejarla mirando desde atr?s y ligeramente m?s alto hacia el objeto.
	 *  Para ello usamos la funci?n lookAt de la transformaci?n, que recibe
	 *  como par?metro 3 cosas: un punto desde el que se va a posicionar la
	 *  c?mara, un punto que va a ser al que va a apuntar la c?mara, y un
	 *  vector perpendicular a la superficie.
	 *  Luego, tenemos que invertir la transformaci?n (??).
	 */
	camara = su.getViewingPlatform().getViewPlatformTransform();
	
	Transform3D tr = new Transform3D();
	tr.rotX(- Math.PI / 12.0 );
	vistaSuperior.setTranslation(POS_RELATIVA);
	vistaSuperior.mul(tr);
    }
    
    /**
     *	Este m?todo recibe como par?metro un vector que indica la posici?n
     *	del objeto que se quiere enfocar con la c?mara
     *	@param mover vector con la posici?n del objeto.
     */
    public void mover( Vector3d mover ){
	posPacman.setTranslation(mover);
    }
    
    /**
     *	Este m?todo se encargar de aplicar una rotaci?n a la c?mara.
     *	Esto lo hace calculando un desplazamiento en base a la posici?n relativa
     *	sobre el objeto que se est? enfocando con la c?mara.
     *	@param ang angulo de rotaci?n de la c?mara.
     */
    public void rotar( double ang ){
	rotPacman.rotY(ang - Math.PI / 2.0 );
    }
    
    public void actualizar(){
	/* Le aplicamos a la camara la posici?n inicial */
	Transform3D tr3 = new Transform3D(rotPacman);
	Transform3D tr4 = new Transform3D(posPacman);
	
	tr3.mul(vistaSuperior);
	tr4.mul(tr3);
	
	this.camara.setTransform(tr4);
    }
}
