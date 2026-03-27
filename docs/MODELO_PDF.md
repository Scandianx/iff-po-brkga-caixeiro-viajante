# Modelo Completo para o PDF do Trabalho

## 1. Titulo sugerido

Implementacao da Metaheuristica BRKGA para o Problema do Caixeiro Viajante Simetrico em Java

## 2. Integrantes

- Filipe Scandiani Soave
- Igor Almenara
- Raphael Castelar

## 3. Introducao

O Problema do Caixeiro Viajante Simetrico e um dos problemas classicos da Pesquisa Operacional e da Otimizacao Combinatoria. Seu objetivo e determinar uma rota de menor custo que permita partir de uma cidade inicial, visitar todas as demais cidades exatamente uma vez e retornar ao ponto de origem. Apesar de parecer simples em sua formulacao, trata-se de um problema de alta complexidade combinatoria, principalmente quando o numero de cidades cresce.

Neste trabalho foi desenvolvida uma implementacao em Java da metaheuristica BRKGA para resolver o Problema do Caixeiro Viajante Simetrico com origem fixa no vertice `1`. A escolha da BRKGA se deu por sua simplicidade de representacao, facilidade de gerar solucoes validas e boa capacidade de explorar diferentes regioes do espaco de busca.

## 4. Objetivo do trabalho

O objetivo do trabalho foi construir uma solucao em Java que:

- leia instancias do PCVS a partir de arquivo texto
- interprete a matriz de distancias corretamente
- considere o vertice `1` como origem e destino fixos
- gere uma populacao de solucoes candidatas
- aplique a BRKGA ao longo de varias geracoes
- retorne a melhor rota encontrada e seu custo total

## 5. Fundamentacao teorica

### 5.1 Problema do Caixeiro Viajante Simetrico

No PCVS, dado um conjunto de cidades e uma matriz de distancias simetrica, busca-se um ciclo Hamiltoniano de custo minimo. Neste trabalho, a cidade `1` foi fixada como ponto de partida e de chegada.

### 5.2 Metaheuristicas

Metaheuristicas sao tecnicas de busca aproximada usadas em problemas onde metodos exatos podem ser muito custosos. Elas nao garantem sempre a solucao otima global, mas costumam produzir resultados de boa qualidade em tempo viavel.

### 5.3 Algoritmos geneticos

Algoritmos geneticos trabalham com uma populacao de individuos candidatos e simulam, de forma abstrata, conceitos como selecao, heranca e variacao. Seus conceitos basicos sao:

- populacao
- individuo
- cromossomo
- fitness
- selecao
- crossover
- mutacao

### 5.4 BRKGA

BRKGA significa Biased Random-Key Genetic Algorithm. Nessa abordagem, cada individuo e representado por um vetor de random keys, e o crossover e enviesado para favorecer o pai que pertence a elite.

## 6. Estrategia usada na solucao

### 6.1 Representacao

Cada individuo foi representado por um vetor de numeros reais no intervalo `[0, 1)`. Como a cidade `1` e fixa, o cromossomo representa apenas as cidades internas, isto e, de `2` ate `n`.

A associacao entre cidade e random key e feita pela posicao no vetor. Se as cidades internas forem `[2, 3, 4, 5]` e o cromossomo for `[0.82, 0.15, 0.47, 0.03]`, entao a cidade `2` fica associada a `0.82`, a cidade `3` a `0.15`, a cidade `4` a `0.47` e a cidade `5` a `0.03`.

### 6.2 Decodificacao

As cidades internas sao associadas aos genes e ordenadas pelas random keys. A rota final e montada no formato:

```text
1 -> cidades ordenadas -> 1
```

Essa representacao foi escolhida porque facilita o crossover entre individuos sem quebrar a validade da solucao. Em vez de cruzar diretamente uma permutacao de cidades, o algoritmo cruza vetores numericos e depois ordena as cidades pelas chaves, gerando uma rota valida de forma simples.

### 6.3 Fitness

O fitness corresponde ao custo total da rota.

### 6.4 Elite

Os melhores individuos da populacao sao preservados como elite.

### 6.5 Mutantes

Uma parte da nova populacao e composta por mutantes aleatorios, para manter diversidade.

