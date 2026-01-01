package daniking.birdsnests;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.NullMarked;

import static net.minecraft.util.Util.make;

@NullMarked
public class NestItem extends Item {
    public static final ResourceKey<LootTable> NEST_LOOT_TABLE_KEY = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(
                    BirdsNests.MODID,
                    "nest/nest_loot"
            )
    );

    public NestItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level world, Player user, InteractionHand hand) {
        final ItemStack stack = user.getItemInHand(hand);
        stack.consume(1, user);
        world.playSound(
                user,
                user.blockPosition(),
                SoundEvents.GRASS_BREAK,
                SoundSource.NEUTRAL,
                1.0F,
                1.0F
        );
        if (world instanceof ServerLevel serverWorld) {
            spawnLoot(serverWorld, user);
            return InteractionResult.SUCCESS;
        } else {
            return super.use(world, user, hand);
        }
    }

    private static void spawnLoot(ServerLevel world, Player player) {
        final LootTable table = world.getServer()
                .reloadableRegistries()
                .getLootTable(NEST_LOOT_TABLE_KEY);
        final RandomSource random = player.getRandom();
        table.getRandomItems(
                new LootParams.Builder(world)
                        .create(LootContextParamSets.EMPTY),
                stack -> world.addFreshEntity(make(
                        new ItemEntity(
                                world,
                                player.getX(),
                                player.getY() + 1.5D,
                                player.getZ(),
                                stack
                        ),
                        entity -> entity.setDeltaMovement(
                                random.nextGaussian() * 0.05F,
                                0.2D,
                                random.nextGaussian() * 0.05F
                        )
                ))
        );
    }
}
