// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BRKGAPCVS {

    // Dados do problema que sera resolvido.
    private final InstanciaPCVS instancia;

    // Parametros configuraveis da BRKGA.
    private final ParametrosBRKGA parametros;

    // Gerador aleatorio usado em toda a execucao.
    private final Random geradorAleatorio;

    // Vetor com as cidades que realmente participam do cromossomo.
    // A cidade 1 nao aparece aqui porque ela e fixa na rota.
    private final int[] cidadesInternas;

    public BRKGAPCVS(InstanciaPCVS instancia, ParametrosBRKGA parametros) {
        this.instancia = instancia;
        this.parametros = parametros;
        this.geradorAleatorio = new Random(parametros.getSemente());
        this.cidadesInternas = instancia.construirCidadesInternas();
    }

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

    public double calcularFitness(int[] rota) {
        // O custo total e a soma das distancias entre cidades consecutivas da rota.
        double custoTotal = 0.0;

        for (int indice = 0; indice < rota.length - 1; indice++) {
            custoTotal += instancia.getDistancia(rota[indice], rota[indice + 1]);
        }

        return custoTotal;
    }

    public List<IndividuoBRKGA> selecionarElite(List<IndividuoBRKGA> populacao) {
        // A elite e formada pelos melhores individuos da populacao atual.
        int quantidadeElite = parametros.calcularQuantidadeElite();
        return new ArrayList<>(populacao.subList(0, quantidadeElite));
    }

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

    public IndividuoBRKGA crossoverEnviesado(IndividuoBRKGA paiElite, IndividuoBRKGA paiNaoElite) {
        // Cada gene do filho tem maior chance de ser herdado do pai elite.
        // Esse e o "viés" principal da BRKGA.
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

    public IndividuoBRKGA recuperarMelhorSolucao(List<IndividuoBRKGA> populacao) {
        // Como a populacao esta ordenada, o melhor individuo e sempre o primeiro.
        return populacao.get(0);
    }

    private void avaliarIndividuo(IndividuoBRKGA individuo) {
        // A avaliacao transforma o cromossomo em rota e calcula seu custo.
        SolucaoPCVS solucao = decodificarCromossomo(individuo.getCromossomoCopia());
        individuo.setSolucao(solucao);
    }

    private static class CidadeChave {

        // Guarda a cidade e sua random key para facilitar a ordenacao.
        private final int cidade;
        private final double chaveAleatoria;

        private CidadeChave(int cidade, double chaveAleatoria) {
            this.cidade = cidade;
            this.chaveAleatoria = chaveAleatoria;
        }

        public int cidade() {
            return cidade;
        }

        public double chaveAleatoria() {
            return chaveAleatoria;
        }
    }
}
