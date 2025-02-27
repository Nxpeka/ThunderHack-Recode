package thunder.hack.utility.player;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import static thunder.hack.modules.Module.mc;

public final class InventoryUtility {
    private static int cachedSlot = -1;


    public static int getItemCount(Item item) {
        if (mc.player == null) return 0;

        int counter = 0;

        for (int i = 0; i <= 44; ++i) {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            counter += itemStack.getCount();
        }

        return counter;
    }

    public static SearchInvResult getCrystal() {
        if (mc.player == null) return SearchInvResult.notFound();

        if (mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL) {
            return new SearchInvResult(mc.player.getInventory().selectedSlot, true, mc.player.getMainHandStack());
        }

        return findItemInHotBar(Items.END_CRYSTAL);
    }

    public static SearchInvResult getXp() {
        if (mc.player == null) return SearchInvResult.notFound();

        ItemStack stack = mc.player.getMainHandStack();
        if (!stack.isEmpty() && stack.getItem() instanceof ExperienceBottleItem) {
            return new SearchInvResult(mc.player.getInventory().selectedSlot, true, stack);
        }

        return findItemInHotBar(Items.EXPERIENCE_BOTTLE);
    }

    public static SearchInvResult getAnchor() {
        if (mc.player == null) return SearchInvResult.notFound();

        ItemStack stack = mc.player.getMainHandStack();
        if (!stack.isEmpty() && stack.getItem().equals(Items.RESPAWN_ANCHOR)) {
            return new SearchInvResult(mc.player.getInventory().selectedSlot, true, stack);
        }

        return findItemInHotBar(Items.RESPAWN_ANCHOR);
    }

    public static SearchInvResult getGlowStone() {
        if (mc.player == null) return SearchInvResult.notFound();

        ItemStack stack = mc.player.getMainHandStack();
        if (!stack.isEmpty() && stack.getItem().equals(Items.GLOWSTONE)) {
            return new SearchInvResult(mc.player.getInventory().selectedSlot, true, stack);
        }

        return findItemInHotBar(Items.GLOWSTONE);
    }

    public static SearchInvResult getAxe() {
        if (mc.player == null) return SearchInvResult.notFound();
        int slot = -1;
        float f = 1.0F;

        for (int b1 = 9; b1 < 45; b1++) {
            ItemStack itemStack = mc.player.getInventory().getStack(b1);
            if (itemStack != null && itemStack.getItem() instanceof AxeItem axe) {
                float f1 = axe.getMaxDamage();
                f1 += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, itemStack);
                if (f1 > f) {
                    f = f1;
                    slot = b1;
                }
            }
        }

