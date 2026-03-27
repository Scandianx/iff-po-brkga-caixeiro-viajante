# Explicacao da Logica do BRKGA

Este documento foi feito para apresentacao.
Ele foca somente nas funcoes que fazem parte da logica do algoritmo BRKGA neste projeto.

Nao entra no escopo:

- leitura de arquivo
- formatacao de saida
- classes auxiliares que nao explicam o funcionamento do BRKGA

## 1. `criarAleatorio`

### O que essa funcao faz

Essa funcao cria um individuo novo e totalmente aleatorio.

Traduzindo sem enfeite:

- um individuo e uma possivel resposta
- essa resposta ainda nao esta em forma de rota
- ela primeiro nasce como um vetor de numeros aleatorios
- esses numeros sao as `random keys`

No BRKGA, o cromossomo nao guarda diretamente a ordem das cidades.
Ele guarda chaves numericas.
Depois, o algoritmo ordena essas chaves para descobrir qual cidade vem antes e qual vem depois.

### Codigo

```java
public static IndividuoBRKGA criarAleatorio(int tamanhoCromossomo, Random geradorAleatorio) {
    // Cada gene e uma random key no intervalo [0, 1).
    // Depois essas chaves serao ordenadas para montar a rota.
    double[] cromossomo = new double[tamanhoCromossomo];

    for (int indiceGene = 0; indiceGene < tamanhoCromossomo; indiceGene++) {
        cromossomo[indiceGene] = geradorAleatorio.nextDouble();
    }

    return new IndividuoBRKGA(cromossomo);
}
```

### Explicacao brutalmente clara

Pensa assim:

- o algoritmo precisa criar varias respostas iniciais
- como ele ainda nao sabe qual resposta e boa, ele comeca chutando
- esse chute nao e uma rota pronta
- esse chute e uma lista de numeros aleatorios

Se o cromossomo tiver tamanho 4, ele pode nascer assim:

```text
[0.82, 0.15, 0.47, 0.03]
```

Isso ainda nao quer dizer "cidade 2, depois cidade 5, depois cidade 3".
Ainda nao.
So quer dizer:

- cada cidade interna recebeu uma chave
- depois vamos ordenar essas chaves
- a ordem crescente dessas chaves vai virar a rota

Entao essa funcao faz a etapa mais basica de todas:

- cria um vetor
- preenche com numeros aleatorios
- devolve esse vetor embrulhado dentro de um individuo

## 2. `gerarPopulacaoInicial`

### O que essa funcao faz

Cria a primeira populacao do algoritmo.

Traduzindo:

- populacao = conjunto de individuos
- cada individuo = uma solucao candidata
- a populacao inicial e feita so com individuos aleatorios

### Codigo

```java
public List<IndividuoBRKGA> gerarPopulacaoInicial() {
    // A populacao inicial e composta somente por individuos aleatorios.
    List<IndividuoBRKGA> populacao = new ArrayList<>();

    for (int indiceIndividuo = 0; indiceIndividuo < parametros.getTamanhoPopulacao(); indiceIndividuo++) {
        IndividuoBRKGA individuo = IndividuoBRKGA.criarAleatorio(cidadesInternas.length, geradorAleatorio);

        // Cada individuo precisa ser decodificado e avaliado antes de ser comparado.
        avaliarIndividuo(individuo);
        populacao.add(individuo);
    }

    // Ordena do menor custo para o maior custo.
    populacao.sort(Comparator.naturalOrder());
    return populacao;
}
```

### Explicacao brutalmente clara

Aqui o algoritmo monta o seu "time inicial".

Ele faz isso assim:

1. cria uma lista vazia
2. gera varios individuos aleatorios
3. avalia cada um deles
4. ordena do melhor para o pior

O ponto importante e este:

- nao basta criar o individuo
- o algoritmo precisa descobrir se ele e bom ou ruim
- por isso ele chama `avaliarIndividuo(individuo)`

