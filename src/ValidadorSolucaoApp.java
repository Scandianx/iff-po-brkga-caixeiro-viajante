// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class ValidadorSolucaoApp {

    private static final String CAMINHO_INSTANCIA = "examples/pcvs_exemplo.txt";
    private static final String CAMINHO_SOLUCAO = "examples/solucao.txt";
    private static final double TOLERANCIA_CUSTO = 0.000001;

    public static void main(String[] argumentos) {
        try {
            InstanciaPCVS instancia = LeitorInstancia.lerArquivo(CAMINHO_INSTANCIA);
            SolucaoInformada solucao = lerSolucao(CAMINHO_SOLUCAO);

            validarInicioEFimNaCidadeUm(solucao.rota());
            validarEstruturaDaRota(instancia, solucao.rota());
            double custoCalculado = calcularCusto(instancia, solucao.rota());
            validarCusto(solucao.custoInformado(), custoCalculado);

            System.out.println("Solucao valida.");
            System.out.println("Custo informado: " + solucao.custoInformado());
            System.out.println("Custo calculado: " + custoCalculado);
        } catch (IllegalArgumentException | IOException excecao) {
            System.err.println("Solucao invalida: " + excecao.getMessage());
        }
    }

    private static SolucaoInformada lerSolucao(String caminhoSolucao) throws IOException {
        Path caminho = Path.of(caminhoSolucao);
        if (!Files.exists(caminho)) {
            throw new IllegalArgumentException("O arquivo de solucao nao foi encontrado em " + caminhoSolucao + ".");
        }

        String conteudo = Files.readString(caminho).trim();
        if (conteudo.isEmpty()) {
            throw new IllegalArgumentException("O arquivo de solucao esta vazio.");
        }

        String[] linhas = conteudo.split("\\R");
        if (linhas.length < 2) {
            throw new IllegalArgumentException("O arquivo de solucao deve ter duas linhas: custo e rota.");
        }

        double custoInformado = Double.parseDouble(linhas[0].trim());
        String[] partesRota = linhas[1].trim().split("\\s+");
        if (partesRota.length < 2) {
            throw new IllegalArgumentException("A rota informada e invalida.");
        }

        int[] rota = new int[partesRota.length];
        for (int indice = 0; indice < partesRota.length; indice++) {
            rota[indice] = Integer.parseInt(partesRota[indice]);
        }

        return new SolucaoInformada(custoInformado, rota);
    }

    private static void validarInicioEFimNaCidadeUm(int[] rota) {
        if (rota[0] != 1) {
            throw new IllegalArgumentException("A rota nao comeca na cidade 1.");
        }

        if (rota[rota.length - 1] != 1) {
            throw new IllegalArgumentException("A rota nao termina na cidade 1.");
        }
    }

    private static double calcularCusto(InstanciaPCVS instancia, int[] rota) {
        double custoTotal = 0.0;

        for (int indice = 0; indice < rota.length - 1; indice++) {
            custoTotal += instancia.getDistancia(rota[indice], rota[indice + 1]);
        }

        return custoTotal;
    }

    private static void validarEstruturaDaRota(InstanciaPCVS instancia, int[] rota) {
        int tamanhoEsperado = instancia.getNumeroVertices() + 1;
        if (rota.length != tamanhoEsperado) {
            throw new IllegalArgumentException(
                "A rota deve ter tamanho " + tamanhoEsperado + ", mas foi informada com tamanho " + rota.length + "."
            );
        }

        Set<Integer> cidadesVisitadas = new HashSet<>();
        for (int indice = 1; indice < rota.length - 1; indice++) {
            int cidade = rota[indice];

            if (cidade < 2 || cidade > instancia.getNumeroVertices()) {
                throw new IllegalArgumentException("A rota contem cidade invalida: " + cidade + ".");
            }

            cidadesVisitadas.add(cidade);
        }

        for (int cidade = 2; cidade <= instancia.getNumeroVertices(); cidade++) {
            if (!cidadesVisitadas.contains(cidade)) {
                throw new IllegalArgumentException("A cidade " + cidade + " nao aparece na rota.");
            }
        }
    }

    private static void validarCusto(double custoInformado, double custoCalculado) {
        if (Math.abs(custoInformado - custoCalculado) > TOLERANCIA_CUSTO) {
            throw new IllegalArgumentException(
                "O custo informado (" + custoInformado + ") nao bate com o custo calculado (" + custoCalculado + ")."
            );
        }
    }

    private record SolucaoInformada(double custoInformado, int[] rota) {
    }
}
