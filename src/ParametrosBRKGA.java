// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
public class ParametrosBRKGA {

    // Valores padrao escolhidos para manter a execucao simples e funcional.
    private static final int TAMANHO_POPULACAO_PADRAO = 80;
    private static final int QUANTIDADE_GERACOES_PADRAO = 300;
    private static final double PERCENTUAL_ELITE_PADRAO = 0.20;
    private static final double PERCENTUAL_MUTANTES_PADRAO = 0.15;
    private static final double PROBABILIDADE_HERANCA_ELITE_PADRAO = 0.70;
    private static final long SEMENTE_PADRAO = 12345L;

    private final int tamanhoPopulacao;
    private final int quantidadeGeracoes;
    private final double percentualElite;
    private final double percentualMutantes;
    private final double probabilidadeHerancaElite;
    private final long semente;

    public ParametrosBRKGA(
        int tamanhoPopulacao,
        int quantidadeGeracoes,
        double percentualElite,
        double percentualMutantes,
        double probabilidadeHerancaElite,
        long semente
    ) {
        validarParametros(
            tamanhoPopulacao,
            quantidadeGeracoes,
            percentualElite,
            percentualMutantes,
            probabilidadeHerancaElite
        );

        this.tamanhoPopulacao = tamanhoPopulacao;
        this.quantidadeGeracoes = quantidadeGeracoes;
        this.percentualElite = percentualElite;
        this.percentualMutantes = percentualMutantes;
        this.probabilidadeHerancaElite = probabilidadeHerancaElite;
        this.semente = semente;
    }

    public static ParametrosBRKGA criarAPartirDosArgumentos(String[] argumentos) {
        // Se algum parametro opcional nao for informado, usa-se o valor padrao.
        // Isso deixa o programa facil de rodar, mas ainda configuravel.
        int tamanhoPopulacao = lerInteiro(argumentos, 1, TAMANHO_POPULACAO_PADRAO);
        int quantidadeGeracoes = lerInteiro(argumentos, 2, QUANTIDADE_GERACOES_PADRAO);
        double percentualElite = lerDouble(argumentos, 3, PERCENTUAL_ELITE_PADRAO);
        double percentualMutantes = lerDouble(argumentos, 4, PERCENTUAL_MUTANTES_PADRAO);
        double probabilidadeHerancaElite = lerDouble(argumentos, 5, PROBABILIDADE_HERANCA_ELITE_PADRAO);
        long semente = lerLong(argumentos, 6, SEMENTE_PADRAO);

        return new ParametrosBRKGA(
            tamanhoPopulacao,
            quantidadeGeracoes,
            percentualElite,
            percentualMutantes,
            probabilidadeHerancaElite,
            semente
        );
    }

    private static int lerInteiro(String[] argumentos, int indice, int valorPadrao) {
        if (argumentos.length <= indice) {
            return valorPadrao;
        }

        return Integer.parseInt(argumentos[indice]);
    }

    private static long lerLong(String[] argumentos, int indice, long valorPadrao) {
        if (argumentos.length <= indice) {
            return valorPadrao;
        }

        return Long.parseLong(argumentos[indice]);
    }

    private static double lerDouble(String[] argumentos, int indice, double valorPadrao) {
        if (argumentos.length <= indice) {
            return valorPadrao;
        }

        return Double.parseDouble(argumentos[indice]);
    }

    private void validarParametros(
        int tamanhoPopulacao,
        int quantidadeGeracoes,
        double percentualElite,
        double percentualMutantes,
        double probabilidadeHerancaElite
    ) {
        if (tamanhoPopulacao < 3) {
            throw new IllegalArgumentException("O tamanho da populacao deve ser pelo menos 3.");
        }

        if (quantidadeGeracoes < 1) {
            throw new IllegalArgumentException("A quantidade de geracoes deve ser maior que zero.");
        }

        if (percentualElite <= 0.0 || percentualElite >= 1.0) {
            throw new IllegalArgumentException("O percentual de elite deve estar entre 0 e 1.");
        }

        if (percentualMutantes <= 0.0 || percentualMutantes >= 1.0) {
            throw new IllegalArgumentException("O percentual de mutantes deve estar entre 0 e 1.");
        }

        if (percentualElite + percentualMutantes >= 1.0) {
            throw new IllegalArgumentException("A soma entre elite e mutantes deve ser menor que 1.");
        }

        if (probabilidadeHerancaElite <= 0.0 || probabilidadeHerancaElite >= 1.0) {
            throw new IllegalArgumentException("A probabilidade de heranca do pai elite deve estar entre 0 e 1.");
        }
    }

    public int calcularQuantidadeElite() {
        // A elite representa os melhores individuos da populacao atual.
        // Pelo menos 1 individuo deve fazer parte da elite.
        int quantidadeElite = Math.max(1, (int) Math.round(tamanhoPopulacao * percentualElite));
        return Math.min(quantidadeElite, tamanhoPopulacao - 2);
    }

    public int calcularQuantidadeMutantes() {
        // Os mutantes sao individuos totalmente novos, criados aleatoriamente.
        // Eles ajudam a manter diversidade na busca.
        int quantidadeMutantes = Math.max(1, (int) Math.round(tamanhoPopulacao * percentualMutantes));
        int quantidadeMaxima = tamanhoPopulacao - calcularQuantidadeElite() - 1;
        return Math.min(quantidadeMutantes, quantidadeMaxima);
    }

    public int calcularQuantidadeFilhos() {
        // A nova populacao e composta por elite, mutantes e filhos.
        // Portanto, a quantidade de filhos e o que sobra apos reservar elite e mutantes.
        return tamanhoPopulacao - calcularQuantidadeElite() - calcularQuantidadeMutantes();
    }

    public int getTamanhoPopulacao() {
        return tamanhoPopulacao;
    }

    public int getQuantidadeGeracoes() {
        return quantidadeGeracoes;
    }

    public double getPercentualElite() {
        return percentualElite;
    }

    public double getPercentualMutantes() {
        return percentualMutantes;
    }

    public double getProbabilidadeHerancaElite() {
        return probabilidadeHerancaElite;
    }

    public long getSemente() {
        return semente;
    }
}
