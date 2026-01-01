package daniking.birdsnests;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

public class BirdsNestsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(context -> context.addAfter(
                Items.WARPED_FUNGUS_ON_A_STICK,
                BirdsNests.NEST_ITEM
        ));
    }
}
