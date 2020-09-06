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

	/** Representa a malha do quadrado. */

	private Mesh ground;
	private Shader shader;

	private Mesh createGround(Shader shader, int width, int depth) {
		//A malha precisa ser centralizada. Por isso devemos subtrair metade da largura do eixo x e metade da
        // profundidade no eixo z.
		var hw = (width-1) / 2.0f;
		var hd = (depth-1) / 2.0f;

		// Criação dos vértices
		var positions = new ArrayList<Vector3f>();
		for (var z = 0; z < depth; z++) {
			for (var x = 0; x < width; x++) {
				positions.add(new Vector3f(x - hw, 0, z - hd));
			}
		}

		//Criação dos índices
        //Os índices são criados por quadrado, de 6 em 6.
        // Teremos sempre 1 quadrado a menos do que vertices. Por exemplo, num grid de 10x20 teremos 9x19 quadrados.
		var indices = new ArrayList<Integer>();
		for (var z = 0; z < depth - 1; z++) {
			for (var x = 0; x < width - 1; x++) {
				var zero = x + z * width;               //inferior esquerdo
				var one = (x + 1) + z * width;          //inferior direito
				var two = x + (z + 1) * width;          //superior esquerdo
				var three = (x + 1) + (z + 1) * width;  //superior direito

				indices.add(zero);
				indices.add(three);
				indices.add(one);

				indices.add(zero);
				indices.add(two);
				indices.add(three);
			}
		}

		return new MeshBuilder(shader)
                .addVector3fAttribute("aPosition", positions)
                .setIndexBuffer(indices)
                .create();
	}

	@Override
	public void init() {
		//Define a cor de limpeza da tela
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		shader = Shader.loadProgram("basic");
		//Cria o chão, associa a ele uma rotação de 40 graus em x (para ficar visível) e ajusta a escala
		ground = createGround(shader, WIDTH, DEPTH)
			.setWireframe(true)
			.setUniform("uWorld",
				new Matrix4f()
					.rotateX((float) Math.toRadians(40))
					.scale(2.0f / WIDTH)
			);
	}

	@Override
	public void update(float secs) {
		//Testa se a tecla ESC foi pressionada
		if (keys.isPressed(GLFW_KEY_ESCAPE)) {
			//Fecha a janela, caso tenha sido
			glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
		} else if (keys.isPressed(GLFW_KEY_M)) {
			ground.setWireframe(!ground.isWireframe());
		}
	}

	@Override
	public void draw() {
		//Solicita a limpeza da tela
		glClear(GL_COLOR_BUFFER_BIT);

		//Desenha
		ground.draw(shader);
	}

	@Override
	public void deinit() {
	}

	public static void main(String[] args) {
		new Window(new GroundScene()).show();
	}
}