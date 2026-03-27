# Guia Completo da BRKGA no Projeto

## 1. Objetivo deste material

Este documento foi escrito para servir como material de estudo e de apresentacao. A proposta e que, depois de ler este texto, a pessoa consiga:

- entender o Problema do Caixeiro Viajante Simetrico
- entender a ideia de algoritmo genetico
- entender o que e BRKGA
- entender como a BRKGA foi aplicada neste trabalho
- entender o fluxo do codigo Java
- explicar o projeto oralmente para o professor

## 2. O problema resolvido pelo trabalho

O problema tratado e o Problema do Caixeiro Viajante Simetrico, ou PCVS.

Nesse problema:

- existe um conjunto de cidades
- existe uma distancia entre cada par de cidades
- o caixeiro deve sair da cidade `1`
- deve visitar todas as outras cidades exatamente uma vez
- deve retornar para a cidade `1`
- o objetivo e minimizar a distancia total

O problema e chamado de simetrico porque a distancia da cidade `i` para a cidade `j` e a mesma da cidade `j` para a cidade `i`.

## 3. Por que esse problema e dificil

Se tivermos poucas cidades, ainda e possivel testar varias rotas manualmente. Mas, conforme o numero de cidades cresce, a quantidade de rotas possiveis cresce muito rapido.

Isso significa que, para instancias maiores, tentar testar todas as possibilidades nao e uma boa estrategia. E justamente por isso que usamos uma metaheuristica.

## 4. O que e uma metaheuristica

Uma metaheuristica e uma estrategia geral de busca usada para encontrar solucoes boas em problemas dificeis. Ela nao testa todas as combinacoes. Em vez disso, ela explora o espaco de busca de forma inteligente.

No caso deste trabalho, a metaheuristica escolhida foi a BRKGA.

## 5. O que e um algoritmo genetico

A BRKGA pertence a familia dos algoritmos geneticos.

Um algoritmo genetico trabalha com a ideia de evolucao de uma populacao de solucoes. A intuicao e a seguinte:

- comecamos com varias solucoes candidatas
- avaliamos quais sao melhores
- usamos as melhores para influenciar novas solucoes
- repetimos esse processo por varias geracoes

### 5.1 Termos importantes

#### Populacao

Conjunto de individuos avaliados ao mesmo tempo.

#### Individuo

Uma solucao candidata para o problema.

#### Cromossomo

Forma como o individuo e representado internamente.

#### Fitness

Medida de qualidade da solucao.

Neste trabalho, o fitness e o custo total da rota.

#### Selecao

Processo de escolha dos melhores individuos.

#### Crossover

Processo de combinacao entre dois pais para gerar um filho.

#### Mutacao ou diversidade

Mecanismo para evitar que todas as solucoes fiquem parecidas cedo demais.

## 6. O que e BRKGA

BRKGA significa:

```text
Biased Random-Key Genetic Algorithm
```

Ela e um algoritmo genetico com duas caracteristicas principais:

- usa random keys para representar solucoes
- usa crossover enviesado, favorecendo o pai elite

### 6.1 Random keys

Cada individuo e representado por um vetor de numeros reais no intervalo `[0, 1)`.

Esses numeros sao chamados de random keys.

Eles nao sao a rota diretamente. Eles apenas servem para definir a ordem das cidades depois da decodificacao.

### 6.2 Biased

Biased significa enviesado.

No crossover, cada gene do filho tem maior chance de vir do pai que pertence a elite. Isso faz com que boas caracteristicas tenham mais probabilidade de continuar aparecendo nas proximas geracoes.

## 7. Como a matriz de distancias funciona

A matriz de distancias e uma tabela que informa o custo de viajar entre duas cidades.

Se existem `n` cidades, a matriz tera `n` linhas e `n` colunas.

- a linha representa a cidade de origem
- a coluna representa a cidade de destino
- o valor guardado naquela posicao representa a distancia

Exemplo:

```text
0 7 1 3
7 0 5 8
1 5 0 6
3 8 6 0
```

Interpretacao:

- linha 1, coluna 2 = `7`, entao `dist(1,2) = 7`
- linha 1, coluna 3 = `1`, entao `dist(1,3) = 1`
- linha 4, coluna 2 = `8`, entao `dist(4,2) = 8`

Como o problema e simetrico:

- `dist(1,3) = 1`
- `dist(3,1) = 1`

Os zeros da diagonal significam que a distancia de uma cidade para ela mesma e zero.

## 8. Como a solucao e representada no projeto

A cidade `1` e fixa, porque o enunciado exige isso.

Logo, o cromossomo nao representa a cidade `1`. Ele representa somente as cidades internas:

```text
2, 3, 4, ..., n
```

Se a instancia tiver 5 cidades, por exemplo, o cromossomo tera 4 genes.

Exemplo:

```text
Cidade 2 -> 0.82
Cidade 3 -> 0.15
Cidade 4 -> 0.61
Cidade 5 -> 0.40
```

## 9. Como a decodificacao funciona

A decodificacao transforma o cromossomo em uma rota valida.

Usando o exemplo anterior:

```text
Cidade 2 -> 0.82
Cidade 3 -> 0.15
Cidade 4 -> 0.61
Cidade 5 -> 0.40
```

Ordenando as cidades pelas chaves em ordem crescente:

```text
Cidade 3 -> 0.15
Cidade 5 -> 0.40
Cidade 4 -> 0.61
Cidade 2 -> 0.82
```

Entao a ordem interna fica:

```text
3, 5, 4, 2
```

E a rota final se torna:

