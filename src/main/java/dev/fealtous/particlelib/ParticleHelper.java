package dev.fealtous.particlelib;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import static dev.fealtous.particlelib.Transformations.rotate;
import static dev.fealtous.particlelib.Transformations.translate;

/**
 * Utility class for spawning particles
 */
public class ParticleHelper {
    public static void send(ServerLevel level, ParticleOptions particle, Vec3 start, Vec3 end, int intensity) {
        level.sendParticles(particle, start.x, start.y, start.z, intensity, end.x, end.y, end.z, 0);
    }

    public static void send(ServerLevel level, ParticleOptions particle, Vec3 start, Vec3 end, int intensity, double speed) {
        level.sendParticles(particle, start.x, start.y, start.z, intensity, end.x, end.y, end.z, speed);
    }

    /**
     * Draws a particle at a location.
     * @param particle the particle
     * @param loc the location
     */
    public static void point(ServerLevel level, ParticleOptions particle, Vec3 loc) {
        level.sendParticles(particle, loc.x, loc.y, loc.z, 1, 0, 0, 0, 0);
    }

    /**
     * Draws a line of particles with interpolated points depending on intensity.
     * @param particle particle
     * @param a start point
     * @param b end point
     * @param intensity level of detail
     */
    public static void lineAtoB(ServerLevel level, ParticleOptions particle, Vec3 a, Vec3 b, int intensity) {
        final double lerp = 1d / intensity;
        for (int i = 1; i <= intensity; i++) {
            Vec3 res = a.lerp(b, lerp * i);
            point(level, particle, res);
        }
    }

    /**
     * Just draw some points
     * @param particleOptions particle
     * @param points the points
     */
    public static void points(ServerLevel level, ParticleOptions particleOptions, Vec3[] points) {
        for (Vec3 point : points) {
            point(level, particleOptions, point);
        }
    }

    /**
     * Draws a prism with two faces having verts vertices and a length of its radius.
     * Only draws the vertices
     * @param particle particle
     * @param origin prism's center
     * @param radius radius of polygon and
     * @param verts number of vertices for the shape
     * @param rotation rotation
     */
    public static void regularPolygonPrism(ServerLevel level, ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation) {
        var top = makeRegularPolyVerts(radius, verts);
        translate(top, new Vec3(0,radius, 0));
        var bottom = makeRegularPolyVerts(radius,verts);
        translate(bottom, new Vec3(0, -radius, 0));
        rotate(top, rotation.x, rotation.y);
        rotate(bottom, rotation.x, rotation.y);
        translate(top, origin);
        translate(bottom, origin);
        for (Vec3 vec3 : top) {
            point(level, particle, vec3);
        }
        for (Vec3 vec3 : bottom) {
            point(level, particle, vec3);
        }
    }

    public static void regularPolygonLinePrism(ServerLevel level, ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation, int intensity) {
        var top = makeRegularPolyVerts(radius, verts);
        translate(top, new Vec3(0,radius, 0));
        var bottom = makeRegularPolyVerts(radius,verts);
        translate(bottom, new Vec3(0, -radius, 0));
        rotate(top, rotation.x, rotation.y);
        rotate(bottom, rotation.x, rotation.y);
        translate(top, origin);
        translate(bottom, origin);
        drawLinePolygon(level, particle, top, intensity);
        drawLinePolygon(level, particle, bottom, intensity);
        for (int i = 0; i < top.length; i++) {
            lineAtoB(level, particle, top[i], bottom[i], intensity);
        }
    }

    /**
     * Generates a vertex array for some regular polygon.
     * @param radius radius of polygon
     * @param verts number of vertices
     * @return Array of verts in clockwise order
     */
    public static Vec3[] makeRegularPolyVerts(float radius, int verts) {
        Vec3 point = new Vec3(radius, 0, 0);
        Vec3[] vertArr = new Vec3[verts];
        float radsPerRot = (float) Math.toRadians(360f/verts);
        for (int i = 0; i < vertArr.length; i++) {
            vertArr[i] = point.yRot(radsPerRot * i);
        }
        return vertArr;
    }

    /**
     * Draws only the vertices of a regular polygon.
     * @param particle particle
     * @param origin center position
     * @param radius radius of points
     * @param verts number of vertices
     */
    public static void regularPolygon(ServerLevel level, ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation) {
        var arr = makeRegularPolyVerts(radius, verts);
        rotate(arr, rotation.x, rotation.y);
        translate(arr, origin);
        for (Vec3 point : arr) {
            point(level, particle, point);
        }
    }

    /**
     * Draws lines in the shape of a regular polygon.
     * @param particle particle
     * @param origin center position
     * @param radius radius of points
     * @param verts number of vertices
     * @param intensity number particles to spawn
     */
    public static void regularLinePolygon(ServerLevel level, ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation, int intensity) {
        Vec3[] vertArr = makeRegularPolyVerts(radius, verts);
        rotate(vertArr, rotation.x, rotation.y);
        translate(vertArr, origin);
        drawLinePolygon(level, particle, vertArr, intensity);
    }

    /**
     * Helper method for drawing faces.
     */
    public static void drawLinePolygon(ServerLevel level, ParticleOptions particle, Vec3[] points, int intensity) {
        for (int i = 0; i < points.length; i++) {
            lineAtoB(level, particle, points[i], points[(i+1)%points.length], intensity);
        }
    }
}
