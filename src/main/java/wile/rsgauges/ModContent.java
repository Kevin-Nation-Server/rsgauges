/*
 * @file ModBlocks.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2018 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * Definition and initialisation of blocks of this
 * module, along with their tile entities if applicable.
 *
 * Note: Straight forward definition of different blocks/entities
 *       to make recipes, models and texture definitions easier.
 */
package wile.rsgauges;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wile.rsgauges.libmc.detail.Auxiliaries;
import wile.rsgauges.libmc.detail.Auxiliaries.IExperimentalFeature;
import wile.rsgauges.detail.ModResources;
import wile.rsgauges.blocks.*;
import wile.rsgauges.items.*;

import java.util.*;
import javax.annotation.Nonnull;


public class ModContent
{
  private static final String MODID = ModRsGauges.MODID;

  //--------------------------------------------------------------------------------------------------------------------
  // Registry auxiliary functions.
  //--------------------------------------------------------------------------------------------------------------------

  private static class ModRegistry
  {
    private static Block[] blocks_of_type(Class<? extends Block> clazz)
    { return BLOCKS.getEntries().stream().map(RegistryObject::get).filter(clazz::isInstance).toArray(Block[]::new); }

    private static <T extends RsBlock.RsTileEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> ctor, Block... blocks)
    {
      return TILE_ENTITIES.register(name, () -> BlockEntityType.Builder.of(ctor, blocks).build(null));
    }

