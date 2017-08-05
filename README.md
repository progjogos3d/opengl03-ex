# opengl03-ex

Solução do exercício sobre construir um chão.

O primeiro passo na solução é a criação dos vértices. Por serem quadrados, a criação é bastante simples, e é constituída
de uma repetição para os vértices das linhas, outra para o das colunas:

Por exemplo, no esquema 5x3 abaixo, os vértices seriam criados na seguinte ordem:
```
.1    .2    .3    .4    .5

.6    .7    .8    .9    .10

.11   .12   .13   .14    .15
```

Como o posicionamento deve ser centralizado, é necessário subtrair das coordenadas x e y da malha metade da largura e 
metade da altura. Observe que, como a distância entre os vértices é 1, e não há distância no vértice 0, essa dimensão 
será (largura-1)/2.0 e (altura-1)/2.0.
 
O código final de criação dos vértices será, portanto:
```java
float hw = (width-1) / 2.0f;
float hd = (depth-1) / 2.0f;

List<Vector3f> positions = new ArrayList<>();
for (int z = 0; z < depth; z++) {
    for (int x = 0; x < width; x++) {
        positions.add(new Vector3f(x - hw, 0, z - hd));
    }
}
```

A parte mais complicada é certamente a criação dos índices. Criaremos os índices quadrado. Porém, como descobrir
o índice do vértice dentro do IndexBuffer, se nele os vértices foram inseridos sequencialmente? 

Podemos converter um índice bidimensional (linha e coluna) em um índice bidimensional utilizando a fórmula:

indice = coluna + linha * largura

Observe que ao percorrer um quadrado, teremos os seguintes vértices
```
.0 (x,z)        .1 (x+1, z)   

.2 (x,z+1)      .3 (x+1, z+1)   
```

O codigo abaixo utiliza a fórmula acima para obte-los:
```java
int zero = x + z * width;               
int one = (x + 1) + z * width;          
int two = x + (z + 1) * width;          
int three = (x + 1) + (z + 1) * width;  
 ```
 
Agora, basta percorrermos cada quadrado, e fornecermos os índices de 6 em 6. Observe no esquema que uma malha 5x3 terá 
4x2 quadrados. Ou seja (linhas-1)x(colunas-1) quadrados. Cada quadrado será formado pelos índices 0,3,1 e 0,2,3, 
resultando no código:
 
 ```java
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
 ```

Formando assim a malha que desejamos. 