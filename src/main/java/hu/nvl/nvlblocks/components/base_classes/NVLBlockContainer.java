package hu.nvl.nvlblocks.components.base_classes;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public abstract class NVLBlockContainer extends NVLBlock implements EntityBlock  {
	protected float RESISTANCE_WHEN_NOT_EMPTY = 5.0f;
	protected float RESISTANCE_WHEN_EMPTY = 5.0f;
	public NVLBlockContainer(Properties props) {
		super(props);
	}
	@Override
	public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof NVLBlockEntityInventory) ((NVLBlockEntityInventory) be).drops();
		return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
	}
	@Override
	public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof NVLBlockEntityInventory) ((NVLBlockEntityInventory) be).drops();
		super.onBlockExploded(state, level, pos, explosion);
	}
	@Override
	public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
		float f = RESISTANCE_WHEN_EMPTY;
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof NVLBlockEntityInventory && !((NVLBlockEntityInventory) be).isEmpty()) f = RESISTANCE_WHEN_NOT_EMPTY;
//		logLine("Pos:"+pos.toShortString()+"Explode:"+f);
		return f;
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
		boolean b = false;
		BlockEntity be = level.getBlockEntity(pos);
		if (be instanceof NVLBlockEntityInventory && ((NVLBlockEntityInventory) be).isEmpty()) b = super.canEntityDestroy(state, level, pos, entity);
//		logLine("Can destroy?"+b);
		return b;
	}

	@Override
	public float defaultDestroyTime() {
		float f = super.defaultDestroyTime();
		logLine("Destroy time:"+f);
		return f;
	}
	@Override
	public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
		logLine("canHarverstBlock: "+player.getName()+","+pos.toShortString());
		return true;
	}
}
