package net.minecraft.server;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

// CraftBukkit start
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.entity.EntityInteractEvent;
// CraftBukkit end

public abstract class BlockButtonAbstract extends BlockDirectional {

    public static final BlockStateBoolean POWERED = BlockStateBoolean.of("powered");
    protected static final AxisAlignedBB b = new AxisAlignedBB(0.3125D, 0.875D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB c = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.125D, 0.625D);
    protected static final AxisAlignedBB d = new AxisAlignedBB(0.3125D, 0.375D, 0.875D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB e = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.125D);
    protected static final AxisAlignedBB f = new AxisAlignedBB(0.875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB g = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.125D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB B = new AxisAlignedBB(0.3125D, 0.9375D, 0.375D, 0.6875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB C = new AxisAlignedBB(0.3125D, 0.0D, 0.375D, 0.6875D, 0.0625D, 0.625D);
    protected static final AxisAlignedBB D = new AxisAlignedBB(0.3125D, 0.375D, 0.9375D, 0.6875D, 0.625D, 1.0D);
    protected static final AxisAlignedBB E = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.6875D, 0.625D, 0.0625D);
    protected static final AxisAlignedBB F = new AxisAlignedBB(0.9375D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
    protected static final AxisAlignedBB G = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.0625D, 0.625D, 0.6875D);
    private final boolean I;

    protected BlockButtonAbstract(boolean flag) {
        super(Material.ORIENTABLE);
        this.y(this.blockStateList.getBlockData().set(BlockButtonAbstract.FACING, EnumDirection.NORTH).set(BlockButtonAbstract.POWERED, Boolean.valueOf(false)));
        this.a(true);
        this.a(CreativeModeTab.d);
        this.I = flag;
    }

    @Override
    @Nullable
    public AxisAlignedBB a(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        return BlockButtonAbstract.k;
    }

    @Override
    public int a(World world) {
        return this.I ? 30 : 20;
    }

    @Override
    public boolean b(IBlockData iblockdata) {
        return false;
    }

    @Override
    public boolean c(IBlockData iblockdata) {
        return false;
    }

    @Override
    public boolean canPlace(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        return a(world, blockposition, enumdirection.opposite());
    }

    @Override
    public boolean canPlace(World world, BlockPosition blockposition) {
        EnumDirection[] aenumdirection = EnumDirection.values();
        int i = aenumdirection.length;

        for (int j = 0; j < i; ++j) {
            EnumDirection enumdirection = aenumdirection[j];

            if (a(world, blockposition, enumdirection)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean a(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        BlockPosition blockposition1 = blockposition.shift(enumdirection);

        return enumdirection == EnumDirection.DOWN ? world.getType(blockposition1).r() : world.getType(blockposition1).m();
    }

    @Override
    public IBlockData getPlacedState(World world, BlockPosition blockposition, EnumDirection enumdirection, float f, float f1, float f2, int i, EntityLiving entityliving) {
        return a(world, blockposition, enumdirection.opposite()) ? this.getBlockData().set(BlockButtonAbstract.FACING, enumdirection).set(BlockButtonAbstract.POWERED, Boolean.valueOf(false)) : this.getBlockData().set(BlockButtonAbstract.FACING, EnumDirection.DOWN).set(BlockButtonAbstract.POWERED, Boolean.valueOf(false));
    }

    @Override
    public void a(IBlockData iblockdata, World world, BlockPosition blockposition, Block block, BlockPosition blockposition1) {
        if (this.e(world, blockposition, iblockdata) && !a(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING).opposite())) {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
        }

    }

    private boolean e(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (this.canPlace(world, blockposition)) {
            return true;
        } else {
            this.b(world, blockposition, iblockdata, 0);
            world.setAir(blockposition);
            return false;
        }
    }

    @Override
    public AxisAlignedBB b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition) {
        EnumDirection enumdirection = iblockdata.get(BlockButtonAbstract.FACING);
        boolean flag = iblockdata.get(BlockButtonAbstract.POWERED).booleanValue();

        switch (enumdirection) {
        case EAST:
            return flag ? BlockButtonAbstract.G : BlockButtonAbstract.g;

        case WEST:
            return flag ? BlockButtonAbstract.F : BlockButtonAbstract.f;

        case SOUTH:
            return flag ? BlockButtonAbstract.E : BlockButtonAbstract.e;

        case NORTH:
        default:
            return flag ? BlockButtonAbstract.D : BlockButtonAbstract.d;

        case UP:
            return flag ? BlockButtonAbstract.C : BlockButtonAbstract.c;

        case DOWN:
            return flag ? BlockButtonAbstract.B : BlockButtonAbstract.b;
        }
    }

    @Override
    public boolean interact(World world, BlockPosition blockposition, IBlockData iblockdata, EntityHuman entityhuman, EnumHand enumhand, EnumDirection enumdirection, float f, float f1, float f2) {
        if (iblockdata.get(BlockButtonAbstract.POWERED).booleanValue()) {
            return true;
        } else {
            // CraftBukkit start
            boolean powered = (iblockdata.get(POWERED));
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            int old = (powered) ? 15 : 0;
            int current = (!powered) ? 15 : 0;

            BlockRedstoneEvent eventRedstone = BlockRedstoneEvent.of(block, old, current); // Torch
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if ((eventRedstone.getNewCurrent() > 0) != (!powered)) {
                return true;
            }
            // CraftBukkit end
            world.setTypeAndData(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(true)), 3);
            world.b(blockposition, blockposition);
            this.a(entityhuman, world, blockposition);
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.a(blockposition, this, this.a(world));
            return true;
        }
    }

    protected abstract void a(@Nullable EntityHuman entityhuman, World world, BlockPosition blockposition);

    protected abstract void b(World world, BlockPosition blockposition);

    @Override
    public void remove(World world, BlockPosition blockposition, IBlockData iblockdata) {
        if (iblockdata.get(BlockButtonAbstract.POWERED).booleanValue()) {
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
        }

        super.remove(world, blockposition, iblockdata);
    }

    @Override
    public int b(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return iblockdata.get(BlockButtonAbstract.POWERED).booleanValue() ? 15 : 0;
    }

    @Override
    public int c(IBlockData iblockdata, IBlockAccess iblockaccess, BlockPosition blockposition, EnumDirection enumdirection) {
        return !iblockdata.get(BlockButtonAbstract.POWERED).booleanValue() ? 0 : (iblockdata.get(BlockButtonAbstract.FACING) == enumdirection ? 15 : 0);
    }

    @Override
    public boolean isPowerSource(IBlockData iblockdata) {
        return true;
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {}

    @Override
    public void b(World world, BlockPosition blockposition, IBlockData iblockdata, Random random) {
        if (iblockdata.get(BlockButtonAbstract.POWERED).booleanValue()) {
            if (this.I) {
                this.d(iblockdata, world, blockposition);
            } else {
                // CraftBukkit start
                org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

                BlockRedstoneEvent eventRedstone = BlockRedstoneEvent.of(block, 15, 0); // Torch
                world.getServer().getPluginManager().callEvent(eventRedstone);

                if (eventRedstone.getNewCurrent() > 0) {
                    return;
                }
                // CraftBukkit end
                world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(false)));
                this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
                this.b(world, blockposition);
                world.b(blockposition, blockposition);
            }

        }
    }

