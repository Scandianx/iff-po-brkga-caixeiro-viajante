# Diferencas: BRKGA vs outros algoritmos para o PCVS/TSP

Este documento explica, de forma direta, o que diferencia a BRKGA de outras abordagens usadas para resolver o mesmo tipo de problema (como o Problema do Caixeiro Viajante Simetrico / TSP).

## 1. O problema: o que "resolver" significa aqui

Para instancias pequenas, e possivel buscar a solucao otima por metodos exatos.
Para instancias medias/grandes, normalmente buscamos **boas solucoes** em tempo viavel usando heuristicas e metaheuristicas.

O projeto deste repositorio usa **BRKGA** como metaheuristica.

## 2. BRKGA em uma frase

BRKGA (Biased Random-Key Genetic Algorithm) e um **algoritmo genetico** que:

- representa cada solucao como um vetor de numeros reais (random keys) no intervalo `[0,1)`
- transforma essas chaves em uma rota valida por uma **decodificacao** (ordenacao das chaves)
- gera filhos por **crossover enviesado** (biased), onde genes do pai elite tem maior chance de serem herdados

## 3. Diferenca principal: representacao (random keys) vs representacao direta

### 3.1 Algoritmos geneticos "classicos" para TSP

Em um GA tradicional para TSP, o cromossomo costuma ser a **permutacao das cidades** (uma rota).

Problema pratico:

- crossover em permutacoes e mais delicado
- e facil gerar filhos invalidos (cidade repetida, cidade faltando)
- exige operadores especificos (PMX, OX, CX etc.) e mais cuidado de implementacao

### 3.2 BRKGA (random keys)

Na BRKGA, o cromossomo e um vetor de reais.
A rota e obtida **depois**, pela decodificacao.

Efeito pratico:

- crossover vira "copiar genes" (como em vetores comuns)
- a solucao continua valida apos decodificar (nao aparece cidade repetida/faltando)
- a complexidade fica concentrada no **decodificador** (no PCVS deste projeto, ordenar chaves ja resolve)

Em resumo: **random keys trocam complexidade de operadores geneticos por uma decodificacao simples e garantias de viabilidade**.

## 4. Diferenca principal: crossover enviesado (biased) vs recombinacao neutra

Muitas metaheuristicas fazem "mistura" de informacao de forma mais neutra (ou com pressao seletiva mais indireta).
Na BRKGA, o vies e explicito:

- um pai vem da elite
- outro pai vem dos nao-elite
- cada gene do filho tem maior chance de vir do pai elite

Efeito pratico:

- a busca explora, mas tende a **reutilizar mais** caracteristicas de solucoes boas
- pode acelerar convergencia
- aumenta o risco de convergencia prematura se a diversidade for baixa (por isso existem os mutantes)

## 5. Comparacao rapida por familias de metodos

### 5.1 Metodos exatos (otimalidade garantida)

Exemplos: programacao inteira, branch-and-bound/branch-and-cut, dinamica (Held-Karp).

Como diferem da BRKGA:

- objetivo: provar o otimo, nao apenas achar um bom
- custo: podem explodir em tempo/memoria conforme `n` cresce
- resultado: quando terminam, entregam certeza de otimalidade

A BRKGA:

- nao prova otimalidade
- busca boas solucoes com limite de tempo/geracoes

### 5.2 Heuristicas construtivas e melhorias locais

Exemplos: vizinho mais proximo, insercao, 2-opt, 3-opt, Lin-Kernighan.

Como diferem da BRKGA:

- normalmente trabalham com 1 solucao (ou poucas) e fazem melhorias locais
- sao otimas para refinar uma rota rapidamente
- podem ficar presas em otimos locais (dependendo da heuristica)

A BRKGA:

- trabalha com uma populacao (muitas solucoes em paralelo)
- combina informacao de varias solucoes
- depende do decodificador e dos parametros (elite/mutantes/vies) para equilibrar exploracao e intensificacao

Obs.: e comum combinar BRKGA com busca local (memetico). Este projeto nao faz isso por simplicidade e foco didatico.

### 5.3 Outras metaheuristicas populares

#### Simulated Annealing (SA)

- base: aceita pioras controladas por uma "temperatura"
- diferenca: e uma busca estocastica em torno de uma solucao (ou poucas), nao populacional

#### Tabu Search (TS)

- base: memoria tabu para evitar ciclos e incentivar exploracao
- diferenca: controla a busca por memoria, nao por recombinacao de pais

#### Ant Colony Optimization (ACO)

- base: construcao probabilistica guiada por feromonio
- diferenca: enfatiza aprendizado coletivo via feromonio e construcao incremental de rotas

#### GRASP / ILS / VNS

- base: multi-start + melhorias locais/perturbacoes
- diferenca: exploram por reinicios/perturbacoes, nao por recombinacao genetica com vies por elite

A BRKGA se destaca quando:

- voce quer uma implementacao genetica simples (random keys + decodificador)
- quer controlar pressao seletiva de forma direta (elite + crossover enviesado)

## 6. Vantagens e desvantagens praticas da BRKGA neste projeto

### Vantagens

- representacao simples (vetor de reais) e decodificacao simples (ordenacao)
- crossover simples e eficiente
- solucao sempre valida apos decodificar
- facil explicar (elite, mutantes, vies) e ajustar por parametros

### Desvantagens / cuidados

- performance depende muito de parametros (tamanho populacao, elite, mutantes, vies, geracoes)
- pode convergir cedo demais se diversidade for insuficiente
- sem busca local, pode perder refinamentos finos que 2-opt/3-opt fazem muito bem
- nao garante otimalidade

## 7. Quando escolher BRKGA (regra de bolso)

Escolha BRKGA quando:

- voce precisa de boas solucoes em tempo previsivel
- quer um GA mais "limpo" para problemas combinatorios via random keys
- tem um decodificador simples (como ordenar chaves para uma permutacao)

Prefira metodos exatos quando:

- a instancia e pequena o suficiente e voce precisa provar o otimo

Prefira heuristicas/busca local quando:

- voce quer refinar muito rapido uma solucao (principalmente para TSP, 2-opt/3-opt sao basicos e fortes)

