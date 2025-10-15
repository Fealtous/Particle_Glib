package dev.fealtous.particlelib;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import static dev.fealtous.particlelib.Transformations.*;

public abstract class AbstractParticleEmitter {
    protected ServerLevel level;
    protected boolean active;
    protected boolean needsRemoval = false;

    public AbstractParticleEmitter(ServerLevel level) {
        active = true;
        this.level = level;
    }

    /**
     * Spawns particles according to the emitter's definition.
     */
    protected abstract void emit();

    /**
     * Handle movement and any other features that may not belong in {@link AbstractParticleEmitter#emit}
     */
    public abstract void tick();

    /**
     * The definition of "active" is up to the implementation.
     * It may be that an emitter only creates particles when active but still moves around.
     * It may also be that an emitter doesn't move when active but still create particles
     * @return whether this emitter is active
     */
    public final boolean isActive() {
        return active;
    }

    public final void deactivate() {
        active = false;
    }

    public final void activate() {
        active = true;
    }



    /**
     * Ideally an emitter should know when it needs to be removed.
     * This method is provided in case more direct management is needed.
     */
    public final void flagToRemove() {
        needsRemoval = true;
    }

    public final boolean isNeedsRemoval() {
        return needsRemoval;
    }

    public final void send(ParticleOptions particle, Vec3 start, Vec3 end, int intensity) {
        this.level.sendParticles(particle, start.x, start.y, start.z, intensity, end.x, end.y, end.z, 0);
    }

    public final void send(ParticleOptions particle, Vec3 start, Vec3 end, int intensity, double speed) {
        this.level.sendParticles(particle, start.x, start.y, start.z, intensity, end.x, end.y, end.z, speed);
    }

    /**
     * Draws a particle at a location.
     * @param particle the particle
     * @param loc the location
     */
    public final void point(ParticleOptions particle, Vec3 loc) {
        this.level.sendParticles(particle, loc.x, loc.y, loc.z, 1, 0, 0, 0, 0);
    }

    /**
     * Draws a line of particles with interpolated points depending on intensity.
     * @param particle particle
     * @param a start point
     * @param b end point
     * @param intensity level of detail
     */
    public final void lineAtoB(ParticleOptions particle, Vec3 a, Vec3 b, int intensity) {
        final double lerp = 1d / intensity;
        for (int i = 1; i <= intensity; i++) {
            Vec3 res = a.lerp(b, lerp * i);
            point(particle, res);
        }
    }

    /**
     * Just draw some points
     * @param particleOptions particle
     * @param points the points
     */
    public final void points(ParticleOptions particleOptions, Vec3[] points) {
        for (Vec3 point : points) {
            point(particleOptions, point);
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
    public final void regularPolygonPrism(ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation) {
        var top = makeRegularPolyVerts(radius, verts);
        translate(top, new Vec3(0,radius, 0));
        var bottom = makeRegularPolyVerts(radius,verts);
        translate(bottom, new Vec3(0, -radius, 0));
        rotate(top, rotation.x, rotation.y);
        rotate(bottom, rotation.x, rotation.y);
        translate(top, origin);
        translate(bottom, origin);
        for (Vec3 vec3 : top) {
            point(particle, vec3);
        }
        for (Vec3 vec3 : bottom) {
            point(particle, vec3);
        }
    }

    public final void regularPolygonLinePrism(ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation, int intensity) {
        var top = makeRegularPolyVerts(radius, verts);
        translate(top, new Vec3(0,radius, 0));
        var bottom = makeRegularPolyVerts(radius,verts);
        translate(bottom, new Vec3(0, -radius, 0));
        rotate(top, rotation.x, rotation.y);
        rotate(bottom, rotation.x, rotation.y);
        translate(top, origin);
        translate(bottom, origin);
        drawLinePolygon(particle, top, intensity);
        drawLinePolygon(particle, bottom, intensity);
        for (int i = 0; i < top.length; i++) {
            lineAtoB(particle, top[i], bottom[i], intensity);
        }
    }

    /**
     * Generates a vertex array for some regular polygon.
     * @param radius radius of polygon
     * @param verts number of vertices
     * @return Array of verts in clockwise order
     */
    protected static Vec3[] makeRegularPolyVerts(float radius, int verts) {
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
    public final void regularPolygon(ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation) {
        var arr = makeRegularPolyVerts(radius, verts);
        rotate(arr, rotation.x, rotation.y);
        translate(arr, origin);
        for (Vec3 point : arr) {
            point(particle, point);
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
    public final void regularLinePolygon(ParticleOptions particle, Vec3 origin, float radius, int verts, Vec2 rotation, int intensity) {
        Vec3[] vertArr = makeRegularPolyVerts(radius, verts);
        rotate(vertArr, rotation.x, rotation.y);
        translate(vertArr, origin);
        drawLinePolygon(particle, vertArr, intensity);
    }

    /**
     * Helper method for drawing faces.
     */
    public final void drawLinePolygon(ParticleOptions particle, Vec3[] points, int intensity) {
        for (int i = 0; i < points.length; i++) {
            lineAtoB(particle, points[i], points[(i+1)%points.length], intensity);
        }
    }
}
