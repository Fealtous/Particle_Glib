package dev.fealtous.particlelib;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jspecify.annotations.NullMarked;

@SuppressWarnings("unused")
@NullMarked
public abstract class PersistentEntityBoundEmitter extends AbstractParticleEmitter {
    protected final Entity target;

    public PersistentEntityBoundEmitter(ServerLevel level, Entity target) {
        super(level);
        this.target = target;
    }

    @Override
    protected abstract void emit();

    @Override
    public void tick() {
        if (target.isAlive()) {
            emit();
        }
        else this.remove();
    }
}
