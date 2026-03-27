# Exemplo de Execucao Comentado

## 1. Objetivo deste documento

Este documento explica passo a passo o que acontece quando o programa e executado. A ideia e que ele funcione como apoio para entender:

- a entrada
- o processamento
- a saida
- o que dizer durante uma apresentacao

## 2. Exemplo pequeno

### 2.1 Comando usado

```bash
java -cp bin App examples/pcvs_exemplo.txt 80 300 0.20 0.15 0.70 12345
```

### 2.2 Arquivo de entrada

```text
4 6
0 7 1 3
7 0 5 8
1 5 0 6
3 8 6 0
```

### 2.3 Leitura da primeira linha

```text
4 6
```

Significa:

- 4 vertices
- 6 arestas

Como o grafo e completo e simples, isso esta correto, pois:

```text
4 * (4 - 1) / 2 = 6
```

### 2.4 Leitura da matriz

A matriz informa as distancias entre as cidades.

Exemplos:

- da cidade 1 para a cidade 2 custa 7
- da cidade 1 para a cidade 3 custa 1
- da cidade 3 para a cidade 4 custa 6
- da cidade 4 para a cidade 1 custa 3

## 3. O que o programa faz internamente

### Passo 1

Le a instancia e monta a matriz de distancias.

### Passo 2

Como a cidade 1 e fixa, as cidades internas passam a ser:

```text
2, 3, 4
```

### Passo 3

O algoritmo gera a populacao inicial com 80 individuos aleatorios.

### Passo 4

Cada individuo recebe random keys.

Exemplo:

```text
Cidade 2 -> 0.82
Cidade 3 -> 0.10
Cidade 4 -> 0.56
```

### Passo 5

As cidades sao ordenadas pelas chaves:

```text
3, 4, 2
```

### Passo 6

A rota final decodificada fica:

```text
1 -> 3 -> 4 -> 2 -> 1
```

### Passo 7

O custo dessa rota e calculado:

```text
1 -> 3 = 1
3 -> 4 = 6
4 -> 2 = 8
2 -> 1 = 7
Total = 22
```

### Passo 8

Depois de avaliar todos os individuos, a populacao e ordenada do menor custo para o maior custo.

## 4. O que acontece em cada geracao

### Elite

Os melhores individuos sao separados na elite.

Com populacao 80 e percentual de elite 0.20:

```text
80 * 0.20 = 16
```

Logo, os 16 melhores entram na elite.

### Mutantes

Com percentual de mutantes 0.15:

```text
80 * 0.15 = 12
```

Logo, 12 individuos novos sao criados aleatoriamente.

### Filhos

O restante da populacao e preenchido com filhos gerados por crossover enviesado.

Cada filho nasce da combinacao entre:

- um pai da elite
- um pai da nao elite

## 5. Interpretando a saida

Uma saida observada foi:

```text
=== BRKGA para o Problema do Caixeiro Viajante Simetrico ===
Arquivo de entrada: examples\pcvs_exemplo.txt
Numero de vertices: 4
Numero de arestas informado: 6
Tamanho da populacao: 80
Quantidade de geracoes: 300
Percentual de elite: 0.2
Percentual de mutantes: 0.15
Probabilidade de herdar gene do pai elite: 0.7
Semente aleatoria: 12345

Melhor rota encontrada: 1 -> 3 -> 2 -> 4 -> 1
Custo total: 17
Melhor geracao: 0
Geracoes executadas: 300
Tempo de execucao (ms): 33
```

### Melhor rota encontrada

Mostra a melhor ordem de visita das cidades.

### Custo total

Mostra a soma das distancias dessa rota.

No exemplo:

```text
1 -> 3 = 1
3 -> 2 = 5
2 -> 4 = 8
4 -> 1 = 3
Total = 17
```

### Melhor geracao

Quando aparece `0`, isso significa que a melhor solucao ja estava na populacao inicial.

Nao e erro.

### Geracoes executadas

Mostra quantas iteracoes evolutivas foram processadas.

### Tempo de execucao

Mostra o tempo total para resolver a instancia.

## 6. Exemplo maior

### 6.1 Comando

```bash
java -cp bin App examples/pcvs_exemplo_maior.txt 120 500 0.20 0.15 0.70 12345
```

### 6.2 Resultado obtido na validacao

```text
Melhor rota encontrada: 1 -> 5 -> 2 -> 3 -> 8 -> 7 -> 4 -> 6 -> 1
Custo total: 53
Melhor geracao: 3
```

Esse exemplo e util porque mostra que, em instancias maiores, a melhor solucao costuma surgir depois de algumas geracoes, e nao necessariamente na populacao inicial.

## 7. O que falar ao mostrar a execucao

Uma forma simples de explicar a execucao e:

```text
Primeiro o programa le a matriz de distancias.
Depois gera uma populacao inicial de cromossomos aleatorios.
Cada cromossomo e decodificado em uma rota valida.
O custo da rota vira o fitness.
Em cada geracao, preservamos os melhores individuos da elite,
criamos mutantes para manter diversidade e geramos filhos por crossover enviesado.
No final, mostramos a melhor rota encontrada.
```

## 8. Perguntas comuns da apresentacao

### Por que a cidade 1 nao esta no cromossomo

Porque o problema fixa a cidade 1 como origem e destino.

### Por que usar random keys

Porque elas permitem transformar facilmente um vetor aleatorio em uma ordem valida de cidades.

### Onde esta a mutacao

Neste projeto, a diversidade e mantida pelos mutantes aleatorios inseridos a cada geracao.

### O algoritmo sempre acha o otimo

Nao necessariamente. A BRKGA e uma metaheuristica. Ela busca solucoes muito boas, mas nao garante sempre o otimo global.

## 9. Conclusao

Depois de ler este documento, a pessoa deve conseguir explicar a entrada, o processamento interno e a saida do programa sem depender apenas do codigo.
