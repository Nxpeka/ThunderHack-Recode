package thunder.hack.gui.clickui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.DrawContext;
import thunder.hack.gui.font.FontRenderers;
import thunder.hack.modules.client.ClickGui;
import thunder.hack.utility.render.Render2DEngine;
import thunder.hack.utility.render.animation.Animation;
import thunder.hack.utility.render.animation.Direction;
import thunder.hack.utility.render.animation.EaseBackIn;
import thunder.hack.modules.Module;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class ModuleWindow extends AbstractWindow {

	private final List<ModuleButton> buttons;
	private final Identifier ICON;

	private final Animation animation = new EaseBackIn(270, 1f, 1.03f, Direction.BACKWARDS);

	private boolean scrollHover; // scroll hover

	public ModuleWindow(String name, List<Module> features, int index, double x, double y, double width, double height) {
		super(name, x, y, width, height);
		buttons = new ArrayList<>();
		ICON = new Identifier("textures/"+ name.toLowerCase() + ".png");
		features.forEach(feature -> {
			ModuleButton button = new ModuleButton(feature);
			button.setHeight(17);
			buttons.add(button);
		});
	}

	@Override
	public void init() {
		buttons.forEach(ModuleButton::init);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta, Color color) {
		super.render(context,mouseX, mouseY, delta, color);

		scrollHover = Render2DEngine.isHovered(mouseX, mouseY, x, y + height, width, 4000);

		animation.setDirection(isOpen() ? Direction.FORWARDS : Direction.BACKWARDS);
		context.getMatrices().push();


		//RoundedShader.drawRoundDoubleColor(matrixStack,(float) x + 2, (float) (y + height - 8), (float) width - 4, (float) ((getButtonsHeight() + 11) * animation.getOutput()), 3, ClickGui.getInstance().getColor(200),ClickGui.getInstance().getColor(0));
		Render2DEngine.drawRound(context.getMatrices(),(float) x + 3, (float) (y + height - 6), (float) width - 6, (float) ((getButtonsHeight() + 8) * animation.getOutput()), 3, ClickGui.getInstance().plateColor.getValue().getColorObject());

		if (animation.finished(Direction.FORWARDS)) {

			Render2DEngine.drawBlurredShadow(context.getMatrices(),(int) x + 4, (int) (y + height - 6), (int) width - 8, 8, 7, new Color(0, 0, 0, 180));

			for (ModuleButton button : buttons) {
				button.setX(x + 2);
				button.setY(y + height);
				button.setWidth(width - 4);
				button.setHeight(17);

				button.render(context,mouseX, mouseY, delta, color);
			}
		}
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		Render2DEngine.drawRoundD(context.getMatrices(),x + 2, y - 3, width - 4, height, 4,ClickGui.getInstance().catColor.getValue().getColorObject());

		Render2DEngine.drawTexture(context,ICON, (int) (x + 7), (int) (y + (height - 18) / 2), 12, 12);

		FontRenderers.categories.drawCenteredString(context.getMatrices(),getName(), ((int) x + 2 + (width - 4) / 2), (int) y + (int) height / 2f - 7, new Color(-1).getRGB());
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		context.getMatrices().pop();
		updatePosition();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int button) {

		if (button == 1 && hovered) {
			setOpen(!isOpen());
		}
		super.mouseClicked(mouseX, mouseY, button);

		if (isOpen() && scrollHover)
			buttons.forEach(b -> b.mouseClicked(mouseX, mouseY, button));
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		if (isOpen())
			buttons.forEach(b -> b.mouseReleased(mouseX, mouseY, button));
	}




	@Override
	public boolean keyTyped(int keyCode) {
		if (isOpen()) {
			for (ModuleButton button : buttons) {
				button.keyTyped(keyCode);
			}
		}
		return false;
	}


	@Override
	public void onClose() {
		super.onClose();
		buttons.forEach(ModuleButton::onGuiClosed);
	}

		private void updatePosition() {
		double offsetY = 0;
		double openY = 0;
		for (ModuleButton button : buttons) {
			button.setOffsetY(offsetY);
			if (button.isOpen()) {
				for (AbstractElement element : button.getElements()) {
					if (element.isVisible())
						offsetY += element.getHeight();
				}
				offsetY += 2;
			}
			offsetY += button.getHeight() + openY;
		}
	}


	public double getButtonsHeight() {
		double height = 0;
		for (ModuleButton button : buttons) {
			height += button.getElementsHeight();
			height += button.getHeight();
		}
		return height;
	}
}
