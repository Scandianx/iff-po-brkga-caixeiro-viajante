// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class App {

    public static void main(String[] argumentos) {
        // O programa espera pelo menos o caminho do arquivo de entrada.
        // Se o usuario nao informar esse caminho, mostramos a forma correta de uso.
        if (argumentos.length < 1) {
            imprimirUso();
            return;
        }

        try {
            // O primeiro argumento sempre e o arquivo com a instancia do PCVS.
            String caminhoArquivo = argumentos[0];

            // Os parametros da BRKGA podem ser informados via linha de comando.
            // Caso nao sejam informados, o programa usa valores padrao.
            ParametrosBRKGA parametros = ParametrosBRKGA.criarAPartirDosArgumentos(argumentos);

            // Faz a leitura do arquivo e monta a representacao do problema.
            InstanciaPCVS instancia = LeitorInstancia.lerArquivo(caminhoArquivo);

            // Cria o objeto responsavel por executar a metaheuristica BRKGA.
            BRKGAPCVS algoritmo = new BRKGAPCVS(instancia, parametros);

            // Executa o processo evolutivo e devolve a melhor solucao encontrada.
            ResultadoBRKGA resultado = algoritmo.executar();

            // Exibe na tela um resumo organizado da execucao.
            imprimirResumoExecucao(caminhoArquivo, instancia, parametros, resultado);
        } catch (IllegalArgumentException | IOException excecao) {
            // Qualquer problema de leitura ou validacao da entrada e informado ao usuario.
            System.err.println("Erro: " + excecao.getMessage());
        }
    }

    private static void imprimirUso() {
        // Instrucoes basicas para executar o programa pelo terminal.
        System.out.println("Uso:");
        System.out.println("java -cp bin App <arquivo_entrada> [populacao] [geracoes] [elite] [mutantes] [heranca_elite] [semente]");
        System.out.println();
        System.out.println("Exemplo:");
        System.out.println("java -cp bin App examples/pcvs_exemplo.txt 80 300 0.20 0.15 0.70 12345");
    }

    private static void imprimirResumoExecucao(
        String caminhoArquivo,
        InstanciaPCVS instancia,
        ParametrosBRKGA parametros,
        ResultadoBRKGA resultado
    ) {
        // Configura o formato numerico para usar ponto como separador decimal.
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.US);
        DecimalFormat formatador = new DecimalFormat("0.###", simbolos);

        // A saida final mostra os dados da instancia, os parametros e a melhor resposta obtida.
        System.out.println("=== BRKGA para o Problema do Caixeiro Viajante Simetrico ===");
        System.out.println("Arquivo de entrada: " + caminhoArquivo);
        System.out.println("Numero de vertices: " + instancia.getNumeroVertices());
        System.out.println("Numero de arestas informado: " + instancia.getNumeroArestas());
        System.out.println("Tamanho da populacao: " + parametros.getTamanhoPopulacao());
        System.out.println("Quantidade de geracoes: " + parametros.getQuantidadeGeracoes());
        System.out.println("Percentual de elite: " + formatador.format(parametros.getPercentualElite()));
        System.out.println("Percentual de mutantes: " + formatador.format(parametros.getPercentualMutantes()));
        System.out.println("Probabilidade de herdar gene do pai elite: " + formatador.format(parametros.getProbabilidadeHerancaElite()));
        System.out.println("Semente aleatoria: " + parametros.getSemente());
        System.out.println();
        System.out.println("Melhor rota encontrada: " + resultado.getMelhorSolucao().formatarRota());
        System.out.println("Custo total: " + formatador.format(resultado.getMelhorSolucao().getCustoTotal()));
        System.out.println("Melhor geracao: " + resultado.getMelhorGeracao());
        System.out.println("Geracoes executadas: " + resultado.getGeracoesExecutadas());
        System.out.println("Tempo de execucao (ms): " + resultado.getTempoExecucaoMillis());
    }
}
