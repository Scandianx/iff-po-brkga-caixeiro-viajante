# Gerador de Instancias

Este projeto possui um gerador de instancias para criar arquivos grandes no mesmo formato de `examples/pcvs_exemplo.txt`.

O gerador cria:

- matriz completa
- matriz simetrica
- diagonal principal com `0`
- distancias inteiras positivas

O arquivo gerado sai pronto para ser usado pelo `App`.

## Formato do arquivo

O formato de saida e:

```text
numero_de_vertices numero_de_arestas
matriz completa de distancias
```

Exemplo:

```text
4 6
0 7 1 3
7 0 5 8
1 5 0 6
3 8 6 0
```

## Como compilar

No Windows PowerShell:

```powershell
javac -d bin src\*.java
```

## Como executar

Uso basico:

```powershell
java -cp bin GeradorInstanciaApp
```

Sem argumentos, ele usa:

- `100` vertices
- distancia minima `1`
- distancia maxima `999`
- semente `12345`
- saida em `examples/pcvs_gerado.txt`

## Argumentos opcionais

Ordem dos argumentos:

1. numero de vertices
2. distancia minima
3. distancia maxima
4. semente
5. caminho do arquivo de saida

Exemplo:

```powershell
java -cp bin GeradorInstanciaApp 500 1 1000 12345 examples\pcvs_500.txt
```

Esse comando gera:

- uma instancia com `500` vertices
- distancias entre `1` e `1000`
- valores reproduziveis por causa da semente `12345`
- arquivo salvo em `examples\pcvs_500.txt`

## Como testar no BRKGA

Depois de gerar a instancia:

```powershell
java -cp bin App examples\pcvs_500.txt
```

Se quiser testar uma instancia ainda maior:

```powershell
java -cp bin GeradorInstanciaApp 1000 1 1000 12345 examples\pcvs_1000.txt
java -cp bin App examples\pcvs_1000.txt 120 500 0.20 0.15 0.70 12345
```

## Observacao

Se voce usar a mesma quantidade de vertices, a mesma faixa de distancias e a mesma semente, o arquivo gerado sera igual toda vez.
