# BRKGA para o Problema do Caixeiro Viajante Simetrico

Projeto em Java para a disciplina de Pesquisa Operacional no Instituto Federal Fluminense, implementando a metaheuristica BRKGA para resolver o Problema do Caixeiro Viajante Simetrico (PCVS) com origem fixa no vertice `1`.

## Estrutura do projeto

```text
brkga/
├── bin/
├── docs/
│   ├── ESTRATEGIA_BRKGA.md
│   ├── EXEMPLO_EXECUCAO.md
│   └── MODELO_PDF.md
├── examples/
│   ├── pcvs_exemplo.txt
│   └── pcvs_exemplo_maior.txt
└── src/
    ├── App.java
    ├── BRKGAPCVS.java
    ├── IndividuoBRKGA.java
    ├── InstanciaPCVS.java
    ├── LeitorInstancia.java
    ├── ParametrosBRKGA.java
    ├── ResultadoBRKGA.java
    └── SolucaoPCVS.java
```

## Requisitos

- Java 11 ou superior
- Terminal Linux/Ubuntu, Windows PowerShell ou equivalente

## Formato do arquivo de entrada

A primeira linha do arquivo deve conter:

```text
numero_de_vertices numero_de_arestas
```

As linhas seguintes devem conter a matriz completa de distancias do problema.

Exemplo:

```text
4 6
0 7 1 3
7 0 5 8
1 5 0 6
3 8 6 0
```

### Como interpretar a matriz

Cada linha representa a cidade de origem e cada coluna representa a cidade de destino.

No exemplo:

- linha 1, coluna 3 = `1`
- isso significa que a distancia da cidade `1` para a cidade `3` e `1`

- linha 4, coluna 2 = `8`
- isso significa que a distancia da cidade `4` para a cidade `2` e `8`

Como o problema e simetrico:

- linha 2, coluna 4 = `8`
- linha 4, coluna 2 = `8`

Os valores da diagonal principal sao `0` porque a distancia de uma cidade para ela mesma e zero.

## Compilacao no Linux/Ubuntu

```bash
mkdir -p bin
javac -d bin src/*.java
```

## Execucao no Linux/Ubuntu

Usando os parametros padrao:

```bash
java -cp bin App examples/pcvs_exemplo.txt
```

Usando parametros personalizados:

```bash
java -cp bin App examples/pcvs_exemplo.txt 80 300 0.20 0.15 0.70 12345
```

Ordem dos parametros opcionais:

1. tamanho da populacao
2. quantidade de geracoes
3. percentual de elite
4. percentual de mutantes
5. probabilidade de herdar gene do pai elite
6. semente aleatoria

## Compilacao no Windows PowerShell

```powershell
if (!(Test-Path bin)) { New-Item -ItemType Directory -Path bin | Out-Null }
javac -d bin src\*.java
```

## Execucao no Windows PowerShell

```powershell
java -cp bin App examples\pcvs_exemplo.txt
```

Para testar uma instancia maior:

```powershell
java -cp bin App examples\pcvs_exemplo_maior.txt 120 500 0.20 0.15 0.70 12345
```

Para gerar uma instancia nova no mesmo formato do projeto:

```powershell
java -cp bin GeradorInstanciaApp 500 1 1000 12345 examples\pcvs_500.txt
```

## Saida esperada

O programa informa:

- melhor rota encontrada
- custo total da rota
- geracao em que a melhor solucao apareceu
- quantidade de geracoes executadas
- tempo de execucao

## Arquivos de apoio

- `docs/ESTRATEGIA_BRKGA.md`: explicacao detalhada da estrategia usada
- `docs/EXEMPLO_EXECUCAO.md`: exemplo comentado passo a passo
- `docs/GERADOR_INSTANCIA.md`: como gerar instancias grandes para teste
- `docs/MODELO_PDF.md`: base textual para o relatorio em PDF
