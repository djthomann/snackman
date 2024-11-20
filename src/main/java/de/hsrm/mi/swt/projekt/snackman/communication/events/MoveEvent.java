package de.hsrm.mi.swt.projekt.snackman.communication.events;
import org.joml.Vector3f;

/**
 * Event that informs backend that a Player wants to move and in what direction
 */
public class MoveEvent extends Event {
    
    private Vector3f movementVector;

    public MoveEvent(Vector3f movementVector) {
        this.movementVector = movementVector;
    }

    public Vector3f getMovementVector() {
        return movementVector;
    }

    public void setMovementVector(int movementVector) {
        this.movementVector = movementVector;
    }

}
