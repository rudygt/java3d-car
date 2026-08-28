package test;

import java.util.Iterator;

import org.jogamp.java3d.Behavior;
import org.jogamp.java3d.Node;
import org.jogamp.java3d.WakeupCriterion;
import org.jogamp.java3d.WakeupOnCollisionEntry;
import org.jogamp.java3d.WakeupOnCollisionExit;


class CollisionDetector extends Behavior {

  Node mNodo = null;
  
  boolean inCollision = false;
  
  private WakeupOnCollisionEntry wEnter;

  private WakeupOnCollisionExit wExit;
  
  CarBehavior mCarB = null;
  
  int numVueltas = 0;

  public CollisionDetector(Node pNode ) {
    
    mNodo = pNode;
    inCollision = false;
    
  }

  public void initialize() {
    wEnter = new WakeupOnCollisionEntry(mNodo , WakeupOnCollisionEntry.USE_GEOMETRY );
    wExit = new WakeupOnCollisionExit(mNodo);
    wakeupOn(wEnter);
  }

  public void processStimulus(Iterator<WakeupCriterion> criteria) {
      
    inCollision = !inCollision;

    if (inCollision) {      
        if( mCarB != null ) {
            mCarB.incrementarNumeroDeVuelta();
        }        
        wakeupOn(wExit);
    } else {              
        wakeupOn(wEnter);
    }
  }

    public void setCarB(CarBehavior mCarB) {
        this.mCarB = mCarB;
    }

    public CarBehavior getCarB() {
        return mCarB;
    }
}
