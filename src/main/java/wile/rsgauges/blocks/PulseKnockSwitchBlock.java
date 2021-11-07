/*
 * @file PulseKnockSwitchBlock.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Seismic mass based adjacent block "knock" detection activate.
 */
package wile.rsgauges.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraft.util.math.*;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.libmc.detail.PlayerBlockInteraction;

import javax.annotation.Nullable;


public class PulseKnockSwitchBlock extends PulseSwitchBlock implements PlayerBlockInteraction.INeighbourBlockInteractionSensitive
{
  public PulseKnockSwitchBlock(long config, AbstractBlock.Properties properties, AxisAlignedBB unrotatedBBUnpowered, @Nullable AxisAlignedBB unrotatedBBPowered, @Nullable ModResources.BlockSoundEvent powerOnSound, @Nullable ModResources.BlockSoundEvent powerOffSound)
  { super(config, properties, unrotatedBBUnpowered, unrotatedBBPowered, powerOnSound, powerOffSound); }

  @Override
  public boolean isCube()
  { return true; }

  // -------------------------------------------------------------------------------------------------------------------

  @Override
  public boolean onNeighborBlockPlayerInteraction(World world, BlockPos pos, BlockState state, BlockPos fromPos, LivingEntity entity, Hand hand, boolean isLeftClick)
  {
    Direction facing = state.getValue(SwitchBlock.FACING);
    if(!pos.relative(facing).equals(fromPos)) return false;
    onSwitchActivated(world, pos, state, null, facing);
    return false;
  }
}
