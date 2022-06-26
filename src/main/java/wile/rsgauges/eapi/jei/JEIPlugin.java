/*
 * @file JEIPlugin.java
 * @author Stefan Wilhelm (wile)
 * @copyright (C) 2020 Stefan Wilhelm
 * @license MIT (see https://opensource.org/licenses/MIT)
 *
 * JEI plugin (see https://github.com/mezz/JustEnoughItems/wiki/Creating-Plugins)
 */
package wile.rsgauges.eapi.jei;
//public class JEIPlugin {}

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wile.rsgauges.ModRsGauges;
import wile.rsgauges.ModConfig;
import wile.rsgauges.ModContent;
import wile.rsgauges.libmc.detail.Auxiliaries;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@mezz.jei.api.JeiPlugin
public class JEIPlugin implements mezz.jei.api.IModPlugin
{
  @Override
  public ResourceLocation getPluginUid()
  { return new ResourceLocation(ModRsGauges.MODID, "jei_plugin_uid"); }

  @Override
  public void onRuntimeAvailable(IJeiRuntime jeiRuntime)
  {
    HashSet<Item> blacklisted = new HashSet<>();
    for(RegistryObject<Block> e: ModContent.getRegisteredBlocks()) {
      Block block = e.get();
      Item item = block.asItem();
      if(ModConfig.isOptedOut(block) && (ForgeRegistries.ITEMS.getKey(item).getPath()).equals((ForgeRegistries.BLOCKS.getKey(block).getPath()))) {
        blacklisted.add(item);
      }
    }
    for(RegistryObject<Item> e: ModContent.getRegisteredItems()) {
      Item item = e.get();
      if(ModConfig.isOptedOut(item) && (!(item instanceof BlockItem))) {
        blacklisted.add(item);
      }
    }
    if(!blacklisted.isEmpty()) {
      List<ItemStack> blacklist = blacklisted.stream().map(ItemStack::new).collect(Collectors.toList());
      try {
        jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, blacklist);
      } catch(Exception e) {
        Auxiliaries.logger().warn("Exception in JEI opt-out processing: '" + e.getMessage() + "', skipping further JEI optout processing.");
      }
    }
  }
}
