#version 330


//Atributos do vértice: posição e cor
//São variáveis de entrada do shader, portanto, devem ser associadas a buffers pelo java
in vec3 aPosition;

//Matriz de transformação World. Deve ser carregada pelo Java.
uniform mat4 uWorld;

void main(){
    //Transforma a posição do triangulo coordenadas do modelo para coordenadas do mundo
    gl_Position = uWorld * vec4(aPosition, 1.0);
}