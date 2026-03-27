// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LeitorInstancia {

    public static InstanciaPCVS lerArquivo(String caminhoArquivo) throws IOException {
        Path caminho = Path.of(caminhoArquivo);

        try (BufferedReader leitor = Files.newBufferedReader(caminho)) {
            // A primeira linha deve informar vertices e arestas.
            // Linhas vazias sao ignoradas para deixar a leitura mais robusta.
            String primeiraLinha = lerProximaLinhaNaoVazia(leitor);

            if (primeiraLinha == null) {
                throw new IllegalArgumentException("O arquivo de entrada esta vazio.");
            }

            String[] cabecalho = quebrarLinha(primeiraLinha);
            if (cabecalho.length != 2) {
                throw new IllegalArgumentException("A primeira linha deve conter numero de vertices e numero de arestas.");
            }

            int numeroVertices = Integer.parseInt(cabecalho[0]);
            int numeroArestas = Integer.parseInt(cabecalho[1]);
            double[][] matrizDistancias = new double[numeroVertices][numeroVertices];

            // As proximas linhas correspondem a matriz completa de distancias.
            // Cada linha da matriz representa as distancias de uma cidade para todas as outras.
            for (int linha = 0; linha < numeroVertices; linha++) {
                String linhaMatriz = lerProximaLinhaNaoVazia(leitor);

                if (linhaMatriz == null) {
                    throw new IllegalArgumentException("A matriz de distancias esta incompleta.");
                }

                String[] valoresLinha = quebrarLinha(linhaMatriz);
                if (valoresLinha.length != numeroVertices) {
                    throw new IllegalArgumentException(
                        "Cada linha da matriz deve conter exatamente " + numeroVertices + " valores."
                    );
                }

                for (int coluna = 0; coluna < numeroVertices; coluna++) {
                    matrizDistancias[linha][coluna] = Double.parseDouble(valoresLinha[coluna]);
                }
            }

            String linhaExtra = lerProximaLinhaNaoVazia(leitor);
            if (linhaExtra != null) {
                // Se ainda houver conteudo relevante no arquivo, a entrada foi montada incorretamente.
                throw new IllegalArgumentException("O arquivo possui linhas extras apos a matriz de distancias.");
            }

            return new InstanciaPCVS(numeroVertices, numeroArestas, matrizDistancias);
        }
    }

    private static String lerProximaLinhaNaoVazia(BufferedReader leitor) throws IOException {
        // Le linhas ate encontrar uma linha com conteudo util.
        String linhaAtual;

        while ((linhaAtual = leitor.readLine()) != null) {
            if (!linhaAtual.trim().isEmpty()) {
                return linhaAtual.trim();
            }
        }

        return null;
    }

    private static String[] quebrarLinha(String linha) {
        // Aceita um ou mais espacos entre os numeros do arquivo.
        return linha.trim().split("\\s+");
    }
}