        if (slot == -1) return SearchInvResult.notFound();
        return new SearchInvResult(slot, true, mc.player.getInventory().getStack(slot));
    }

    public static SearchInvResult getPickAxe() {
        if (mc.player == null) return SearchInvResult.notFound();

        int slot = -1;
        float f = 1.0F;
        for (int b1 = 9; b1 < 45; b1++) {
            ItemStack itemStack = mc.player.getInventory().getStack(b1);
            if (itemStack != null && itemStack.getItem() instanceof PickaxeItem) {
                float f1 = 0;
                f1 += EnchantmentHelper.getLevel(Enchantments.EFFICIENCY, itemStack);
                if (f1 > f) {
                    f = f1;
                    slot = b1;
                }
            }
        }

        if (slot == -1) return SearchInvResult.notFound();
        return new SearchInvResult(slot, true, mc.player.getInventory().getStack(slot));
    }

    public static SearchInvResult getSword() {
        if (mc.player == null) return SearchInvResult.notFound();

        int slot = -1;
        float f = 1.0F;
        for (int b1 = 9; b1 < 45; b1++) {
            ItemStack itemStack = mc.player.getInventory().getStack(b1);
            if (itemStack != null && itemStack.getItem() instanceof SwordItem sword) {
                float f1 = sword.getMaxDamage();
                f1 += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, itemStack);
                if (f1 > f) {
                    f = f1;
                    slot = b1;
                }
            }
        }

        if (slot == -1) return SearchInvResult.notFound();
        return new SearchInvResult(slot, true, mc.player.getInventory().getStack(slot));
    }

    public static SearchInvResult getSwordHotbar() {
        if (mc.player == null) return SearchInvResult.notFound();

        int slot = -1;
        float f = 1.0F;
        for (int b1 = 0; b1 < 9; b1++) {
            ItemStack itemStack = mc.player.getInventory().getStack(b1);
            if (itemStack != null && itemStack.getItem() instanceof SwordItem sword) {
                float f1 = sword.getMaxDamage();
                f1 += EnchantmentHelper.getLevel(Enchantments.SHARPNESS, itemStack);
                if (f1 > f) {
                    f = f1;
                    slot = b1;
                }
            }
        }

        if (slot == -1) return SearchInvResult.notFound();
        return new SearchInvResult(slot, true, mc.player.getInventory().getStack(slot));
    }

    // Replace with findBlockInHotBar
    @Deprecated
    public static int findHotbarBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            Block block;
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof BlockItem) || (block = ((BlockItem) stack.getItem()).getBlock()) != blockIn)
                continue;
            return i;
        }
        return -1;
    }

    // Needs rewrite
    @Deprecated
    public static int getElytra() {
        for (ItemStack stack : mc.player.getInventory().armor) {
            if (stack.getItem() == Items.ELYTRA && stack.getDamage() < 430) {
                return -2;
            }
        }

        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack s = mc.player.getInventory().getStack(i);
            if (s.getItem() == Items.ELYTRA && s.getDamage() < 430) {
                slot = i;
                break;
            }
        }

        if (slot < 9 && slot != -1) {
            slot = slot + 36;
        }

        return slot;
    }

    @Deprecated
    public static int getItemSlot(Item input) {
        if (input == mc.player.getOffHandStack().getItem()) return 999;
        for (int i = 36; i >= 0; i--) {
            final Item item = mc.player.getInventory().getStack(i).getItem();
            if (item == input) {
                if (i < 9) {
                    i += 36;
                }
                return i;
            }
        }
        return -1;
    }

    @Deprecated
    public static int getItemSlotHotbar(Item input) {
        for (int i = 0; i < 9; i++) if (mc.player.getInventory().getStack(i).getItem() == input) return i;
        return -1;
    }

    public static SearchInvResult findInHotBar(Searcher searcher) {
        if (mc.player != null) {
            for (int i = 0; i < 9; ++i) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (searcher.isValid(stack)) {
                    return new SearchInvResult(i, true, stack);
                }
            }
        }

        return SearchInvResult.notFound();
    }

    public static SearchInvResult findItemInHotBar(List<Item> items) {
        return findInHotBar(stack -> items.contains(stack.getItem()));
    }

    public static SearchInvResult findItemInHotBar(Item... items) {
        return findItemInHotBar(Arrays.asList(items));
    }

    public static SearchInvResult findInInventory(Searcher searcher) {
        if (mc.player != null) {
            for (int i = 36; i >= 0; i--) {
                ItemStack stack = mc.player.getInventory().getStack(i);
                if (searcher.isValid(stack)) {
                    if (i < 9) i += 36;
                    return new SearchInvResult(i, true, stack);
                }
            }
        }

        return SearchInvResult.notFound();
    }

    public static SearchInvResult findItemInInventory(List<Item> items) {
        return findInInventory(stack -> items.contains(stack.getItem()));
    }

    public static SearchInvResult findItemInInventory(Item... items) {
        return findItemInInventory(Arrays.asList(items));
    }

    public static SearchInvResult findBlockInHotBar(@NotNull List<Block> blocks) {
        return findItemInHotBar(blocks.stream().map(Block::asItem).toList());
    }

    public static SearchInvResult findBlockInHotBar(Block... blocks) {
        return findItemInHotBar(Arrays.stream(blocks).map(Block::asItem).toList());
    }

    public static SearchInvResult findBlockInInventory(@NotNull List<Block> blocks) {
        return findItemInInventory(blocks.stream().map(Block::asItem).toList());
    }

    public static SearchInvResult findBlockInInventory(Block... blocks) {
        return findItemInInventory(Arrays.stream(blocks).map(Block::asItem).toList());
    }

    public static void saveSlot() {
        cachedSlot = mc.player.getInventory().selectedSlot;
    }

    public static void returnSlot(){
        if(cachedSlot != -1)
            switchTo(cachedSlot);
        cachedSlot = -1;
    }

    public static void switchTo(int slot) {
        if(mc.player.getInventory().selectedSlot != slot)
            switchTo(slot, SwitchMode.All);
    }

    public static void switchTo(int slot, InventoryUtility.SwitchMode switchMode) {
        if(mc.player.getInventory().selectedSlot == slot)
            return;

        if (switchMode == InventoryUtility.SwitchMode.Normal || switchMode == InventoryUtility.SwitchMode.All) {
            mc.player.getInventory().selectedSlot = slot;
        }
        if (switchMode == InventoryUtility.SwitchMode.Packet || switchMode == InventoryUtility.SwitchMode.All) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }
    }

    public interface Searcher {
        boolean isValid(ItemStack stack);
    }

    public enum SwitchMode {
        Packet,
        Normal,
        All
    }
}
