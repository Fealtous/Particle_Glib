package dev.fealtous.particlelib;

import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public interface ParticleShape {
    /**
     * Creates a list of positions particles will spawn at
     * @param rootPosition ""center"" of this shape, really this is just where offsets should be relative to
     * @param iterations Number of times this emitter has been used.
     * @param intensity Informs how many particles should spawn
     * @param last Last set of particles spawned. Consider using matrices to rebuild a particle list from a prior created one.
     */
    List<Vec3> emit(Vec3 rootPosition, int iterations, int intensity, @Nullable List<Vec3> last);
}