### 6.6 Crossover enviesado

Os filhos sao gerados por crossover entre um pai da elite e um pai da nao elite, com maior probabilidade de herdar genes do pai elite.

## 7. Estrutura do codigo

O projeto foi dividido em classes com responsabilidades bem definidas:

- `App`: ponto de entrada do programa
- `LeitorInstancia`: leitura do arquivo de entrada
- `InstanciaPCVS`: representacao e validacao da instancia
- `ParametrosBRKGA`: armazenamento dos parametros do algoritmo
- `IndividuoBRKGA`: representacao do individuo
- `SolucaoPCVS`: rota decodificada e custo total
- `BRKGAPCVS`: logica principal da metaheuristica
- `ResultadoBRKGA`: resultado final da execucao

## 8. Como executar

### 8.1 Compilacao

```bash
mkdir -p bin
javac -d bin src/*.java
```

### 8.2 Execucao com exemplo pequeno

```bash
java -cp bin App examples/pcvs_exemplo.txt
```

### 8.3 Execucao com exemplo maior

```bash
java -cp bin App examples/pcvs_exemplo_maior.txt 120 500 0.20 0.15 0.70 12345
```

## 9. Exemplo de execucao

Para a instancia pequena:

```text
4 6
0 7 1 3
7 0 5 8
1 5 0 6
3 8 6 0
```

Uma saida observada foi:

```text
Melhor rota encontrada: 1 -> 3 -> 2 -> 4 -> 1
Custo total: 17
Melhor geracao: 0
```

Verificacao manual:

```text
1 -> 3 = 1
3 -> 2 = 5
2 -> 4 = 8
4 -> 1 = 3
Total = 17
```

## 10. Discussao dos resultados

A implementacao mostrou-se funcional e coerente com a proposta da BRKGA. Mesmo em instancias pequenas, o algoritmo foi capaz de encontrar rapidamente rotas de baixo custo. Em instancias maiores, observou-se que a melhora tende a aparecer ao longo das geracoes, o que confirma o papel do processo evolutivo.

## 11. Pontos fortes e limitacoes

### Pontos fortes

- codigo simples e didatico
- estrutura bem separada em classes
- representacao eficiente com random keys
- facilidade de gerar rotas validas
- boa apresentacao visual da saida

### Limitacoes

- nao ha garantia de otimo global
- o desempenho depende da parametrizacao
- instancias maiores podem exigir mais tempo de execucao

## 12. Possiveis melhorias

Algumas extensoes futuras seriam:

- criterio de parada por estagnacao
- uso de busca local sobre as melhores rotas
- teste com varias instancias em lote
- comparacao com outras metaheuristicas
- exportacao dos resultados para arquivo

## 13. Conclusao

Conclui-se que a BRKGA foi uma escolha adequada para resolver o Problema do Caixeiro Viajante Simetrico neste trabalho. A abordagem permitiu uma implementacao clara, modular, funcional e didatica, ao mesmo tempo em que explorou conceitos importantes de Pesquisa Operacional, algoritmos geneticos e otimizacao heuristica.

## 14. Roteiro para apresentacao oral

Uma ordem recomendada para apresentar o trabalho e:

1. explicar o problema do caixeiro viajante
2. mostrar por que ele e dificil
3. explicar o que e algoritmo genetico
4. explicar o que e BRKGA
5. mostrar a matriz de distancias
6. mostrar como o cromossomo funciona
7. mostrar a decodificacao
8. explicar fitness, elite, mutantes e crossover
9. apresentar a estrutura do codigo
10. rodar um exemplo e comentar a saida

## 15. Texto de apoio para fala

```text
Neste trabalho implementamos a metaheuristica BRKGA para resolver o Problema do Caixeiro Viajante Simetrico.
A cidade 1 foi fixada como origem e destino. Cada individuo da populacao e representado por random keys
associadas as cidades internas. A decodificacao ordena essas chaves e gera uma rota valida. O custo da rota
e usado como fitness. Em cada geracao, preservamos os melhores individuos da elite, inserimos mutantes para
manter diversidade e geramos filhos por crossover enviesado, favorecendo genes do pai elite. Ao final,
retornamos a melhor rota encontrada e seu custo total.
```