No final, a populacao fica ordenada.
Entao:

- posicao `0` = melhor individuo
- ultimas posicoes = piores individuos

## 3. `decodificarCromossomo`

### O que essa funcao faz

Transforma o cromossomo de random keys em uma rota de verdade.

Essa e uma das funcoes mais importantes do BRKGA.
Sem ela, o algoritmo so teria um monte de numeros aleatorios sem significado pratico.

### Codigo

```java
public SolucaoPCVS decodificarCromossomo(double[] cromossomo) {
    // Ordena as cidades internas pelas random keys para formar uma permutacao valida.
    // Se a chave da cidade A for menor que a chave da cidade B,
    // a cidade A aparece antes da cidade B na rota.
    List<CidadeChave> cidadesComChaves = new ArrayList<>();

    for (int indice = 0; indice < cromossomo.length; indice++) {
        // Associa cada gene a uma cidade interna.
        cidadesComChaves.add(new CidadeChave(cidadesInternas[indice], cromossomo[indice]));
    }

    // A ordenacao das chaves e o que transforma o cromossomo em permutacao.
    cidadesComChaves.sort(Comparator.comparingDouble(CidadeChave::chaveAleatoria));

    // A rota final sempre comeca e termina na cidade 1.
    int[] rota = new int[cidadesInternas.length + 2];
    rota[0] = 1;
    rota[rota.length - 1] = 1;

    for (int indice = 0; indice < cidadesComChaves.size(); indice++) {
        // Preenche o miolo da rota com as cidades ordenadas.
        rota[indice + 1] = cidadesComChaves.get(indice).cidade();
    }

    // Depois de montar a rota, calculamos seu custo total.
    double custoTotal = calcularFitness(rota);
    return new SolucaoPCVS(rota, custoTotal);
}
```

### Explicacao brutalmente clara

Aqui acontece a magia das random keys.

Suponha que as cidades internas sejam:

```text
[2, 3, 4, 5]
```

E o cromossomo seja:

```text
[0.82, 0.15, 0.47, 0.03]
```

O algoritmo junta cada cidade com sua chave:

```text
2 -> 0.82
3 -> 0.15
4 -> 0.47
5 -> 0.03
```

Agora ele ordena pela chave:

```text
5 -> 0.03
3 -> 0.15
4 -> 0.47
2 -> 0.82
```

Pronto.
A ordem das cidades virou:

```text
[5, 3, 4, 2]
```

Como o problema fixa a cidade `1` no inicio e no fim, a rota final fica:

```text
[1, 5, 3, 4, 2, 1]
```

Ou seja:

- o cromossomo nao e a rota
- o cromossomo e um jeito indireto de montar a rota
- a ordenacao das chaves e o que converte os genes em sequencia de cidades

Essa ideia e importante porque:

- crossover entre numeros e facil
- mexer em permutacao diretamente e mais chato
- random keys simplificam a representacao

## 4. `calcularFitness`

### O que essa funcao faz

Calcula o custo total da rota.

No problema de caixeiro viajante, isso significa:

- somar a distancia entre cada par de cidades consecutivas

### Codigo

```java
public double calcularFitness(int[] rota) {
    // O custo total e a soma das distancias entre cidades consecutivas da rota.
    double custoTotal = 0.0;

    for (int indice = 0; indice < rota.length - 1; indice++) {
        custoTotal += instancia.getDistancia(rota[indice], rota[indice + 1]);
    }

    return custoTotal;
}
```

### Explicacao brutalmente clara

Se a rota for:

```text
[1, 5, 3, 4, 2, 1]
```

O algoritmo soma:

- distancia de `1` para `5`
- distancia de `5` para `3`
- distancia de `3` para `4`
- distancia de `4` para `2`
- distancia de `2` para `1`

Essa soma final e o fitness.

Neste projeto:

- fitness menor = melhor
- fitness maior = pior

Entao aqui nao existe misterio:
o algoritmo quer achar a rota mais barata.

