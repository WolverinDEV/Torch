package net.minecraft.server;

import com.google.common.base.Optional;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.craftbukkit.event.CraftEventFactory;
import org.bukkit.event.entity.ExplosionPrimeEvent;
// CraftBukkit end

public class EntityEnderCrystal extends Entity {

    private static final DataWatcherObject<Optional<BlockPosition>> b = DataWatcher.a(EntityEnderCrystal.class, DataWatcherRegistry.k);
    private static final DataWatcherObject<Boolean> c = DataWatcher.a(EntityEnderCrystal.class, DataWatcherRegistry.h);
    public int a;

    public EntityEnderCrystal(World world) {
        super(world);
        this.i = true;
        this.setSize(2.0F, 2.0F);
        this.a = this.random.nextInt(100000);
    }

    public EntityEnderCrystal(World world, double d0, double d1, double d2) {
        this(world);
        this.setPosition(d0, d1, d2);
    }

    @Override
    protected boolean playStepSound() {
        return false;
    }

    @Override
    protected void i() {
        this.getDataWatcher().register(EntityEnderCrystal.b, Optional.absent());
        this.getDataWatcher().register(EntityEnderCrystal.c, Boolean.valueOf(true));
    }

    @Override
    public void A_() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        ++this.a;
        BlockPosition blockposition = new BlockPosition(this);

        if (this.world.worldProvider instanceof WorldProviderTheEnd && this.world.getType(blockposition).getBlock() != Blocks.FIRE) {
            // CraftBukkit start
            if (!CraftEventFactory.callBlockIgniteEvent(this.world, blockposition.getX(), blockposition.getY(), blockposition.getZ(), this).isCancelled()) {
                this.world.setTypeUpdate(blockposition, Blocks.FIRE.getBlockData());
            }
            // CraftBukkit end
        }

    }

    @Override
    protected void b(NBTTagCompound nbttagcompound) {
        if (this.getBeamTarget() != null) {
            nbttagcompound.set("BeamTarget", GameProfileSerializer.a(this.getBeamTarget()));
        }

        nbttagcompound.setBoolean("ShowBottom", this.isShowingBottom());
    }

    @Override
    protected void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("BeamTarget", 10)) {
            this.setBeamTarget(GameProfileSerializer.c(nbttagcompound.getCompound("BeamTarget")));
        }

        if (nbttagcompound.hasKeyOfType("ShowBottom", 1)) {
            this.setShowingBottom(nbttagcompound.getBoolean("ShowBottom"));
        }

    }

    @Override
    public boolean isInteractable() {
        return true;
    }

    @Override
    public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if (damagesource.getEntity() instanceof EntityEnderDragon) {
            return false;
        } else {
            if (!this.dead) {
                // CraftBukkit start - All non-living entities need this
                if (CraftEventFactory.handleNonLivingEntityDamageEvent(this, damagesource, f)) {
                    return false;
                }
                // CraftBukkit end
                this.die();
                // CraftBukkit start
                ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 6.0F, true);
                this.world.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    this.dead = false;
                    return false;
                }
                this.world.explode(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire());
                // CraftBukkit end
                this.a(damagesource);
            }

            return true;
        }
    }

    @Override
    public void Q() {
        this.a(DamageSource.GENERIC);
        super.Q();
    }

    private void a(DamageSource damagesource) {
        if (this.world.worldProvider instanceof WorldProviderTheEnd) {
            WorldProviderTheEnd worldprovidertheend = (WorldProviderTheEnd) this.world.worldProvider;
            EnderDragonBattle enderdragonbattle = worldprovidertheend.t();

            if (enderdragonbattle != null) {
                enderdragonbattle.a(this, damagesource);
            }
        }

    }

    public void setBeamTarget(@Nullable BlockPosition blockposition) {
        this.getDataWatcher().set(EntityEnderCrystal.b, Optional.fromNullable(blockposition));
    }

    @Nullable
    public BlockPosition getBeamTarget() {
        return (BlockPosition) ((Optional) this.getDataWatcher().get(EntityEnderCrystal.b)).orNull();
    }

    public void setShowingBottom(boolean flag) {
        this.getDataWatcher().set(EntityEnderCrystal.c, Boolean.valueOf(flag));
    }

    public boolean isShowingBottom() {
        return this.getDataWatcher().get(EntityEnderCrystal.c).booleanValue();
    }
}
