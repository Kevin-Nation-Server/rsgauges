package wile.rsgauges;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static wile.rsgauges.ModRsGauges.MODID;


public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> TUTORIAL_TAB = CREATIVE_MODE_TABS.register(MODID,
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModContent.INDUSTRIAL_SMALL_LEVER.get()))
                    .title(Component.translatable(MODID))
                    .displayItems((pParameters, pOutput) -> {


                        pOutput.accept(ModContent.INDUSTRIAL_SMALL_LEVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_LEVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ROTARY_LEVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ROTARY_MACHINE_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_MACHINE_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ESTOP_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_HOPPER_BLOCKING_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_BUTTON.get());
                        pOutput.accept(ModContent.INDUSTRIAL_FENCED_BUTTON.get());
                        pOutput.accept(ModContent.INDUSTRIAL_DOUBLE_POLE_BUTTON.get());
                        pOutput.accept(ModContent.INDUSTRIAL_FOOT_BUTTON.get());
                        pOutput.accept(ModContent.INDUSTRIAL_PULL_HANDLE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_DIMMER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_DOOR_CONTACT_MAT.get());
                        pOutput.accept(ModContent.INDUSTRIAL_CONTACT_MAT.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SHOCK_SENSITIVE_CONTACT_MAT.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SHOCK_SENSITIVE_TRAPDOOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_HIGH_SENSITIVE_TRAPDOOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_FALLTHROUGH_DETECTOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_DAY_TIMER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_INTERVAL_TIMER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ENTITY_DETECTOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_LINEAR_ENTITY_DETECTOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_LIGHT_SENSOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_RAIN_SENSOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_LIGHTNING_SENSOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_COMPARATOR_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_BLOCK_DETECTOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_RECEIVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_RECEIVER_ANALOG.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_CASED_RECEIVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_PULSE_RECEIVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_CASED_PULSE_RECEIVER.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_RELAY.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_RELAY_ANALOG.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SWITCHLINK_PULSE_RELAY.get());
                        pOutput.accept(ModContent.INDUSTRIAL_BISTABLE_KNOCK_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_PULSE_KNOCK_SWITCH.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ANALOG_GAUGE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ANALOG_HORIZONTAL_GAUGE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_VERTICAL_BAR_GAUGE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_SMALL_DIGITAL_GAUGE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_TUBE_GAUGE.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ALARM_LAMP.get());
                        pOutput.accept(ModContent.INDUSTRIAL_ALARM_SIREN.get());
                        pOutput.accept(ModContent.INDUSTRIAL_GREEN_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_YELLOW_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_RED_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_WHITE_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_GREEN_BLINK_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_YELLOW_BLINK_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_RED_BLINK_LED_INDICATOR.get());
                        pOutput.accept(ModContent.INDUSTRIAL_WHITE_BLINK_LED_INDICATOR.get());
                        pOutput.accept(ModContent.RUSTIC_LEVER.get());
                        pOutput.accept(ModContent.RUSTIC_TWO_HINGE_LEVER.get());
                        pOutput.accept(ModContent.RUSTIC_ANGULAR_LEVER.get());
                        pOutput.accept(ModContent.RUSTIC_NAIL_LEVER.get());
                        pOutput.accept(ModContent.RUSTIC_BUTTON.get());
                        pOutput.accept(ModContent.RUSTIC_SMALL_BUTTON.get());
                        pOutput.accept(ModContent.RUSTIC_SPRING_RESET_CHAIN.get());
                        pOutput.accept(ModContent.RUSTIC_NAIL_BUTTON.get());
                        pOutput.accept(ModContent.RUSTIC_DOOR_CONTACT_PLATE.get());
                        pOutput.accept(ModContent.RUSTIC_CONTACT_PLATE.get());
                        pOutput.accept(ModContent.RUSTIC_SHOCK_SENSITIVE_TRAPDOOR.get());
                        pOutput.accept(ModContent.RUSTIC_HIGH_SENSITIVE_TRAPDOOR.get());
                        pOutput.accept(ModContent.RUSTIC_FALLTHROUGH_DETECTOR.get());
                        pOutput.accept(ModContent.RUSTIC_CIRCULAR_GAUGE.get());
                        pOutput.accept(ModContent.RUSTIC_SEMAPHORE_INDICATOR.get());
                        pOutput.accept(ModContent.GLASS_ROTARY_SWITCH.get());
                        pOutput.accept(ModContent.GLASS_TOUCH_SWITCH.get());
                        pOutput.accept(ModContent.GLASS_BUTTON.get());
                        pOutput.accept(ModContent.GLASS_SMALL_BUTTON.get());
                        pOutput.accept(ModContent.GLASS_TOUCH_BUTTON.get());
                        pOutput.accept(ModContent.GLASS_DOOR_CONTACT_MAT.get());
                        pOutput.accept(ModContent.GLASS_CONTACT_MAT.get());
                        pOutput.accept(ModContent.GLASS_DAY_TIMER.get());
                        pOutput.accept(ModContent.GLASS_INTERVAL_TIMER.get());
                        pOutput.accept(ModContent.GLASS_ENTITY_DETECTOR.get());
                        pOutput.accept(ModContent.GLASS_VERTICAL_BAR_GAUGE.get());
                        pOutput.accept(ModContent.OLDFANCY_BISTABLE_SWITCH1.get());
                        pOutput.accept(ModContent.OLDFANCY_BISTABLE_SWITCH2.get());
                        pOutput.accept(ModContent.OLDFANCY_BUTTON.get());
                        pOutput.accept(ModContent.OLDFANCY_SPRING_RESET_CHAIN.get());
                        pOutput.accept(ModContent.OLDFANCY_SMALL_BUTTON.get());
                        pOutput.accept(ModContent.YELLOW_POWER_PLANT.get());
                        pOutput.accept(ModContent.RED_POWER_PLANT.get());
                        pOutput.accept(ModContent.LIGHT_SWITCH.get());
                        pOutput.accept(ModContent.ARROW_TARGET_SWITCH.get());
                        pOutput.accept(ModContent.BISTABLE_VALVE_WHEEL_SWITCH.get());
                        pOutput.accept(ModContent.ELEVATOR_BUTTON.get());
                        pOutput.accept(ModContent.DOOR_SENSOR_SWITCH.get());
                        pOutput.accept(ModContent.EMITTING_SENSITIVE_GLASS_BLOCK.get());
                        pOutput.accept(ModContent.COLORED_SENSITIVE_GLASS_BLOCK.get());

                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