## 5. `avaliarIndividuo`

### O que essa funcao faz

Pega um individuo e finalmente descobre quanto ele vale.

### Codigo

```java
private void avaliarIndividuo(IndividuoBRKGA individuo) {
    // A avaliacao transforma o cromossomo em rota e calcula seu custo.
    SolucaoPCVS solucao = decodificarCromossomo(individuo.getCromossomoCopia());
    individuo.setSolucao(solucao);
}
```

### Explicacao brutalmente clara

Um individuo nasce so com cromossomo.
So que cromossomo sozinho nao diz se ele e bom ou ruim.

Entao esta funcao faz duas coisas:

1. transforma o cromossomo em rota
2. guarda dentro do individuo a solucao com custo total

Depois disso, o individuo passa a poder ser comparado com outros.

Antes disso, ele e basicamente um candidato sem nota.

## 6. `selecionarElite`

### O que essa funcao faz

Separa os melhores individuos da populacao atual.

### Codigo

```java
public List<IndividuoBRKGA> selecionarElite(List<IndividuoBRKGA> populacao) {
    // A elite e formada pelos melhores individuos da populacao atual.
    int quantidadeElite = parametros.calcularQuantidadeElite();
    return new ArrayList<>(populacao.subList(0, quantidadeElite));
}
```

### Explicacao brutalmente clara

Como a populacao ja esta ordenada do melhor para o pior, selecionar elite e facil:

- pega o comeco da lista
- esses sao os melhores

Por que isso existe?

Porque o algoritmo nao quer jogar fora as melhores solucoes encontradas.

Entao a elite serve para:

- preservar qualidade
- garantir que individuos bons continuem vivos
- aumentar a chance de passar genes bons adiante

Se nao tivesse elite, o algoritmo poderia se perder e destruir boas solucoes por azar.

## 7. `gerarMutantes`

### O que essa funcao faz

Cria individuos totalmente novos, aleatorios, no meio do processo evolutivo.

### Codigo

```java
public List<IndividuoBRKGA> gerarMutantes() {
    // Mutantes sao individuos totalmente novos para manter diversidade.
    // Eles nao dependem da populacao atual.
    List<IndividuoBRKGA> mutantes = new ArrayList<>();

    for (int indiceMutante = 0; indiceMutante < parametros.calcularQuantidadeMutantes(); indiceMutante++) {
        IndividuoBRKGA mutante = IndividuoBRKGA.criarAleatorio(cidadesInternas.length, geradorAleatorio);
        avaliarIndividuo(mutante);
        mutantes.add(mutante);
    }

    return mutantes;
}
```

### Explicacao brutalmente clara

Se o algoritmo so mantivesse elite e filhos dos mesmos pais, ele poderia ficar viciado nas mesmas ideias.

Mutante existe para isso:

- baguncar um pouco
- trazer sangue novo
- impedir que a populacao fique toda parecida

Em outras palavras:

- elite conserva
- crossover combina
- mutante injeta novidade

Sem mutantes, o algoritmo pode travar cedo em solucoes meia-boca.

## 8. `crossoverEnviesado`

### O que essa funcao faz

Cria um filho misturando genes de dois pais.

Mas nao e uma mistura justa.
Ela e propositalmente enviesada para o pai elite.

E isso e o coracao do BRKGA.

### Codigo

```java
public IndividuoBRKGA crossoverEnviesado(IndividuoBRKGA paiElite, IndividuoBRKGA paiNaoElite) {
    // Cada gene do filho tem maior chance de ser herdado do pai elite.
    // Esse e o "vies" principal da BRKGA.
    double[] cromossomoFilho = new double[paiElite.getTamanhoCromossomo()];

    for (int indiceGene = 0; indiceGene < cromossomoFilho.length; indiceGene++) {
        boolean herdarDoPaiElite = geradorAleatorio.nextDouble() < parametros.getProbabilidadeHerancaElite();
        cromossomoFilho[indiceGene] = herdarDoPaiElite
            ? paiElite.getGene(indiceGene)
            : paiNaoElite.getGene(indiceGene);
    }

    // Depois de gerar o cromossomo do filho, ele tambem precisa ser avaliado.
    IndividuoBRKGA filho = new IndividuoBRKGA(cromossomoFilho);
    avaliarIndividuo(filho);
    return filho;
}
```

