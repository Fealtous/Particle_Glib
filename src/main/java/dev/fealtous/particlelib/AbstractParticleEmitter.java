package dev.fealtous.particlelib;

import net.minecraft.server.level.ServerLevel;

public abstract class AbstractParticleEmitter {
    protected ServerLevel level;
    protected boolean needsRemoval = false;

    public AbstractParticleEmitter(ServerLevel level) {
        this.level = level;
    }

    /**
     * Spawns particles according to its implementation.
     */
    protected abstract void emit();

    /**
     * Handle movement and any other features that may not belong in {@link AbstractParticleEmitter#emit}
     */
    public abstract void tick();

    /**
     * Ideally an emitter should know when it needs to be removed.
     * This method is provided in case more direct management is needed.
     */
    public final void remove() {
        needsRemoval = true;
    }

    public final boolean shouldRemove() {
        return needsRemoval;
    }
}
