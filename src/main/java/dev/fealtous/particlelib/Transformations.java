package dev.fealtous.particlelib;

import net.minecraft.world.phys.Vec3;

/**
 * A utility class for transforming particles or sets of particles.
 * Each particle's location IS a vector and must be treated as such.
 * Array based methods will overwrite the original values.
 */
public class Transformations {

    public static void rotate(Vec3[] points, float xRot, float yRot) {
        for (int i = 0; i < points.length; i++) {
            points[i] = rotate(points[i], xRot, yRot);
        }
    }

    public static Vec3 rotate(Vec3 point, float xRot, float yRot) {
        return point.xRot(xRot).yRot(yRot);
    }

    public static void translate(Vec3[] points, Vec3 movement) {
        for (int i = 0; i < points.length; i++) {
            points[i] = translate(points[i], movement);
        }
    }

    public static Vec3 translate(Vec3 point, Vec3 movement) {
        return point.add(movement);
    }

    public static void scale(Vec3[] points, float scale) {
        for (int i = 0; i < points.length; i++) {
            points[i] = scale(points[i], scale);
        }
    }

    public static Vec3 scale(Vec3 point, float scale) {
        return point.scale(scale);
    }
}
