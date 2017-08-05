#version 330

//Matriz de transformação World. Deve ser carregada pelo Java.
uniform mat4 uWorld;

//Atributo dos do vértie
in vec3 aPosition;


void main(){
    //Transforma a posição do triangulo coordenadas do modelo para coordenadas do mundo
    gl_Position = uWorld * vec4(aPosition, 1.0);
}