    private static <T extends RsBlock.RsTileEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> ctor, Class<? extends Block> clazz)
    {
      return TILE_ENTITIES.register(name, () -> BlockEntityType.Builder.of(ctor, blocks_of_type(clazz)).build(null));
    }
  }

  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

  // -----------------------------------------------------------------------------------------------------------------
  // -- Internal constants, default block properties
  // -----------------------------------------------------------------------------------------------------------------

  private static final BlockBehaviour.Properties gauge_metallic_block_properties()
  {
    return BlockBehaviour.Properties.of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .noCollission()
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final BlockBehaviour.Properties gauge_glass_block_properties() {
    return (BlockBehaviour.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false)
    );
  }

  private static final BlockBehaviour.Properties indicator_metallic_block_properties()
  {
    return BlockBehaviour.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .lightLevel((state)->3)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final BlockBehaviour.Properties indicator_glass_block_properties()
  {
    return BlockBehaviour.Properties
        .of(Material.METAL, MaterialColor.METAL)
        .strength(0.5f, 15f)
        .sound(SoundType.METAL)
          .lightLevel((state)->3)
        .noOcclusion()
        .isValidSpawn((s,w,p,e)->false);
  }

  private static final BlockBehaviour.Properties alarm_lamp_block_properties()
  {
    return BlockBehaviour.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .noOcclusion()
      .lightLevel((state)->state.getValue(IndicatorBlock.POWERED)?12:2)
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final BlockBehaviour.Properties colored_sensitive_glass_block_properties()
  {
    return (BlockBehaviour.Properties
      .of(Material.BUILDABLE_GLASS, MaterialColor.METAL)
      .strength(0.35f, 15f)
      .sound(SoundType.METAL)
      .noOcclusion()
      .isValidSpawn((s,w,p,e)->false)
    );
  }

  private static final BlockBehaviour.Properties light_emitting_sensitive_glass_block_properties()
  {
    return BlockBehaviour.Properties
      .of(Material.BUILDABLE_GLASS, MaterialColor.METAL)
      .strength(0.35f, 15f)
      .sound(SoundType.METAL)
      .noOcclusion().emissiveRendering((s,w,p)->true)
      .lightLevel((state)->state.getValue(SensitiveGlassBlock.POWERED)?15:0)
      .isValidSpawn((s,w,p,e)->false);
  }

  private static final BlockBehaviour.Properties switch_metallic_block_properties()
  { return gauge_metallic_block_properties(); }

  private static final BlockBehaviour.Properties switch_glass_block_properties()
  { return gauge_glass_block_properties(); }

  private static final BlockBehaviour.Properties switch_metallic_faint_light_block_properties()
  {
    return BlockBehaviour.Properties
      .of(Material.METAL, MaterialColor.METAL)
      .strength(0.5f, 15f)
      .sound(SoundType.METAL)
      .lightLevel((state)->5);
  }

  // -----------------------------------------------------------------------------------------------------------------
  // -- industrual
  // -----------------------------------------------------------------------------------------------------------------

  // Contact lever switch
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_SMALL_LEVER = BLOCKS.register("industrial_small_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0,12,15,4),
    Auxiliaries.getPixeledAABB(4,1,0,12,12,4)
  ));

  // Mechanical lever
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_LEVER = BLOCKS.register("industrial_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,4,0,11,15,5),
    Auxiliaries.getPixeledAABB(5,1,0,11,12,5)
  ));

  // Mechanical rotary lever
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_ROTARY_LEVER = BLOCKS.register("industrial_rotary_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,4,0,12,12,6),
    Auxiliaries.getPixeledAABB(1,1,0,12,12,6)
  ));

  // Rotary machine switch
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_ROTARY_MACHINE_SWITCH = BLOCKS.register("industrial_rotary_machine_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  ));

  // Two-button machine switch
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_MACHINE_SWITCH = BLOCKS.register("industrial_machine_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null
  ));

  // ESTOP button
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_ESTOP_SWITCH = BLOCKS.register("industrial_estop_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE_OFF,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2.5),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 3.5)
  ));

  // Hopper blocking switch
  public static final RegistryObject<BistableSwitchBlock> INDUSTRIAL_HOPPER_BLOCKING_SWITCH = BLOCKS.register("industrial_hopper_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_DATA_WEAK,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 6.7),
    Auxiliaries.getPixeledAABB(3,10,0, 13, 12, 3.7)
  ));

  // Square machine pulse switch
  public static final RegistryObject<PulseSwitchBlock> INDUSTRIAL_BUTTON = BLOCKS.register("industrial_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  ));

  // Fenced round machine pulse switch
  public static final RegistryObject<PulseSwitchBlock> INDUSTRIAL_FENCED_BUTTON = BLOCKS.register("industrial_fenced_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0, 11, 11, 2), null
  ));

  // Retro double pole switch
  public static final RegistryObject<PulseSwitchBlock> INDUSTRIAL_DOUBLE_POLE_BUTTON = BLOCKS.register("industrial_double_pole_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 3),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  ));

  // Mechanical spring reset push button
  public static final RegistryObject<PulseSwitchBlock> INDUSTRIAL_FOOT_BUTTON = BLOCKS.register("industrial_foot_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,3,0, 11, 7, 4), null
  ));

  // Mechanical spring reset pull handle
  public static final RegistryObject<PulseSwitchBlock> INDUSTRIAL_PULL_HANDLE = BLOCKS.register("industrial_pull_handle", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 2)
  ));

  // Manual dimmer
  public static final RegistryObject<DimmerSwitchBlock> INDUSTRIAL_DIMMER = BLOCKS.register("industrial_dimmer", () -> new DimmerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,1,0, 12, 15, 2),
    null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f)
  ));

  // Door contact mat
  public static final RegistryObject<ContactMatBlock> INDUSTRIAL_DOOR_CONTACT_MAT = BLOCKS.register("industrial_door_contact_mat", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,0,0, 15, 1, 13), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Sensitive full size contact mat
  public static final RegistryObject<ContactMatBlock> INDUSTRIAL_CONTACT_MAT = BLOCKS.register("industrial_contact_mat", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Industrial shock sensor contact mat
  public static final RegistryObject<ContactMatBlock> INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT = BLOCKS.register("industrial_shock_sensitive_contact_mat", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 1, 16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Industrial trap door switch (shock vibration sensitive)
  public static final RegistryObject<TrapdoorSwitchBlock> INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR = BLOCKS.register("industrial_shock_sensitive_trapdoor", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 3.0f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f)
  ));

  // Industrial trap door switch (high sensitive shock vibration sensitive)
  public static final RegistryObject<TrapdoorSwitchBlock> INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR = BLOCKS.register("industrial_high_sensitive_trapdoor", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16, 16, 16),
    Auxiliaries.getPixeledAABB(0,2,0, 16, 1, 0.1),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    // Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  ));

  // Industrial trap door switch (item trap door)
  public static final RegistryObject<TrapdoorSwitchBlock> INDUSTRIAL_FALLTHROUGH_DETECTOR = BLOCKS.register("industrial_fallthrough_detector", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    Auxiliaries.getPixeledAABB(0.1,12.6,0.1, 15.9,13, 15.9),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
    null
  ));

  // Day time switch
  public static final RegistryObject<DayTimerSwitchBlock> INDUSTRIAL_DAY_TIMER = BLOCKS.register("industrial_day_timer", () -> new DayTimerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Interval signal timer
  public static final RegistryObject<IntervalTimerSwitchBlock> INDUSTRIAL_INTERVAL_TIMER = BLOCKS.register("industrial_interval_timer", () -> new IntervalTimerSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Infrared motion_sensor
  public static final RegistryObject<EntityDetectorSwitchBlock> INDUSTRIAL_ENTITY_DETECTOR = BLOCKS.register("industrial_entity_detector", () -> new EntityDetectorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Linear laser motion sensor
  public static final RegistryObject<EntityDetectorSwitchBlock> INDUSTRIAL_LINEAR_ENTITY_DETECTOR = BLOCKS.register("industrial_linear_entity_detector", () -> new EntityDetectorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Local light sensor
  public static final RegistryObject<EnvironmentalSensorSwitchBlock> INDUSTRIAL_LIGHT_SENSOR = BLOCKS.register("industrial_light_sensor", () -> new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Rain sensor switch
  public static final RegistryObject<EnvironmentalSensorSwitchBlock> INDUSTRIAL_RAIN_SENSOR = BLOCKS.register("industrial_rain_sensor", () -> new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_RAIN|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Lightning sensor switch
  public static final RegistryObject<EnvironmentalSensorSwitchBlock> INDUSTRIAL_LIGHTNING_SENSOR = BLOCKS.register("industrial_lightning_sensor", () -> new EnvironmentalSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_LIGHTNING|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Comparator output level observing switch
  public static final RegistryObject<ComparatorSwitchBlock> INDUSTRIAL_COMPARATOR_SWITCH = BLOCKS.register("industrial_comparator_switch", () -> new ComparatorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,10,0, 12, 15, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.1f, 1.2f)
  ));

  // Uni-directional block detector switch
  public static final RegistryObject<ObserverSwitchBlock> INDUSTRIAL_BLOCK_DETECTOR = BLOCKS.register("industrial_block_detector", () -> new ObserverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_SENSOR_BLOCKDETECT|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_BOTTOM|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_TOP|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_FRONT|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_LEFT|
    SwitchBlock.SWITCH_DATA_SIDE_ENABLED_RIGHT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  ));

  // Industrial bistable link receiver switch
  public static final RegistryObject<LinkReceiverSwitchBlock> INDUSTRIAL_SWITCHLINK_RECEIVER = BLOCKS.register("industrial_switchlink_receiver", () -> new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Industrial analog link receiver
  public static final RegistryObject<LinkReceiverSwitchBlock> INDUSTRIAL_SWITCHLINK_RECEIVER_ANALOG = BLOCKS.register("industrial_switchlink_receiver_analog", () -> new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_BISTABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    true
  ));

  // Industrial full block bistable link receiver switch
  public static final RegistryObject<LinkReceiverSwitchBlock> INDUSTRIAL_SWITCHLINK_CASED_RECEIVER = BLOCKS.register("industrial_switchlink_cased_receiver", () -> new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Industrial pulse link receiver switch
  public static final RegistryObject<LinkReceiverSwitchBlock> INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER = BLOCKS.register("industrial_switchlink_pulse_receiver", () -> new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Industrial full block pulse link receiver switch
  public static final RegistryObject<LinkReceiverSwitchBlock> INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER = BLOCKS.register("industrial_switchlink_cased_pulse_receiver", () -> new LinkReceiverSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16, 16, 16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Industrial bistable link relay
  public static final RegistryObject<LinkSenderSwitchBlock> INDUSTRIAL_SWITCHLINK_RELAY = BLOCKS.register("industrial_switchlink_relay", () -> new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Industrial analog link relay
  public static final RegistryObject<LinkSenderSwitchBlock> INDUSTRIAL_SWITCHLINK_RELAY_ANALOG = BLOCKS.register("industrial_switchlink_relay_analog", () -> new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    true
  ));

  // Industrial pulse link relay
  public static final RegistryObject<LinkSenderSwitchBlock> INDUSTRIAL_SWITCHLINK_PULSE_RELAY = BLOCKS.register("industrial_switchlink_pulse_relay", () -> new LinkSenderSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_LINK_SENDER|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_DATA_WEAK|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.9f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.01f, 1.7f),
    false
  ));

  // Bistable industrial knock surge detctor
  public static final RegistryObject<BistableKnockSwitchBlock> INDUSTRIAL_BISTABLE_KNOCK_SWITCH = BLOCKS.register("industrial_knock_switch", () -> new BistableKnockSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  ));

  // Pulse industrial knock surge detctor
  public static final RegistryObject<PulseKnockSwitchBlock> INDUSTRIAL_PULSE_KNOCK_SWITCH = BLOCKS.register("industrial_knock_button", () -> new PulseKnockSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|SwitchBlock.RSBLOCK_CONFIG_OPOSITE_PLACEMENT|SwitchBlock.RSBLOCK_CONFIG_FULLCUBE|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|SwitchBlock.SWITCH_DATA_SIDE_ENABLED_ALL,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0.5,0.5,0.5, 15.5, 15.5, 15.5), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.2f, 1.2f)
  ));

  public static final RegistryObject<GaugeBlock> INDUSTRIAL_ANALOG_GAUGE = BLOCKS.register("industrial_analog_angular_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  ));

  public static final RegistryObject<GaugeBlock> INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE = BLOCKS.register("industrial_analog_horizontal_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,4,0, 14, 12, 1)
  ));

  public static final RegistryObject<GaugeBlock> INDUSTRIAL_VERTICAL_BAR_GAUGE = BLOCKS.register("industrial_vertical_bar_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,2,0, 12, 14, 1)
  ));

  public static final RegistryObject<GaugeBlock> INDUSTRIAL_SMALL_DIGITAL_GAUGE = BLOCKS.register("industrial_small_digital_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,5,0, 12, 11, 1)
  ));

  public static final RegistryObject<GaugeBlock> INDUSTRIAL_TUBE_GAUGE = BLOCKS.register("industrial_tube_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,4,0, 9, 12, 3)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_ALARM_LAMP = BLOCKS.register("industrial_alarm_lamp", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    alarm_lamp_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 4)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_ALARM_SIREN = BLOCKS.register("industrial_alarm_siren", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,6.5,0, 11.5, 9.5, 4),
    new ModResources.BlockSoundEvent(ModResources.ALARM_SIREN_SOUND, 2f),
    null
  ));

  // square LED
  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_GREEN_LED_INDICATOR = BLOCKS.register("industrial_green_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_YELLOW_LED_INDICATOR = BLOCKS.register("industrial_yellow_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_RED_LED_INDICATOR = BLOCKS.register("industrial_red_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_WHITE_LED_INDICATOR = BLOCKS.register("industrial_white_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_GREEN_BLINK_LED_INDICATOR = BLOCKS.register("industrial_green_blinking_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR = BLOCKS.register("industrial_yellow_blinking_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_RED_BLINK_LED_INDICATOR = BLOCKS.register("industrial_red_blinking_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|GaugeBlock.GAUGE_DATA_BLINKING,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  public static final RegistryObject<IndicatorBlock> INDUSTRIAL_WHITE_BLINK_LED_INDICATOR = BLOCKS.register("industrial_white_blinking_led", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0, 10, 10, 0.5)
  ));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Rustic
  // -----------------------------------------------------------------------------------------------------------------

  // Rustic lever 1
  public static final RegistryObject<BistableSwitchBlock> RUSTIC_LEVER = BLOCKS.register("rustic_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,5,0, 10.3, 15, 4.5),
    Auxiliaries.getPixeledAABB(6,2,0, 10.3, 11, 4.5)
  ));

  // Rustic lever 2 (bolted)
  public static final RegistryObject<BistableSwitchBlock> RUSTIC_TWO_HINGE_LEVER = BLOCKS.register("rustic_two_hinge_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,6,0, 14,13,4.5),
    Auxiliaries.getPixeledAABB(2,4,0, 14,10,4.5)
  ));

  // Rustic lever 3 (big angular)
  public static final RegistryObject<BistableSwitchBlock> RUSTIC_ANGULAR_LEVER = BLOCKS.register("rustic_angular_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,10,0, 14,15,4.5),
    Auxiliaries.getPixeledAABB(6, 2,0, 14,15,4.5)
  ));

  // Rustic lever 7 (The Nail)
  public static final RegistryObject<BistableSwitchBlock> RUSTIC_NAIL_LEVER = BLOCKS.register("rustic_nail_lever", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  ));

  // Rustic button 1
  public static final RegistryObject<PulseSwitchBlock> RUSTIC_BUTTON = BLOCKS.register("rustic_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0,11,11,2.5), null
  ));

  // Rustic button 2 (bolted)
  public static final RegistryObject<PulseSwitchBlock> RUSTIC_SMALL_BUTTON = BLOCKS.register("rustic_small_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0,10,10,2.5), null
  ));

  // Rustic button 3 (pull chain)
  public static final RegistryObject<PulseSwitchBlock> RUSTIC_SPRING_RESET_CHAIN = BLOCKS.register("rustic_spring_reset_chain", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,3.5,0,11,15,4), null
  ));

  // Rustic button 7 (pull nail)
  public static final RegistryObject<PulseSwitchBlock> RUSTIC_NAIL_BUTTON = BLOCKS.register("rustic_nail_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,7,0, 9,10,3), null
  ));

  // Rustic door contact mat
  public static final RegistryObject<ContactMatBlock> RUSTIC_DOOR_CONTACT_PLATE = BLOCKS.register("rustic_door_contact_plate", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(1,0,0, 15,1,12),
    Auxiliaries.getPixeledAABB(1,0,0, 15,0.5,12),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  ));

  // Rustic full-size contact plate
  public static final RegistryObject<ContactMatBlock> RUSTIC_CONTACT_PLATE = BLOCKS.register("rustic_contact_plate", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  ));

  // Rustic shock sensor plate
  public static final RegistryObject<ContactMatBlock> RUSTIC_SHOCK_SENSITIVE_CONTACT_PLATE = BLOCKS.register("rustic_shock_sensitive_plate", () -> new ContactMatBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,1,16),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.5,16),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  ));

  // Rustic trap door switch (shock vibration sensitive)
  public static final RegistryObject<TrapdoorSwitchBlock> RUSTIC_SHOCK_SENSITIVE_TRAPDOOR = BLOCKS.register("rustic_shock_sensitive_trapdoor", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
  ));

  // Rustic trap door switch (high sensitive shock vibration sensitive)
  public static final RegistryObject<TrapdoorSwitchBlock> RUSTIC_HIGH_SENSITIVE_TRAPDOOR = BLOCKS.register("rustic_high_sensitive_trapdoor", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_SHOCK_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_HIGH_SENSITIVE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,15.6,0, 16,16,16),
    Auxiliaries.getPixeledAABB(0, 2.0,0, 16,16, 0.1),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.0f)
    //Auxiliaries.RsMaterials.MATERIAL_TRAPDOORSWITCH
  ));

  // Rustic trap door switch (item trap door)
  public static final RegistryObject<TrapdoorSwitchBlock> RUSTIC_FALLTHROUGH_DETECTOR = BLOCKS.register("rustic_fallthrough_detector", () -> new TrapdoorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    Auxiliaries.getPixeledAABB(0,12.6,0, 16,13,16),
    new ModResources.BlockSoundEvent(() -> SoundEvents.IRON_DOOR_CLOSE, 0.05f, 2.5f),
    null
  ));

  public static final RegistryObject<GaugeBlock> RUSTIC_CIRCULAR_GAUGE = BLOCKS.register("rustic_circular_gauge", () -> new GaugeBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2,2,0, 14,14,1)
  ));

  public static final RegistryObject<IndicatorBlock> RUSTIC_SEMAPHORE_INDICATOR = BLOCKS.register("rustic_semaphore", () -> new IndicatorBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT,
    indicator_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(3,4,0, 13,11,1),
    null,
    null
  ));


  // -----------------------------------------------------------------------------------------------------------------
  // -- Glass
  // -----------------------------------------------------------------------------------------------------------------

  // Thin star shaped glass switch
  public static final RegistryObject<BistableSwitchBlock> GLASS_ROTARY_SWITCH = BLOCKS.register("glass_rotary_switch", () -> new BistableSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  ));

  // Bistable glass touch switch
  public static final RegistryObject<BistableSwitchBlock> GLASS_TOUCH_SWITCH = BLOCKS.register("glass_touch_switch", () -> new BistableSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  ));

  // Thin star shaped glass button
  public static final RegistryObject<PulseSwitchBlock> GLASS_BUTTON = BLOCKS.register("glass_button", () -> new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  ));

  // Thin small star shaped glass button
  public static final RegistryObject<PulseSwitchBlock> GLASS_SMALL_BUTTON = BLOCKS.register("glass_small_button", () -> new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  ));

  // Glass touch button
  public static final RegistryObject<PulseSwitchBlock> GLASS_TOUCH_BUTTON = BLOCKS.register("glass_touch_button", () -> new PulseSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.5), null
  ));

  // Glass door plate
  public static final RegistryObject<ContactMatBlock> GLASS_DOOR_CONTACT_MAT = BLOCKS.register("glass_door_contact_mat", () -> new ContactMatBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  // Glass plate
  public static final RegistryObject<ContactMatBlock> GLASS_CONTACT_MAT = BLOCKS.register("glass_contact_mat", () -> new ContactMatBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(0,0,0, 16,0.25,16), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  // Glass Day time switch
  public static final RegistryObject<DayTimerSwitchBlock> GLASS_DAY_TIMER = BLOCKS.register("glass_day_timer", () -> new DayTimerSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_TIMER_DAYTIME|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  // Glass interval signal timer
  public static final RegistryObject<IntervalTimerSwitchBlock> GLASS_INTERVAL_TIMER = BLOCKS.register("glass_interval_timer", () -> new IntervalTimerSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|
    SwitchBlock.SWITCH_CONFIG_TIMER_INTERVAL|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  // Glass infrared motion sensor
  public static final RegistryObject<EntityDetectorSwitchBlock> GLASS_ENTITY_DETECTOR = BLOCKS.register("glass_entity_detector", () -> new EntityDetectorSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_SENSOR_VOLUME|
    SwitchBlock.SWITCH_CONFIG_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  // Glass laser motion sensor
  public static final RegistryObject<EntityDetectorSwitchBlock> GLASS_LINEAR_ENTITY_DETECTOR = BLOCKS.register("glass_linear_entity_detector", () -> new EntityDetectorSwitchBlock(
    SwitchBlock.SWITCH_CONFIG_TRANSLUCENT|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_SENSOR_LINEAR|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5.5,5.5,0,10.5,10.5,0.1), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.3f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.0f, 1.2f)
  ));

  public static final RegistryObject<GaugeBlock> GLASS_VERTICAL_BAR_GAUGE = BLOCKS.register("glass_vertical_bar_gauge", () -> new GaugeBlock(
    RsBlock.RSBLOCK_CONFIG_CUTOUT,
    gauge_glass_block_properties(),
    Auxiliaries.getPixeledAABB(7,3.7,0, 10,12,0.4)
  ));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Old Fancy
  // -----------------------------------------------------------------------------------------------------------------

  // Old fancy gold decorated lever
  public static final RegistryObject<BistableSwitchBlock> OLDFANCY_BISTABLE_SWITCH1 = BLOCKS.register("oldfancy_bistableswitch1", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6.5,0, 10.3,13.5,4.5),
    Auxiliaries.getPixeledAABB(6,3.5,0, 10.3,10.0,4.5)
  ));

  // Old fancy angular lever
  public static final RegistryObject<BistableSwitchBlock> OLDFANCY_BISTABLE_SWITCH2 = BLOCKS.register("oldfancy_bistableswitch2", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(2.5,6.0,0, 9.7,10,4.5),
    Auxiliaries.getPixeledAABB(4.5,3.5,0, 9.2,10,4.5)
  ));

  // Old fancy (golden decorated) button
  public static final RegistryObject<PulseSwitchBlock> OLDFANCY_BUTTON = BLOCKS.register("oldfancy_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6,6,0,10,10,1.5), null
  ));

  // Old fancy (golden decorated) chain pulse switch
  public static final RegistryObject<PulseSwitchBlock> OLDFANCY_SPRING_RESET_CHAIN = BLOCKS.register("oldfancy_spring_reset_chain", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(6.5,4.8,0,9.5,13,4),
    Auxiliaries.getPixeledAABB(6.5,3.8,0,9.5,12,4)
  ));

  // Old fancy (golden decorated) tiny button
  public static final RegistryObject<PulseSwitchBlock> OLDFANCY_SMALL_BUTTON = BLOCKS.register("oldfancy_small_button", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,7,0,9,9,1.5), null
  ));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Other
  // -----------------------------------------------------------------------------------------------------------------

  // Yellow power plant
  public static final RegistryObject<PowerPlantBlock> YELLOW_POWER_PLANT = BLOCKS.register("yellow_power_plant", () -> new PowerPlantBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
    // Auxiliaries.RsMaterials.MATERIAL_PLANT
  ));

  // Red power plant
  public static final RegistryObject<PowerPlantBlock> RED_POWER_PLANT = BLOCKS.register("red_power_plant", () -> new PowerPlantBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_CONTACT|SwitchBlock.SWITCH_CONFIG_LATERAL|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,5, 11,9,11), null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.GRASS_BREAK, 0.09f, 3.6f),
    new ModResources.BlockSoundEvent(() -> SoundEvents.GRASS_BREAK, 0.04f, 3.0f)
    // Auxiliaries.RsMaterials.MATERIAL_PLANT
  ));

  // Light flip switch
  public static final RegistryObject<BistableSwitchBlock> LIGHT_SWITCH = BLOCKS.register("light_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(7,6,0,9,10,1.5), null
  ));

  // Arrow target
  public static final RegistryObject<PulseSwitchBlock> ARROW_TARGET_SWITCH = BLOCKS.register("arrow_target", () -> new PulseSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|
    SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,5,0,11,11,1), null
  ));

  // Valve Wheel
  public static final RegistryObject<BistableSwitchBlock> BISTABLE_VALVE_WHEEL_SWITCH = BLOCKS.register("valve_wheel_switch", () -> new BistableSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_BISTABLE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0,12,12,3.5), null
  ));

  // Elevator button
  public static final RegistryObject<ElevatorSwitchBlock> ELEVATOR_BUTTON = BLOCKS.register("elevator_button", () -> new ElevatorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_WALLMOUNT|
    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
    SwitchBlock.SWITCH_CONFIG_PULSE_EXTENDABLE|SwitchBlock.SWITCH_CONFIG_PULSETIME_CONFIGURABLE|
    SwitchBlock.SWITCH_CONFIG_LCLICK_RESETTABLE|SwitchBlock.SWITCH_CONFIG_PROJECTILE_SENSE|
    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_faint_light_block_properties(),
    Auxiliaries.getPixeledAABB(4,4,0, 12, 12, 1), null
  ));

  // Door sensor
  public static final RegistryObject<DoorSensorSwitchBlock> DOOR_SENSOR_SWITCH = BLOCKS.register("door_sensor_switch", () -> new DoorSensorSwitchBlock(
    SwitchBlock.RSBLOCK_CONFIG_CUTOUT|
    SwitchBlock.SWITCH_CONFIG_PULSE|SwitchBlock.SWITCH_CONFIG_LATERAL_WALLMOUNT|SwitchBlock.SWITCH_CONFIG_WEAKABLE|
    SwitchBlock.SWITCH_CONFIG_INVERTABLE|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
    switch_metallic_block_properties(),
    Auxiliaries.getPixeledAABB(5,0,0, 11,1, 1.5),
    null,
    new ModResources.BlockSoundEvent(() -> SoundEvents.LEVER_CLICK, 0.05f, 2.5f),
    null
  ));

  // -----------------------------------------------------------------------------------------------------------------
  // -- sensitive glass
  // -----------------------------------------------------------------------------------------------------------------

  public static final RegistryObject<SensitiveGlassBlock> EMITTING_SENSITIVE_GLASS_BLOCK = BLOCKS.register("sensitive_glass_block", () -> new SensitiveGlassBlock(
    light_emitting_sensitive_glass_block_properties()
  ));

  public static final RegistryObject<SensitiveGlassBlock> COLORED_SENSITIVE_GLASS_BLOCK = BLOCKS.register("stained_sensitiveglass", () -> new SensitiveGlassBlock(
    colored_sensitive_glass_block_properties()
  ));

  // -----------------------------------------------------------------------------------------------------------------
  // -- Testing blocks
  // -----------------------------------------------------------------------------------------------------------------

  // Testing CUBE
