package dev.fealtous.particlelib;

import net.minecraft.world.level.Level;

import java.util.*;

public class ParticleManager {
    private final Map<Level, List<AbstractParticleEmitter>> emitters = new HashMap<>();
    private final Map<Level, Queue<AbstractParticleEmitter>> toAdd = new HashMap<>();


    public ParticleManager() {}

    public void addEmitter(AbstractParticleEmitter emitter) {
        var lvl = emitter.level;
        if (!toAdd.containsKey(lvl)) {
            toAdd.put(lvl, new ArrayDeque<>());
        }
        this.toAdd.get(lvl).add(emitter);
    }


    /**
     * Assumes emitters don't need to be ticked in any particular order. Older emitters will be ticked last.
     * Emitters that need to be removed should mark themselves as such.
     */
    public void tick(Level level) {
        List<AbstractParticleEmitter> newList = new ArrayList<>();
        if (toAdd.get(level) != null) newList.addAll(toAdd.get(level));
        if (emitters.get(level) instanceof List<AbstractParticleEmitter> quant) {
            for (AbstractParticleEmitter emitter : quant) {
                if (!emitter.isNeedsRemoval()) {
                    newList.add(emitter);
                    emitter.tick();
                }
            }
        }
        toAdd.remove(level);
        emitters.put(level, newList);
    }
}
