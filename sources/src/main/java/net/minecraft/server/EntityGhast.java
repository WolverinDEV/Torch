package net.minecraft.server;

import java.util.Random;
import javax.annotation.Nullable;

public class EntityGhast extends EntityFlying implements IMonster {

    private static final DataWatcherObject<Boolean> a = DataWatcher.a(EntityGhast.class, DataWatcherRegistry.h);
    private int b = 1;

    public EntityGhast(World world) {
        super(world);
        this.setSize(4.0F, 4.0F);
        this.fireProof = true;
        this.b_ = 5;
        this.moveController = new EntityGhast.ControllerGhast(this);
    }

    @Override
	protected void r() {
        this.goalSelector.a(5, new EntityGhast.PathfinderGoalGhastIdleMove(this));
        this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastMoveTowardsTarget(this));
        this.goalSelector.a(7, new EntityGhast.PathfinderGoalGhastAttackTarget(this));
        this.targetSelector.a(1, new PathfinderGoalTargetNearestPlayer(this));
    }

    public void a(boolean flag) {
        this.datawatcher.set(EntityGhast.a, Boolean.valueOf(flag));
    }

    public int getPower() {
        return this.b;
    }

    @Override
	public void A_() {
        super.A_();
        if (this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.die();
        }

    }

    @Override
	public boolean damageEntity(DamageSource damagesource, float f) {
        if (this.isInvulnerable(damagesource)) {
            return false;
        } else if ("fireball".equals(damagesource.p()) && damagesource.getEntity() instanceof EntityHuman) {
            super.damageEntity(damagesource, 1000.0F);
            ((EntityHuman) damagesource.getEntity()).b(AchievementList.z);
            return true;
        } else {
            return super.damageEntity(damagesource, f);
        }
    }

    @Override
	protected void i() {
        super.i();
        this.datawatcher.register(EntityGhast.a, Boolean.valueOf(false));
    }

    @Override
	protected void initAttributes() {
        super.initAttributes();
        this.getAttributeInstance(GenericAttributes.maxHealth).setValue(10.0D);
        this.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(100.0D);
    }

    @Override
	public SoundCategory bC() {
        return SoundCategory.HOSTILE;
    }

    @Override
	protected SoundEffect G() {
        return SoundEffects.bV;
    }

    @Override
	protected SoundEffect bW() {
        return SoundEffects.bX;
    }

    @Override
	protected SoundEffect bX() {
        return SoundEffects.bW;
    }

    @Override
	@Nullable
    protected MinecraftKey J() {
        return LootTables.aj;
    }

    @Override
	protected float ci() {
        return 10.0F;
    }

    @Override
	public boolean cM() {
        return this.random.nextInt(20) == 0 && super.cM() && this.world.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    @Override
	public int cQ() {
        return 1;
    }

    public static void a(DataConverterManager dataconvertermanager) {
        EntityInsentient.a(dataconvertermanager, EntityGhast.class);
    }

    @Override
	public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setInt("ExplosionPower", this.b);
    }

    @Override
	public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKeyOfType("ExplosionPower", 99)) {
            this.b = nbttagcompound.getInt("ExplosionPower");
        }

    }

    @Override
	public float getHeadHeight() {
        return 2.6F;
    }

    static class PathfinderGoalGhastAttackTarget extends PathfinderGoal {

        private final EntityGhast ghast;
        public int a;

        public PathfinderGoalGhastAttackTarget(EntityGhast entityghast) {
            this.ghast = entityghast;
        }

        @Override
		public boolean a() {
            return this.ghast.getGoalTarget() != null;
        }

        @Override
		public void c() {
            this.a = 0;
        }

        @Override
		public void d() {
            this.ghast.a(false);
        }

        @Override
		public void e() {
            EntityLiving entityliving = this.ghast.getGoalTarget();
            double d0 = 64.0D;

            if (entityliving.h(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(entityliving)) {
                World world = this.ghast.world;

                ++this.a;
                if (this.a == 10) {
                    world.a((EntityHuman) null, 1015, new BlockPosition(this.ghast), 0);
                }

                if (this.a == 20) {
                    double d1 = 4.0D;
                    Vec3D vec3d = this.ghast.f(1.0F);
                    double d2 = entityliving.locX - (this.ghast.locX + vec3d.x * 4.0D);
                    double d3 = entityliving.getBoundingBox().b + entityliving.length / 2.0F - (0.5D + this.ghast.locY + this.ghast.length / 2.0F);
                    double d4 = entityliving.locZ - (this.ghast.locZ + vec3d.z * 4.0D);

                    world.a((EntityHuman) null, 1016, new BlockPosition(this.ghast), 0);
                    EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.ghast, d2, d3, d4);

                    // CraftBukkit - set bukkitYield when setting explosionpower
                    entitylargefireball.bukkitYield = entitylargefireball.yield = this.ghast.getPower();
                    entitylargefireball.locX = this.ghast.locX + vec3d.x * 4.0D;
                    entitylargefireball.locY = this.ghast.locY + this.ghast.length / 2.0F + 0.5D;
                    entitylargefireball.locZ = this.ghast.locZ + vec3d.z * 4.0D;
                    world.addEntity(entitylargefireball);
                    this.a = -40;
                }
            } else if (this.a > 0) {
                --this.a;
            }

            this.ghast.a(this.a > 10);
        }
    }

    static class PathfinderGoalGhastMoveTowardsTarget extends PathfinderGoal {

        private final EntityGhast a;

        public PathfinderGoalGhastMoveTowardsTarget(EntityGhast entityghast) {
            this.a = entityghast;
            this.a(2);
        }

        @Override
		public boolean a() {
            return true;
        }

        @Override
		public void e() {
            if (this.a.getGoalTarget() == null) {
                this.a.yaw = -((float) MathHelper.c(this.a.motX, this.a.motZ)) * 57.295776F;
                this.a.aN = this.a.yaw;
            } else {
                EntityLiving entityliving = this.a.getGoalTarget();
                double d0 = 64.0D;

                if (entityliving.h(this.a) < 4096.0D) {
                    double d1 = entityliving.locX - this.a.locX;
                    double d2 = entityliving.locZ - this.a.locZ;

                    this.a.yaw = -((float) MathHelper.c(d1, d2)) * 57.295776F;
                    this.a.aN = this.a.yaw;
                }
            }

        }
    }

    static class PathfinderGoalGhastIdleMove extends PathfinderGoal {

        private final EntityGhast a;

        public PathfinderGoalGhastIdleMove(EntityGhast entityghast) {
            this.a = entityghast;
            this.a(1);
        }

        @Override
		public boolean a() {
            ControllerMove controllermove = this.a.getControllerMove();

            if (!controllermove.a()) {
                return true;
            } else {
                double d0 = controllermove.d() - this.a.locX;
                double d1 = controllermove.e() - this.a.locY;
                double d2 = controllermove.f() - this.a.locZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        @Override
		public boolean b() {
            return false;
        }

        @Override
		public void c() {
            Random random = this.a.getRandom();
            double d0 = this.a.locX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double d1 = this.a.locY + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double d2 = this.a.locZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;

            this.a.getControllerMove().a(d0, d1, d2, 1.0D);
        }
    }

    static class ControllerGhast extends ControllerMove {

        private final EntityGhast i;
        private int j;

        public ControllerGhast(EntityGhast entityghast) {
            super(entityghast);
            this.i = entityghast;
        }

        @Override
		public void c() {
            if (this.h == ControllerMove.Operation.MOVE_TO) {
                double d0 = this.b - this.i.locX;
                double d1 = this.c - this.i.locY;
                double d2 = this.d - this.i.locZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.j-- <= 0) {
                    this.j += this.i.getRandom().nextInt(5) + 2;
                    d3 = MathHelper.sqrt(d3);
                    if (this.b(this.b, this.c, this.d, d3)) {
                        this.i.motX += d0 / d3 * 0.1D;
                        this.i.motY += d1 / d3 * 0.1D;
                        this.i.motZ += d2 / d3 * 0.1D;
                    } else {
                        this.h = ControllerMove.Operation.WAIT;
                    }
                }

            }
        }

        private boolean b(double d0, double d1, double d2, double d3) {
            double d4 = (d0 - this.i.locX) / d3;
            double d5 = (d1 - this.i.locY) / d3;
            double d6 = (d2 - this.i.locZ) / d3;
            AxisAlignedBB axisalignedbb = this.i.getBoundingBox();

            for (int i = 1; i < d3; ++i) {
                axisalignedbb = axisalignedbb.d(d4, d5, d6);
                if (!this.i.world.getCubes(this.i, axisalignedbb).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }
}