### Explicacao brutalmente clara

Aqui funciona assim:

- escolhe um pai bom, da elite
- escolhe um pai ruim ou pelo menos nao-elite
- para cada gene, sorteia de quem o filho vai herdar
- o pai elite tem mais chance de passar seu gene

Exemplo:

- probabilidade de herdar do elite = `0.70`

Isso quer dizer:

- para cada posicao do cromossomo
- existe 70% de chance de copiar o gene do pai elite
- e 30% de chance de copiar do pai nao elite

Por isso o nome `crossover enviesado`.
Ele e enviesado porque puxa mais para o lado do melhor pai.

A ideia e simples:

- aproveitar mais aquilo que ja mostrou ser bom
- sem ignorar totalmente o outro pai

Entao o filho nao e uma copia exata.
Ele e uma mistura, mas uma mistura puxando para o melhor lado.

## 9. `atualizarPopulacao`

### O que essa funcao faz

Monta a proxima geracao.

Essa e a funcao central da evolucao no BRKGA.

### Codigo

```java
public List<IndividuoBRKGA> atualizarPopulacao(List<IndividuoBRKGA> populacaoAtual) {
    // A nova populacao preserva elite e adiciona mutantes e filhos.
    // Essa e a etapa central de evolucao da BRKGA.
    List<IndividuoBRKGA> elite = selecionarElite(populacaoAtual);
    List<IndividuoBRKGA> naoElite = new ArrayList<>(
        populacaoAtual.subList(parametros.calcularQuantidadeElite(), populacaoAtual.size())
    );
    List<IndividuoBRKGA> novaPopulacao = new ArrayList<>();

    // Copia a elite diretamente para a nova geracao.
    novaPopulacao.addAll(elite);

    // Adiciona os mutantes aleatorios.
    novaPopulacao.addAll(gerarMutantes());

    // Gera os filhos por crossover enviesado ate completar a populacao.
    for (int indiceFilho = 0; indiceFilho < parametros.calcularQuantidadeFilhos(); indiceFilho++) {
        IndividuoBRKGA paiElite = elite.get(geradorAleatorio.nextInt(elite.size()));
        IndividuoBRKGA paiNaoElite = naoElite.get(geradorAleatorio.nextInt(naoElite.size()));
        novaPopulacao.add(crossoverEnviesado(paiElite, paiNaoElite));
    }

    // Reordena a populacao para que o melhor individuo fique na primeira posicao.
    novaPopulacao.sort(Comparator.naturalOrder());
    return novaPopulacao;
}
```

### Explicacao brutalmente clara

Essa funcao pega a populacao velha e fabrica a populacao nova.

O jeito mais facil de entender e pensar assim:

- a populacao atual ja esta ordenada do melhor para o pior
- o algoritmo nao quer jogar fora tudo
- mas tambem nao quer copiar tudo igual

Entao ele monta a nova populacao em 3 pedaços:

1. `elite`
2. `mutantes`
3. `filhos`

### Passo 1: separar a elite

```java
List<IndividuoBRKGA> elite = selecionarElite(populacaoAtual);
```

Aqui ele pega os melhores individuos da populacao atual.

Se a populacao tiver 10 individuos e a elite for 20%, ele pega os 2 melhores.

Entao:

- elite = melhores individuos
- eles sobrevivem direto para a proxima geracao

### Passo 2: separar quem nao e elite

```java
List<IndividuoBRKGA> naoElite = new ArrayList<>(
    populacaoAtual.subList(parametros.calcularQuantidadeElite(), populacaoAtual.size())
);
```

