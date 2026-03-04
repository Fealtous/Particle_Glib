package dev.fealtous.particlelib;

import net.minecraft.world.level.Level;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@SuppressWarnings("unused")
@NullMarked
public class ParticleManager {
    private final Map<Level, List<AbstractParticleEmitter>> emitters = new HashMap<>();
    private final Map<Level, Queue<AbstractParticleEmitter>> toAdd = new HashMap<>();
    private static final List<AbstractParticleEmitter> EMPTY = new ArrayList<>();


    public ParticleManager() {}

    /**
     * Queue an emitter to be ticked next level tick.
     * @param emitter to add
     */
    public void queueEmitter(AbstractParticleEmitter emitter) {
        if (!toAdd.containsKey(emitter.level)) {
            toAdd.put(emitter.level, new ArrayDeque<>());
        }
        this.toAdd.get(emitter.level).add(emitter);
    }


    /**
     * Assumes emitters don't need to be ticked in any particular order. Older emitters will be ticked last.
     * Emitters that need to be removed should mark themselves as such.
     */
    public void tick(Level level) {
        List<AbstractParticleEmitter> newList = new ArrayList<>();
        if (toAdd.get(level) != null) newList.addAll(toAdd.get(level));
        toAdd.remove(level);
        List<AbstractParticleEmitter> quant = emitters.getOrDefault(level, EMPTY);
        for (AbstractParticleEmitter emitter : quant) {
            if (!emitter.shouldRemove()) {
                newList.add(emitter);
                emitter.tick();
            }
        }
        emitters.put(level, newList);
    }
}
