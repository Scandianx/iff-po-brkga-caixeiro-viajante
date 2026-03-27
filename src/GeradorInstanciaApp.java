// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class GeradorInstanciaApp {

    private static final int NUMERO_VERTICES_PADRAO = 100;
    private static final int DISTANCIA_MINIMA_PADRAO = 1;
    private static final int DISTANCIA_MAXIMA_PADRAO = 999;
    private static final long SEMENTE_PADRAO = 12345L;
    private static final String CAMINHO_SAIDA_PADRAO = "examples/pcvs_gerado.txt";

    public static void main(String[] argumentos) {
        try {
            int numeroVertices = lerInteiro(argumentos, 0, NUMERO_VERTICES_PADRAO);
            int distanciaMinima = lerInteiro(argumentos, 1, DISTANCIA_MINIMA_PADRAO);
            int distanciaMaxima = lerInteiro(argumentos, 2, DISTANCIA_MAXIMA_PADRAO);
            long semente = lerLong(argumentos, 3, SEMENTE_PADRAO);
            String caminhoSaida = lerString(argumentos, 4, CAMINHO_SAIDA_PADRAO);

            validarEntradas(numeroVertices, distanciaMinima, distanciaMaxima);

            double[][] matriz = gerarMatrizSimetrica(numeroVertices, distanciaMinima, distanciaMaxima, semente);
            salvarInstancia(numeroVertices, matriz, caminhoSaida);

            System.out.println("Instancia gerada com sucesso.");
            System.out.println("Arquivo: " + caminhoSaida);
            System.out.println("Numero de vertices: " + numeroVertices);
            System.out.println("Numero de arestas: " + calcularNumeroArestas(numeroVertices));
            System.out.println("Distancia minima: " + distanciaMinima);
            System.out.println("Distancia maxima: " + distanciaMaxima);
            System.out.println("Semente: " + semente);
        } catch (IllegalArgumentException | IOException excecao) {
            System.err.println("Erro ao gerar instancia: " + excecao.getMessage());
        }
    }

    private static double[][] gerarMatrizSimetrica(
        int numeroVertices,
        int distanciaMinima,
        int distanciaMaxima,
        long semente
    ) {
        Random geradorAleatorio = new Random(semente);
        double[][] matriz = new double[numeroVertices][numeroVertices];

        for (int linha = 0; linha < numeroVertices; linha++) {
            matriz[linha][linha] = 0.0;

            for (int coluna = linha + 1; coluna < numeroVertices; coluna++) {
                int distancia = geradorAleatorio.nextInt(distanciaMaxima - distanciaMinima + 1) + distanciaMinima;
                matriz[linha][coluna] = distancia;
                matriz[coluna][linha] = distancia;
            }
        }

        return matriz;
    }

    private static void salvarInstancia(int numeroVertices, double[][] matriz, String caminhoSaida) throws IOException {
        StringBuilder conteudo = new StringBuilder();
        conteudo.append(numeroVertices)
            .append(' ')
            .append(calcularNumeroArestas(numeroVertices))
            .append(System.lineSeparator());

        for (int linha = 0; linha < numeroVertices; linha++) {
            for (int coluna = 0; coluna < numeroVertices; coluna++) {
                if (coluna > 0) {
                    conteudo.append(' ');
                }

                conteudo.append((int) matriz[linha][coluna]);
            }

            if (linha < numeroVertices - 1) {
                conteudo.append(System.lineSeparator());
            }
        }

        Files.writeString(Path.of(caminhoSaida), conteudo.toString());
    }

    private static int calcularNumeroArestas(int numeroVertices) {
        return numeroVertices * (numeroVertices - 1) / 2;
    }

    private static void validarEntradas(int numeroVertices, int distanciaMinima, int distanciaMaxima) {
        if (numeroVertices < 2) {
            throw new IllegalArgumentException("O numero de vertices deve ser pelo menos 2.");
        }

        if (distanciaMinima < 1) {
            throw new IllegalArgumentException("A distancia minima deve ser pelo menos 1.");
        }

        if (distanciaMaxima < distanciaMinima) {
            throw new IllegalArgumentException("A distancia maxima deve ser maior ou igual a distancia minima.");
        }
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

    private static String lerString(String[] argumentos, int indice, String valorPadrao) {
        if (argumentos.length <= indice) {
            return valorPadrao;
        }

        return argumentos[indice];
    }
}