Aqui ele pega o resto da populacao.

Se a elite eram os 2 melhores de uma populacao com 10, entao `naoElite` vai ser os outros 8.

Esse grupo nao e lixo.
Ele ainda serve para participar do crossover.
So nao recebe o privilegio de ser copiado direto.

### Passo 3: criar a nova populacao vazia

```java
List<IndividuoBRKGA> novaPopulacao = new ArrayList<>();
```

Aqui o algoritmo prepara a "caixa" onde vai montar a proxima geracao.

### Passo 4: copiar a elite direto

```java
novaPopulacao.addAll(elite);
```

Aqui nao tem sorteio, nao tem mistura, nao tem mudanca.

Os melhores individuos entram do jeito que ja estavam.

Traduzindo:

- "esses caras ja provaram que sao bons"
- "entao eu nao vou perder eles"

### Passo 5: adicionar mutantes

```java
novaPopulacao.addAll(gerarMutantes());
```

Agora o algoritmo coloca individuos novos e aleatorios.

Por que ele faz isso?

- para nao deixar a populacao toda parecida
- para testar caminhos novos
- para evitar ficar preso sempre nas mesmas combinacoes

### Passo 6: completar com filhos

```java
for (int indiceFilho = 0; indiceFilho < parametros.calcularQuantidadeFilhos(); indiceFilho++) {
    IndividuoBRKGA paiElite = elite.get(geradorAleatorio.nextInt(elite.size()));
    IndividuoBRKGA paiNaoElite = naoElite.get(geradorAleatorio.nextInt(naoElite.size()));
    novaPopulacao.add(crossoverEnviesado(paiElite, paiNaoElite));
}
```

Aqui esta a parte que costuma confundir.

O algoritmo faz assim:

- escolhe 1 pai aleatorio dentro da elite
- escolhe 1 pai aleatorio dentro dos nao-elite
- cruza os dois
- gera 1 filho
- repete isso varias vezes

Repara numa coisa importante:

- o pai elite nao e sempre o melhor de todos
- ele e um sorteado dentre os melhores
- o pai nao-elite tambem e sorteado dentre o resto

Entao o algoritmo mistura:

- qualidade, vindo da elite
- diversidade, vindo do nao-elite

### Exemplo concreto

Imagina:

- tamanho da populacao = 10
- elite = 2 individuos
- mutantes = 2 individuos

Entao faltam:

- `10 - 2 - 2 = 6` individuos

Esses 6 que faltam serao filhos.

No final, a nova populacao fica assim:

- 2 individuos elite copiados
- 2 mutantes aleatorios
- 6 filhos gerados por crossover

Total:

- 10 individuos de novo

Ou seja, a populacao nao cresce e nao diminui.
Ela so e reconstruida.

### Passo 7: ordenar a nova populacao

```java
novaPopulacao.sort(Comparator.naturalOrder());
return novaPopulacao;
```

Depois que a nova populacao esta pronta, o algoritmo ordena tudo de novo do melhor para o pior.

Isso e importante porque na proxima geracao ele vai precisar:

- saber quem e elite
- saber quem e o melhor individuo

### Resumindo sem enrolacao

Essa funcao faz exatamente isto:

- salva os melhores
- injeta individuos aleatorios
- cria filhos misturando elite com nao-elite
- junta tudo
- ordena de novo

Se voce quiser uma frase bem simples, pode pensar assim:

> `atualizarPopulacao` e a funcao que desmonta a geracao antiga e monta a proxima, mantendo os melhores, criando novidade e produzindo novos filhos.

## 10. `recuperarMelhorSolucao`

### O que essa funcao faz

Pega o melhor individuo da populacao.

### Codigo

```java
public IndividuoBRKGA recuperarMelhorSolucao(List<IndividuoBRKGA> populacao) {
    // Como a populacao esta ordenada, o melhor individuo e sempre o primeiro.
    return populacao.get(0);
}
```

