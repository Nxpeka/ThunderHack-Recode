package thunder.hack.modules.movement;

import com.google.common.eventbus.Subscribe;
import meteordevelopment.orbit.EventHandler;
import thunder.hack.events.impl.PlayerUpdateEvent;
import thunder.hack.modules.Module;
import thunder.hack.setting.Setting;

public class AutoSprint extends Module {
    public AutoSprint() {
        super("AutoSprint", "AutoSprint", Category.MOVEMENT);
    }

    public static final Setting<Boolean> sprint = new Setting<>("KeepSprint", true);
    public static final Setting<Float> motion = new Setting("motion", 1f, 0f, 1f, v-> sprint.getValue());

    @EventHandler
    public void onTick(PlayerUpdateEvent event) {
        if (mc.player.getHungerManager().getFoodLevel() <= 6) return;
        mc.player.setSprinting(mc.player.input.movementForward > 0 && !mc.player.isSneaking());
    }
}
