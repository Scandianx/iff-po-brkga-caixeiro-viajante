# Parametros Recomendados para Competicao

Este documento sugere parametros para rodar o BRKGA deste projeto em instancias com diferentes quantidades de vertices.

Importante:

- estes valores sao `recomendados`, nao garantidamente otimos
- o melhor conjunto real depende das instancias que o professor vai usar
- se a competicao priorizar `melhor custo`, vale preferir configuracoes mais agressivas
- se tambem houver limite de tempo forte, use a coluna de observacao para reduzir custo computacional

## Ordem dos parametros no `App`

O programa aceita:

```text
java -cp bin App <arquivo_entrada> [populacao] [geracoes] [elite] [mutantes] [heranca_elite] [semente]
```

Ou seja, os parametros ajustaveis sao:

- `populacao`
- `geracoes`
- `elite`
- `mutantes`
- `heranca_elite`
- `semente`

## Tabela recomendada

| Numero de vertices | Populacao | Geracoes | Elite | Mutantes | Heranca elite | Semente | Observacao |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | --- |
| 4 a 20 | 60 | 800 | 0.20 | 0.10 | 0.70 | 12345 | Instancias pequenas convergem rapido. Nao precisa exagerar. |
| 21 a 50 | 80 | 1500 | 0.20 | 0.15 | 0.70 | 12345 | Boa configuracao base para competir sem custo alto demais. |
| 51 a 100 | 120 | 2500 | 0.20 | 0.15 | 0.72 | 12345 | Faixa mais equilibrada entre exploracao e intensificacao. |
| 101 a 200 | 160 | 4000 | 0.18 | 0.18 | 0.72 | 12345 | Aumenta diversidade porque o espaco de busca cresce bastante. |
| 201 a 400 | 220 | 6000 | 0.18 | 0.20 | 0.73 | 12345 | Melhor para competir quando o foco principal e baixar custo final. |
| 401 a 700 | 300 | 8000 | 0.15 | 0.20 | 0.75 | 12345 | Mais vertices pedem mais busca e um pouco menos conservadorismo na elite. |
| 701 a 1000 | 400 | 10000 | 0.15 | 0.22 | 0.75 | 12345 | Configuracao pesada. Use se a maquina aguentar e o tempo permitir. |

## Leitura rapida da logica

- `populacao` maior ajuda a explorar mais solucoes por geracao
- `geracoes` maior ajuda a refinar mais a busca
- `elite` muito alta pode fazer o algoritmo convergir cedo demais
- `mutantes` ajudam a evitar que a populacao fique parecida cedo
- `heranca_elite` maior favorece intensificacao em torno de individuos bons

## Recomendacao pratica para a competicao

Se voce nao souber antes o tamanho das instancias, estas 3 configuracoes sao as mais uteis:

| Perfil | Populacao | Geracoes | Elite | Mutantes | Heranca elite | Quando usar |
| --- | ---: | ---: | ---: | ---: | ---: | --- |
| Conservador | 120 | 2500 | 0.20 | 0.15 | 0.70 | Quando houver pouco tempo de execucao |
| Equilibrado | 160 | 4000 | 0.18 | 0.18 | 0.72 | Melhor escolha geral |
| Agressivo | 300 | 8000 | 0.15 | 0.20 | 0.75 | Quando a prioridade for custo final e houver tempo |

## Comandos prontos

### Instancia pequena

```powershell
java -cp bin App examples\pcvs_exemplo.txt 60 800 0.20 0.10 0.70 12345
```

### Instancia media

```powershell
java -cp bin App examples\pcvs_gerado.txt 160 4000 0.18 0.18 0.72 12345
```

### Instancia grande

```powershell
java -cp bin App examples\pcvs_gerado.txt 300 8000 0.15 0.20 0.75 12345
```

## O que eu usaria

Se a competicao for cega e eu tiver que levar uma configuracao so, eu comecaria com:

```text
populacao = 160
geracoes = 4000
elite = 0.18
mutantes = 0.18
heranca_elite = 0.72
semente = 12345
```

Motivo:

- e mais forte que o padrao
- ainda nao e pesada demais
- segura melhor instancias medias e grandes
- reduz um pouco o risco de convergencia prematura

## Observacao importante sobre "melhores parametros"

Para descobrir os melhores parametros de verdade, o certo seria fazer `benchmark` com varias instancias e varias sementes, comparando:

- custo final medio
- melhor custo encontrado
- tempo medio
- estabilidade entre execucoes

Sem isso, o que existe aqui e uma `tabela recomendada de tuning inicial para competir`.