```text
1 -> 3 -> 5 -> 4 -> 2 -> 1
```

Essa estrategia e muito importante porque sempre gera uma permutacao valida das cidades internas.

## 10. O que e o fitness neste trabalho

O fitness e a medida de qualidade do individuo.

Neste projeto:

```text
fitness = custo total da rota
```

Como o objetivo e minimizar:

- menor custo = melhor individuo
- maior custo = pior individuo

Exemplo de calculo:

```text
1 -> 3 -> 2 -> 4 -> 1
```

Custo:

```text
dist(1,3) + dist(3,2) + dist(2,4) + dist(4,1)
1 + 5 + 8 + 3 = 17
```

## 11. Estrutura da BRKGA neste projeto

O algoritmo segue esta logica:

1. ler a instancia
2. gerar a populacao inicial
3. decodificar e avaliar todos os individuos
4. ordenar a populacao
5. separar elite e nao elite
6. gerar mutantes
7. gerar filhos por crossover enviesado
8. montar a nova populacao
9. repetir por varias geracoes
10. devolver a melhor solucao encontrada

## 12. Populacao inicial

A populacao inicial e formada por individuos aleatorios.

Cada individuo recebe um cromossomo com random keys. Depois:

- o cromossomo e decodificado
- a rota e avaliada
- o fitness e armazenado

No final, a populacao e ordenada do menor custo para o maior custo.

## 13. Elite

A elite e o conjunto dos melhores individuos da populacao atual.

Se a populacao tiver 100 individuos e o percentual de elite for 20 por cento, os 20 melhores formam a elite.

### Papel da elite

- preservar as melhores solucoes
- influenciar fortemente a proxima geracao

## 14. Mutantes

Mutantes sao individuos totalmente novos, gerados aleatoriamente.

No contexto deste projeto, eles servem para manter diversidade. Isso e importante porque, sem diversidade, a populacao pode ficar muito parecida cedo demais.

### Por que isso importa

Se todos os individuos ficarem parecidos:

- o algoritmo perde capacidade de explorar alternativas
- a busca pode parar em uma solucao boa, mas nao necessariamente a melhor possivel

## 15. Crossover enviesado

O crossover acontece entre:

- um pai da elite
- um pai da nao elite

Para cada gene do filho:

- com maior probabilidade, o gene vem do pai elite
- com menor probabilidade, o gene vem do pai nao elite

Se a probabilidade de herdar do pai elite for `0.70`, isso significa:

- 70 por cento de chance de copiar o gene do pai elite
- 30 por cento de chance de copiar o gene do pai nao elite

Esse mecanismo e o principal diferencial da BRKGA.

## 16. Atualizacao da populacao

A cada geracao, a nova populacao e composta por:

- elite preservada
- mutantes
- filhos produzidos por crossover enviesado

Depois disso, a populacao e ordenada novamente.

## 17. Melhor solucao global

Durante toda a execucao, o algoritmo guarda a melhor solucao encontrada em qualquer momento.

No final, ele nao devolve apenas a melhor da ultima geracao, e sim a melhor de toda a execucao.

## 18. Parametros importantes

### Tamanho da populacao

Controla quantos individuos existem por geracao.

### Quantidade de geracoes

Controla quantas evolucoes serao feitas.

### Percentual de elite

Controla quantos individuos muito bons sao preservados.

### Percentual de mutantes

Controla o nivel de diversidade aleatoria mantido na populacao.

### Probabilidade de heranca do pai elite

Controla o viés do crossover.

## 19. Como o codigo foi organizado

### `App.java`

Classe principal. Le argumentos, chama a leitura da instancia, executa o algoritmo e imprime o resultado.

### `LeitorInstancia.java`

Le o arquivo de entrada e monta a matriz de distancias.

### `InstanciaPCVS.java`

Armazena os dados do problema e valida a estrutura da instancia.

### `ParametrosBRKGA.java`

Guarda os parametros da BRKGA.

### `IndividuoBRKGA.java`

Representa um individuo da populacao.

### `SolucaoPCVS.java`

Armazena a rota decodificada e seu custo total.

### `BRKGAPCVS.java`

Contem a logica principal da metaheuristica.

### `ResultadoBRKGA.java`

Agrupa o resultado final da execucao.

## 20. Como apresentar esse codigo

Uma apresentacao clara pode seguir esta sequencia:

1. explicar o problema
2. explicar a matriz de distancias
3. explicar o que e algoritmo genetico
4. explicar o que e BRKGA
5. explicar random keys
6. explicar decodificacao
7. explicar fitness
8. explicar elite, mutantes e crossover enviesado
9. mostrar a estrutura das classes
10. mostrar uma execucao no terminal

## 21. Fala pronta para apresentacao

```text
Neste trabalho implementamos a BRKGA para resolver o Problema do Caixeiro Viajante Simetrico.
A cidade 1 foi fixada como origem e destino. Cada individuo da populacao e representado por random keys
associadas as cidades internas. A decodificacao ordena essas chaves e gera uma rota valida.
O custo total da rota e usado como fitness. Em cada geracao, preservamos a elite, inserimos mutantes
aleatorios para manter diversidade e geramos filhos por crossover enviesado, favorecendo genes do pai elite.
Ao final, o algoritmo retorna a melhor rota encontrada e seu custo total.
```

## 22. Conclusao

A BRKGA foi adequada para este trabalho porque oferece uma representacao simples, uma decodificacao clara e um processo genetico facil de explicar. O projeto ficou modular, comentado e pronto para ser apresentado tanto do ponto de vista teorico quanto do ponto de vista de implementacao.
