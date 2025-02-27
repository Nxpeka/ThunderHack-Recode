package thunder.hack.modules.misc;

import com.google.common.eventbus.Subscribe;
import meteordevelopment.orbit.EventHandler;
import thunder.hack.Thunderhack;
import thunder.hack.events.impl.PacketEvent;
import thunder.hack.modules.Module;
import thunder.hack.setting.Setting;
import thunder.hack.utility.ThunderUtility;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;

public class AutoTpAccept extends Module {

    public AutoTpAccept() {
        super("AutoTPaccept", "Принимает тп автоматом", Category.MISC);
    }

    public Setting<Boolean> grief = new Setting<>("Grief", false);
    public Setting<Boolean> onlyFriends = new Setting<>("onlyFriends", true);


    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if(fullNullCheck()) return;
        if (event.getPacket() instanceof GameMessageS2CPacket) {
            final GameMessageS2CPacket packet = event.getPacket();
            if (mc.player == null) {
                return;
            }
            if (packet.content().getString().contains("телепортироваться")) {
                if (onlyFriends.getValue()) {
                    if (Thunderhack.friendManager.isFriend(ThunderUtility.solveName(packet.content().getString()))) {
                        if(grief.getValue()){
                            mc.getNetworkHandler().sendChatCommand("tpaccept " + ThunderUtility.solveName(packet.content().getString()));
                        } else {
                            mc.getNetworkHandler().sendChatCommand("tpaccept");
                        }
                    }
                } else {
                    if(grief.getValue()){
                        mc.getNetworkHandler().sendChatCommand("tpaccept " + ThunderUtility.solveName(packet.content().getString()));
                    } else {
                        mc.getNetworkHandler().sendChatCommand("tpaccept");
                    }
                }

            }
        }
    }


}