//  public static final RegistryObject<SwitchBlock> TESTING_QUBE = BLOCKS.register("qube", () -> new SwitchBlock(
//    SwitchBlock.SWITCH_CONFIG_NOT_PASSABLE|
//    SwitchBlock.SWITCH_CONFIG_WEAKABLE|SwitchBlock.SWITCH_CONFIG_INVERTABLE|
//    SwitchBlock.SWITCH_CONFIG_TOUCH_CONFIGURABLE|
//    SwitchBlock.SWITCH_CONFIG_LINK_TARGET_SUPPORT|SwitchBlock.SWITCH_CONFIG_LINK_SOURCE_SUPPORT,
//    BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.METAL).strength(0.1f, 32000f).sound(SoundType.METAL),
//    new AABB(0,0,0,1,1,1), null,
//    null, null
//  ));

  //--------------------------------------------------------------------------------------------------------------------
  // Tile entities bound exclusively to the blocks above
  //--------------------------------------------------------------------------------------------------------------------

  private static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

  public static final RegistryObject<BlockEntityType<AbstractGaugeBlock.GaugeTileEntity>> TET_GAUGE = ModRegistry.register("te_gauge", AbstractGaugeBlock.GaugeTileEntity::new, AbstractGaugeBlock.class);
  public static final RegistryObject<BlockEntityType<SwitchBlock.SwitchTileEntity>> TET_SWITCH = ModRegistry.register("te_switch", SwitchBlock.SwitchTileEntity::new, SwitchBlock.class);
  public static final RegistryObject<BlockEntityType<ContactSwitchBlock.ContactSwitchTileEntity>> TET_CONTACT_SWITCH = ModRegistry.register("te_contact_switch", ContactSwitchBlock.ContactSwitchTileEntity::new, ContactSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<EntityDetectorSwitchBlock.DetectorSwitchTileEntity>> TET_DETECTOR_SWITCH = ModRegistry.register("te_detector_switch", EntityDetectorSwitchBlock.DetectorSwitchTileEntity::new, EntityDetectorSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity>> TET_ENVSENSOR_SWITCH = ModRegistry.register("te_envsensor_switch", EnvironmentalSensorSwitchBlock.EnvironmentalSensorSwitchTileEntity::new, EnvironmentalSensorSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<DayTimerSwitchBlock.DayTimerSwitchTileEntity>> TET_DAYTIMER_SWITCH = ModRegistry.register("te_daytimer_switch", DayTimerSwitchBlock.DayTimerSwitchTileEntity::new, DayTimerSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity>> TET_TIMER_SWITCH = ModRegistry.register("te_intervaltimer_switch", IntervalTimerSwitchBlock.IntervalTimerSwitchTileEntity::new, IntervalTimerSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<ComparatorSwitchBlock.ComparatorSwitchTileEntity>> TET_COMPARATOR_SWITCH = ModRegistry.register("te_comparator_switch", ComparatorSwitchBlock.ComparatorSwitchTileEntity::new, ComparatorSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<ObserverSwitchBlock.ObserverSwitchTileEntity>> TET_OBSERVER_SWITCH = ModRegistry.register("te_observer_switch", ObserverSwitchBlock.ObserverSwitchTileEntity::new, ObserverSwitchBlock.class);
  public static final RegistryObject<BlockEntityType<DoorSensorSwitchBlock.DoorSensorSwitchTileEntity>> TET_DOORSENSOR_SWITCH = ModRegistry.register("te_doorsensor_switch", DoorSensorSwitchBlock.DoorSensorSwitchTileEntity::new, DoorSensorSwitchBlock.class);

  //--------------------------------------------------------------------------------------------------------------------
  // Items
  //--------------------------------------------------------------------------------------------------------------------

  private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

  private static Item.Properties default_item_properties()
  { return (new Item.Properties()).tab(ModRsGauges.ITEMGROUP); }

  public static final RegistryObject<SwitchLinkPearlItem> SWITCH_LINK_PEARL = ITEMS.register("switchlink_pearl", () -> new SwitchLinkPearlItem(
    default_item_properties()
  ));

  //--------------------------------------------------------------------------------------------------------------------
  // Initialisation events
  //--------------------------------------------------------------------------------------------------------------------

  @Nonnull
  public static Collection<RegistryObject<Block>> getRegisteredBlocks()
  { return BLOCKS.getEntries(); }

  @Nonnull
  public static Collection<RegistryObject<Item>> getRegisteredItems()
  { return ITEMS.getEntries(); }

  public static boolean isExperimentalBlock(Block block)
  { return block instanceof IExperimentalFeature; }

  public static final void registerBlocks(IEventBus bus)
  { BLOCKS.register(bus); }

  public static final void registerItems(IEventBus bus)
  {
    for(RegistryObject<Block> entry : BLOCKS.getEntries()) {
      ResourceLocation location = entry.getId();
      ITEMS.register(location.getPath(), () -> new BlockItem(entry.get(), new Item.Properties().tab(ModRsGauges.ITEMGROUP)));
    }
    ITEMS.register(bus);
  }

  public static final void registerTileEntities(IEventBus bus)
  { TILE_ENTITIES.register(bus); }

  public static final void processRegisteredContent()
  {}

  @OnlyIn(Dist.CLIENT)
  public static void processContentClientSide(final FMLClientSetupEvent event)
  {
    // Block renderer selection
    for(RegistryObject<Block> entry: getRegisteredBlocks()) {
      Block block = entry.get();
      if(block instanceof RsBlock) {
        switch(((RsBlock)block).getRenderTypeHint()) {
          case CUTOUT: ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout()); break;
          case CUTOUT_MIPPED: ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped()); break;
          case TRANSLUCENT: ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent()); break;
          case SOLID: break;
        }
      }
    }
  }

}
