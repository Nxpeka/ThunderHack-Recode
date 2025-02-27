package thunder.hack.gui.thundergui.components;


import thunder.hack.gui.font.FontRenderers;
import thunder.hack.gui.thundergui.ThunderGui2;
import thunder.hack.setting.Setting;
import thunder.hack.utility.math.MathUtility;
import thunder.hack.utility.render.Render2DEngine;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.Objects;

public class SliderComponent extends SettingElement {

    private final float min;
    private final float max;
    public boolean listening;
    public String Stringnumber = "";
    private float animation;
    private double stranimation;
    private boolean dragging;


    public SliderComponent(Setting setting) {
        super(setting);
        this.min = ((Number) setting.getMin()).floatValue();
        this.max = ((Number) setting.getMax()).floatValue();
    }

    public static String removeLastChar(String str) {
        String output = "";
        if (str != null && str.length() > 0) {
            output = str.substring(0, str.length() - 1);
        }
        return output;
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack,mouseX, mouseY, partialTicks);
        if ((getY() > ThunderGui2.getInstance().main_posY + ThunderGui2.getInstance().height) || getY() < ThunderGui2.getInstance().main_posY) {
            return;
        }

        FontRenderers.modules.drawString(stack,getSetting().getName(), (float) getX(), (float) getY() + 5, isHovered() ? -1 : new Color(0xB0FFFFFF, true).getRGB(), false);

        double currentPos = (((Number) setting.getValue()).floatValue() - min) / (max - min);
        stranimation = stranimation + (((Number) setting.getValue()).floatValue() * 100 / 100 - stranimation) / 2.0D;
        animation = Render2DEngine.scrollAnimate(animation, (float) currentPos, .5f);

        Color color = new Color(0xFFE1E1E1);
        Render2DEngine.drawRound(stack,(float) (x + 54), (float) (y + height - 8), (float) (90), 1, 0.5f, new Color(0xff0E0E0E));
        Render2DEngine.drawRound(stack,(float) (x + 54), (float) (y + height - 8), (90) * animation, 1, 0.5f, color);
        Render2DEngine.drawRound(stack,(float) ((x + 52 + (90) * animation)), (float) (y + height - 9.5f), (float) 4, 4, 1.5f, color);

        if (mouseX > x + 154 && mouseX < x + 176 && mouseY > y + height - 11 && mouseY < y + height - 4) {
            Render2DEngine.drawRound(stack,(float) (x + 154), (float) (y + height - 11), 22, 7, 0.5f, new Color(82, 57, 100, 178));
        } else {
            Render2DEngine.drawRound(stack,(float) (x + 154), (float) (y + height - 11), 22, 7, 0.5f, new Color(50, 35, 60, 178));
        }

        if (!listening) {
            if (setting.getValue() instanceof Float)
                FontRenderers.modules.drawString(stack,String.valueOf(MathUtility.round((Float) setting.getValue(), 2)), (float) (x + 156), (float) (y + height - 9), new Color(0xBAFFFFFF, true).getRGB(), false);
            if (setting.getValue() instanceof Integer)
                FontRenderers.modules.drawString(stack,String.valueOf(setting.getValue()), (float) (x + 156), (float) (y + height - 9), new Color(0xBAFFFFFF, true).getRGB(), false);
        } else {
            if (Objects.equals(Stringnumber, "")) {
                FontRenderers.modules.drawString(stack,"...", (float) (x + 156), (float) (y + height - 9), new Color(0xBAFFFFFF, true).getRGB(), false);
            } else {
                FontRenderers.modules.drawString(stack,Stringnumber, (float) (x + 156), (float) (y + height - 9), new Color(0xBAFFFFFF, true).getRGB(), false);
            }
        }

        animation = MathUtility.clamp(animation, 0, 1);

        if (dragging)
            setValue(mouseX, x + 54, (90));

    }

    private void setValue(int mouseX, double x, double width) {
        double diff = ((Number) setting.getMax()).floatValue() - ((Number) setting.getMin()).floatValue();
        double percentBar = MathHelper.clamp((mouseX - x) / width, 0.0, 1.0);
        double value = ((Number) setting.getMin()).floatValue() + percentBar * diff;


        if (this.setting.getValue() instanceof Float) {
            this.setting.setValue((float) value);
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue((int) value);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if ((getY() > ThunderGui2.getInstance().main_posY + ThunderGui2.getInstance().height) || getY() < ThunderGui2.getInstance().main_posY) {
            return;
        }
        if (mouseX > x + 154 && mouseX < x + 176 && mouseY > y + height - 11 && mouseY < y + height - 4) {
            Stringnumber = "";
            this.listening = true;
        } else {
            if (button == 0 && hovered) {
                this.dragging = true;
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        this.dragging = false;
    }

    @Override
    public void resetAnimation() {
        dragging = false;
        animation = 0f;
        stranimation = 0;
    }

    @Override
    public void keyTyped(String typedChar, int keyCode) {
        if (this.listening) {
            switch (keyCode) {
                case 1: {
                    listening = false;
                    Stringnumber = "";
                    return;
                }
                case 28: {
                    try {
                        this.searchNumber();

                    } catch (Exception e) {
                        Stringnumber = "";
                        listening = false;
                    }
                }
                case 14: {
                    this.Stringnumber = removeLastChar(this.Stringnumber);
                }
            }
            this.Stringnumber = this.Stringnumber + typedChar;
        }
    }

    private void searchNumber() {
        if (this.setting.getValue() instanceof Float) {
            this.setting.setValue(Float.valueOf(Stringnumber));
            Stringnumber = "";
            listening = false;
        } else if (this.setting.getValue() instanceof Integer) {
            this.setting.setValue(Integer.valueOf(Stringnumber));
            Stringnumber = "";
            listening = false;
        }
    }

    @Override
    public void checkMouseWheel(float value) {
        super.checkMouseWheel(value);
        if (isHovered()) {
            ThunderGui2.scroll_lock = true;
        } else {
            return;
        }
        if (value < 0) {
            if (this.setting.getValue() instanceof Float) {
                this.setting.setValue((Float) setting.getValue() + 0.01f);
            } else if (this.setting.getValue() instanceof Integer) {
                this.setting.setValue((Integer) setting.getValue() + 1);
            }
        } else if (value > 0) {
            if (this.setting.getValue() instanceof Float) {
                this.setting.setValue((Float) setting.getValue() - 0.01f);
            } else if (this.setting.getValue() instanceof Integer) {
                this.setting.setValue((Integer) setting.getValue() - 1);
            }
        }
    }


}