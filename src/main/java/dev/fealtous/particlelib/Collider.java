package dev.fealtous.particlelib;


public interface Collider {

    /**
     * Check whether a collision has occurred.
     * You may want to capture what it collided with here as well.
     * @return Whether a collision occurred
     */
    boolean checkCollision();

    /**
     * What will happen when there is a collision.
     * By default a collision will mark this emitter for removal.
     */
    default void onCollision() {
        if (this instanceof AbstractParticleEmitter self) self.flagToRemove();
    };
}
