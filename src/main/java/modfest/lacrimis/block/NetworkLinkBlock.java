package modfest.lacrimis.block;

import com.zundrel.wrenchable.block.BlockWrenchable;
import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.NetworkLinkEntity;
import modfest.lacrimis.util.SoulTank;
import net.minecraft.block.*;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DyeColor;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;

public class NetworkLinkBlock extends BlockWithEntity implements DuctConnectBlock, BlockWrenchable {
    public NetworkLinkBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new NetworkLinkEntity();
    }

    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void onWrenched(World world, PlayerEntity player, BlockHitResult result) {
        NetworkLinkEntity linkEntity = ((NetworkLinkEntity) world.getBlockEntity(result.getBlockPos()));
        if(!world.isClient && linkEntity != null) {
            BlockPos pos = result.getBlockPos();
            float[] color = DyeColor.WHITE.getColorComponents();
            int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, pos.getX(), pos.getZ());

            int n;
            for (n = 0; n < 10 && pos.getY() <= l; ++n) {
                BlockState blockState = world.getBlockState(pos);
                Block block = blockState.getBlock();
                if (block instanceof Stainable) {
                    float[] fs = ((Stainable)block).getColor().getColorComponents();
                    if (!Arrays.equals(fs, color)) {
                        color[0] = (color[0] + fs[0]) / 2.0F;
                        color[1] = (color[1] + fs[1]) / 2.0F;
                        color[2] = (color[2] + fs[2]) / 2.0F;
                    }
                } else if (blockState.getOpacity(world, pos) >= 15 && block != Blocks.BEDROCK) {
                    color = DyeColor.WHITE.getColorComponents();
                    break;
                }

                pos = pos.up();
            }

            if(Arrays.equals(DyeColor.WHITE.getColorComponents(), color))
                linkEntity.setState(true, color, world);
            else
                linkEntity.setState(true, color, world);
        }
    }

    @Override
    public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
        NetworkLinkEntity linkEntity = ((NetworkLinkEntity) world.getBlockEntity(pos));
        if (linkEntity != null)
            linkEntity.setState(false, null, null);
    }

    @Override
    public boolean canConnectDuctTo(BlockPos pos, BlockView world, Direction side) {
        return side != Direction.UP;
    }

    @Override
    public int extractTears(BlockPos pos, BlockView world, int request, boolean simulate) {
        return 0;
    }

    @Override
    public boolean insert(BlockPos pos, BlockView world, Object value) {
        return false;
    }
}
