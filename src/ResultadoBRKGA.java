// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
public class ResultadoBRKGA {

    // Melhor solucao encontrada em toda a execucao.
    private final SolucaoPCVS melhorSolucao;

    // Geracao em que a melhor solucao apareceu.
    // Quando vale 0, significa que ela apareceu na populacao inicial.
    private final int melhorGeracao;

    // Quantidade total de geracoes processadas pelo algoritmo.
    private final int geracoesExecutadas;

    // Tempo total gasto na execucao, em milissegundos.
    private final long tempoExecucaoMillis;

    public ResultadoBRKGA(
        SolucaoPCVS melhorSolucao,
        int melhorGeracao,
        int geracoesExecutadas,
        long tempoExecucaoMillis
    ) {
        this.melhorSolucao = melhorSolucao;
        this.melhorGeracao = melhorGeracao;
        this.geracoesExecutadas = geracoesExecutadas;
        this.tempoExecucaoMillis = tempoExecucaoMillis;
    }

    public SolucaoPCVS getMelhorSolucao() {
        return melhorSolucao;
    }

    public int getMelhorGeracao() {
        return melhorGeracao;
    }

    public int getGeracoesExecutadas() {
        return geracoesExecutadas;
    }

    public long getTempoExecucaoMillis() {
        return tempoExecucaoMillis;
    }
}
