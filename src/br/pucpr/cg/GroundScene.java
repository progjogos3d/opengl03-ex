package br.pucpr.cg;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import br.pucpr.mage.*;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Exercício da aula 3
 *
 * O modo wireframe pode ser ligado/desligado pressionando-se a tecla M
 */
public class GroundScene implements Scene {
	private static final int WIDTH = 20;
	private static final int DEPTH = 10;

	private Keyboard keys = Keyboard.getInstance();
	private boolean wireframe = true;

	/** Representa a malha do quadrado. */

	private Mesh ground;

	private Mesh createGround(int width, int depth) {
		//A malha precisa ser centralizada. Por isso devemos subtrair metade da largura do eixo x e metade da
        // profundidade no eixo z.
		float hw = (width-1) / 2.0f;
		float hd = (depth-1) / 2.0f;

		// Criação dos vértices
		List<Vector3f> positions = new ArrayList<>();
		for (int z = 0; z < depth; z++) {
			for (int x = 0; x < width; x++) {
				positions.add(new Vector3f(x - hw, 0, z - hd));
			}
		}

		//Criação dos índices
        //Os índices são criados por quadrado, de 6 em 6.
        // Teremos sempre 1 quadrado a menos do que vertices. Por exemplo, num grid de 10x20 teremos 9x19 quadrados.
		List<Integer> indices = new ArrayList<>();
		for (int z = 0; z < depth - 1; z++) {
			for (int x = 0; x < width - 1; x++) {
				int zero = x + z * width;               //inferior esquerdo
				int one = (x + 1) + z * width;          //inferior direito
				int two = x + (z + 1) * width;          //superior esquerdo
				int three = (x + 1) + (z + 1) * width;  //superior direito

				indices.add(zero);
				indices.add(three);
				indices.add(one);

				indices.add(zero);
				indices.add(two);
				indices.add(three);
			}
		}

		return new MeshBuilder()
                .addVector3fAttribute("aPosition", positions)
                .setIndexBuffer(indices)
                .loadShader("/br/pucpr/resource/basic")
                .create();
	}

	@Override
	public void init() {
		//Define a cor de limpeza da tela
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		//------------------
		//Criação da malha
		//------------------
		ground = createGround(WIDTH, DEPTH);
	}

	@Override
	public void update(float secs) {
		//Testa se a tecla ESC foi pressionada
		if (keys.isPressed(GLFW_KEY_ESCAPE)) {
			//Fecha a janela, caso tenha sido
			glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
		} else if (keys.isPressed(GLFW_KEY_M)) {
			wireframe = !wireframe;
		}
	}

	@Override
	public void draw() {
		//Solicita a limpeza da tela
		glClear(GL_COLOR_BUFFER_BIT);

		glPolygonMode(GL_FRONT_AND_BACK, wireframe ? GL_LINE : GL_FILL);

		//Associa a transformação a malha
		ground.setUniform("uWorld", new Matrix4f().rotateX((float) Math.toRadians(40)).scale(2.0f / WIDTH));

		//Desenha
		ground.draw();
	}

	@Override
	public void deinit() {
	}

	public static void main(String[] args) {
		new Window(new GroundScene()).show();
	}
}