    @Override
    public void a(World world, BlockPosition blockposition, IBlockData iblockdata, Entity entity) {
        if (this.I) {
            if (!iblockdata.get(BlockButtonAbstract.POWERED).booleanValue()) {
                this.d(iblockdata, world, blockposition);
            }
        }
    }

    private void d(IBlockData iblockdata, World world, BlockPosition blockposition) {
        List list = world.a(EntityArrow.class, iblockdata.d(world, blockposition).a(blockposition));
        boolean flag = !list.isEmpty();
        boolean flag1 = iblockdata.get(BlockButtonAbstract.POWERED).booleanValue();

        // CraftBukkit start - Call interact event when arrows turn on wooden buttons
        if (flag1 != flag && flag) {
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());
            boolean allowed = false;

            // If all of the events are cancelled block the button press, else allow
            for (Object object : list) {
                if (object != null) {
                    EntityInteractEvent event = new EntityInteractEvent(((Entity) object).getBukkitEntity(), block);
                    world.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        allowed = true;
                        break;
                    }
                }
            }

            if (!allowed) {
                return;
            }
        }
        // CraftBukkit end

        if (flag && !flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = BlockRedstoneEvent.of(block, 0, 15); // Torch
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() <= 0) {
                return;
            }
            // CraftBukkit end
            world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(true)));
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.b(blockposition, blockposition);
            this.a((EntityHuman) null, world, blockposition);
        }

        if (!flag && flag1) {
            // CraftBukkit start
            org.bukkit.block.Block block = world.getWorld().getBlockAt(blockposition.getX(), blockposition.getY(), blockposition.getZ());

            BlockRedstoneEvent eventRedstone = BlockRedstoneEvent.of(block, 15, 0); // Torch
            world.getServer().getPluginManager().callEvent(eventRedstone);

            if (eventRedstone.getNewCurrent() > 0) {
                return;
            }
            // CraftBukkit end
            world.setTypeUpdate(blockposition, iblockdata.set(BlockButtonAbstract.POWERED, Boolean.valueOf(false)));
            this.c(world, blockposition, iblockdata.get(BlockButtonAbstract.FACING));
            world.b(blockposition, blockposition);
            this.b(world, blockposition);
        }

        if (flag) {
            world.a(new BlockPosition(blockposition), this, this.a(world));
        }

    }

    private void c(World world, BlockPosition blockposition, EnumDirection enumdirection) {
        world.applyPhysics(blockposition, this, false);
        world.applyPhysics(blockposition.shift(enumdirection.opposite()), this, false);
    }

    @Override
    public IBlockData fromLegacyData(int i) {
        EnumDirection enumdirection;

        switch (i & 7) {
        case 0:
            enumdirection = EnumDirection.DOWN;
            break;

        case 1:
            enumdirection = EnumDirection.EAST;
            break;

        case 2:
            enumdirection = EnumDirection.WEST;
            break;

        case 3:
            enumdirection = EnumDirection.SOUTH;
            break;

        case 4:
            enumdirection = EnumDirection.NORTH;
            break;

        case 5:
        default:
            enumdirection = EnumDirection.UP;
        }

        return this.getBlockData().set(BlockButtonAbstract.FACING, enumdirection).set(BlockButtonAbstract.POWERED, Boolean.valueOf((i & 8) > 0));
    }

    @Override
    public int toLegacyData(IBlockData iblockdata) {
        int i;

        switch (iblockdata.get(BlockButtonAbstract.FACING)) {
        case EAST:
            i = 1;
            break;

        case WEST:
            i = 2;
            break;

        case SOUTH:
            i = 3;
            break;

        case NORTH:
            i = 4;
            break;

        case UP:
        default:
            i = 5;
            break;

        case DOWN:
            i = 0;
        }

        if (iblockdata.get(BlockButtonAbstract.POWERED).booleanValue()) {
            i |= 8;
        }

        return i;
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockRotation enumblockrotation) {
        return iblockdata.set(BlockButtonAbstract.FACING, enumblockrotation.a(iblockdata.get(BlockButtonAbstract.FACING)));
    }

    @Override
    public IBlockData a(IBlockData iblockdata, EnumBlockMirror enumblockmirror) {
        return iblockdata.a(enumblockmirror.a(iblockdata.get(BlockButtonAbstract.FACING)));
    }

    @Override
    protected BlockStateList getStateList() {
        return new BlockStateList(this, new IBlockState[] { BlockButtonAbstract.FACING, BlockButtonAbstract.POWERED});
    }
}
