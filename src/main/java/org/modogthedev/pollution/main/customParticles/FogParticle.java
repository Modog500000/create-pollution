package org.modogthedev.pollution.main.customParticles;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.modogthedev.pollution.util.IParticleRenderType;

@OnlyIn(Dist.CLIENT)
public class FogParticle extends TextureSheetParticle {
    protected FogParticle(ClientLevel level, double xCoord, double yCoord, double zCoord,
                               SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);

        this.friction = 1;
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.quadSize = 5;
        this.lifetime = 100;
        this.age = 10;
        this.setSpriteFromAge(spriteSet);

        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
    }
    @Override
    public void tick() {
        super.tick();
        fadeOut();
    }
    private void fadeOut() {
            this.alpha = (-(1 / (float) lifetime) * age + 1.1F);
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(@NotNull SimpleParticleType particleType, @NotNull ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            FogParticle fogParticle = new FogParticle(level, x, y, z, this.sprites, dx, dy, dz);
            fogParticle.pickSprite(sprites);
            fogParticle.quadSize = (float) (Math.random()*4)+3;
            return fogParticle;
        }
    }
}