### Explicacao brutalmente clara

Como a populacao sempre e ordenada do menor fitness para o maior:

- o melhor esta na frente
- entao nao precisa procurar na lista inteira

Isso simplifica muito o controle do algoritmo.

## 11. `executar`

### O que essa funcao faz

Controla o processo completo do BRKGA do inicio ao fim.

### Codigo

```java
public ResultadoBRKGA executar() {
    long instanteInicial = System.currentTimeMillis();

    // Gera a primeira populacao e guarda a melhor solucao conhecida.
    // A populacao inicial ja pode conter uma solucao muito boa.
    List<IndividuoBRKGA> populacao = gerarPopulacaoInicial();
    IndividuoBRKGA melhorIndividuo = populacao.get(0);
    int melhorGeracao = 0;

    // Em cada geracao, criamos uma nova populacao com base na anterior.
    for (int geracao = 1; geracao <= parametros.getQuantidadeGeracoes(); geracao++) {
        populacao = atualizarPopulacao(populacao);

        // Como a populacao fica ordenada, a melhor solucao sempre esta na primeira posicao.
        IndividuoBRKGA melhorDaGeracao = recuperarMelhorSolucao(populacao);
        if (melhorDaGeracao.getFitness() < melhorIndividuo.getFitness()) {
            melhorIndividuo = melhorDaGeracao;
            melhorGeracao = geracao;
        }
    }

    // Ao final, devolvemos a melhor resposta encontrada em toda a busca.
    long tempoExecucao = System.currentTimeMillis() - instanteInicial;
    return new ResultadoBRKGA(
        melhorIndividuo.getSolucao(),
        melhorGeracao,
        parametros.getQuantidadeGeracoes(),
        tempoExecucao
    );
}
```

### Explicacao brutalmente clara

Essa funcao e o maestro da orquestra.

Ela faz:

1. cria a populacao inicial
2. guarda a melhor solucao atual
3. repete o processo de evolucao por varias geracoes
4. observa se apareceu alguem melhor
5. no fim, devolve a melhor resposta encontrada

Entao o fluxo geral do algoritmo e:

```text
cria populacao inicial
-> avalia
-> separa elite
-> cria mutantes
-> faz crossover
-> monta nova populacao
-> repete
-> devolve a melhor solucao
```

## 12. Resumo em portugues direto

Se voce precisar explicar o BRKGA em 30 segundos, pode falar assim:

> O algoritmo cria varias solucoes aleatorias em forma de random keys.
> Essas chaves sao ordenadas para virar uma rota valida.
> Cada rota recebe um custo.
> A cada geracao, os melhores individuos sao preservados, novos mutantes sao criados, e filhos sao gerados misturando elite com nao-elite, com vies para a elite.
> Depois a populacao e reordenada.
> Repetindo isso varias vezes, o algoritmo vai tentando melhorar a melhor rota encontrada.

## 13. Frases prontas para apresentacao

Se quiser falar de forma bem clara durante a apresentacao, aqui vao frases prontas:

- `criarAleatorio`: "Aqui o algoritmo so cria um candidato novo, preenchendo o cromossomo com numeros aleatorios."
- `decodificarCromossomo`: "Aqui os numeros aleatorios deixam de ser so numeros e passam a definir a ordem das cidades."
- `calcularFitness`: "Aqui a rota recebe uma nota. Quanto menor o custo total, melhor a solucao."
- `selecionarElite`: "Aqui o algoritmo protege os melhores individuos para nao perder qualidade."
- `gerarMutantes`: "Aqui ele injeta diversidade para nao ficar preso sempre nas mesmas ideias."
- `crossoverEnviesado`: "Aqui nasce um filho misturando dois pais, mas com maior chance de copiar os genes do pai elite."
- `atualizarPopulacao`: "Aqui a nova geracao e montada com elite, mutantes e filhos."
- `executar`: "Aqui fica o laco principal que repete a evolucao ate o fim."
