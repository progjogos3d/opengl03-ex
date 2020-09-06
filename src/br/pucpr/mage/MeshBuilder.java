package br.pucpr.mage;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Collection;

import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 * Classe utilizada para a construção de novas malhas. Contém uma série de métodos para definição de atributos,
 * uniformes, index buffer e shader. Contém também versões do métodos com suporte a coleções, tipos da JOGL e outras
 * facilidades.
 */
public class MeshBuilder {
    private Mesh mesh;
    private Shader shader;

    public MeshBuilder(Shader shader) {
        mesh = new Mesh();
        this.shader = shader;
        glBindVertexArray(mesh.getId());
    }

    //Buffers de atributos
    //--------------------
    public MeshBuilder addBufferAttribute(String name, ArrayBuffer data) {
        mesh.addAttribute(name, data);
        shader.setAttribute(name, data);
        return this;
    }

    public MeshBuilder addBufferAttribute(String name, int elementSize, FloatBuffer values) {
        return addBufferAttribute(name, new ArrayBuffer(elementSize, values));
    }

    public MeshBuilder addFloatArrayAttribute(String name, int elementSize, float... values) {
        return addBufferAttribute(name, new ArrayBuffer(elementSize, values));
    }

    // Atributos do tipo Vector2
    // -------------------------
    public MeshBuilder addVector2fAttribute(String name, Collection<Vector2f> values) {
        try (var stack = MemoryStack.stackPush()) {
            var valueBuffer = stack.mallocFloat(values.size() * 2);
            values.forEach(value -> valueBuffer.put(value.x).put(value.y));
            valueBuffer.flip();
            return addBufferAttribute(name, 2, valueBuffer);
        }
    }

    public MeshBuilder addVector2fAttribute(String name, Vector2f... values) {
        return addVector2fAttribute(name, Arrays.asList(values));
    }

    public MeshBuilder addVector2fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 2, values);
    }

    // Atributos do tipo Vector3
    // -------------------------
    public MeshBuilder addVector3fAttribute(String name, Collection<Vector3f> values) {
        try (var stack = MemoryStack.stackPush()) {
            var valueBuffer = stack.mallocFloat(values.size() * 3);
            values.forEach(value -> valueBuffer.put(value.x).put(value.y).put(value.z));
            valueBuffer.flip();
            return addBufferAttribute(name, 3, valueBuffer);
        }
    }

    public MeshBuilder addVector3fAttribute(String name, Vector3f... values) {
        return addVector3fAttribute(name, Arrays.asList(values));

    }

    public MeshBuilder addVector3fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 3, values);
    }

    //Atributos do tipo Vector4
    //-------------------------
    public MeshBuilder addVector4fAttribute(String name, Collection<Vector4f> values) {
        try (var stack = MemoryStack.stackPush()) {
            var valueBuffer = stack.mallocFloat(values.size() * 4);
            values.forEach(value -> valueBuffer.put(value.x).put(value.y).put(value.z).put(value.w));
            valueBuffer.flip();
            return addBufferAttribute(name, 4, valueBuffer);
        }
    }

    public MeshBuilder addVector4fAttribute(String name, Vector4f... values) {
        return addVector4fAttribute(name, Arrays.asList(values));
    }

    public MeshBuilder addVector4fAttribute(String name, float... values) {
        return addFloatArrayAttribute(name, 4, values);
    }

    // Index buffer
    // ------------
    public MeshBuilder setIndexBuffer(IndexBuffer indexBuffer) {
        mesh.setIndexBuffer(indexBuffer);
        return this;
    }

    public MeshBuilder setIndexBuffer(IntBuffer data) {
        return setIndexBuffer(new IndexBuffer(data));
    }

    public MeshBuilder setIndexBuffer(Collection<Integer> data) {
        try (var stack = MemoryStack.stackPush()) {
            IntBuffer buffer = stack.mallocInt(data.size());
            data.forEach(buffer::put);
            buffer.flip();
            return setIndexBuffer(buffer);
        }
    }

    public MeshBuilder setIndexBuffer(int... data) {
        return setIndexBuffer(new IndexBuffer(data));
    }

    /**
     * Cria a malha previamente definida.
     * @return A malha criada.
     */
    public Mesh create() {
        return mesh.unbindAll();
    }
}
