// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
public class InstanciaPCVS {

    // Tolerancia usada para comparar valores de ponto flutuante.
    private static final double TOLERANCIA_SIMETRIA = 0.000001;

    private final int numeroVertices;
    private final int numeroArestas;
    private final double[][] matrizDistancias;

    public InstanciaPCVS(int numeroVertices, int numeroArestas, double[][] matrizDistancias) {
        // Antes de armazenar os dados, validamos se a instancia realmente representa
        // um PCVS simetrico em um grafo simples e completo.
        validarInstancia(numeroVertices, numeroArestas, matrizDistancias);

        this.numeroVertices = numeroVertices;
        this.numeroArestas = numeroArestas;
        this.matrizDistancias = copiarMatriz(matrizDistancias);
    }

    private void validarInstancia(int numeroVertices, int numeroArestas, double[][] matrizDistancias) {
        // O problema precisa de pelo menos duas cidades.
        if (numeroVertices < 2) {
            throw new IllegalArgumentException("A instancia deve possuir pelo menos 2 vertices.");
        }

        // Em um grafo completo simples e nao orientado com n vertices,
        // o numero de arestas deve ser n * (n - 1) / 2.
        int quantidadeEsperadaArestas = numeroVertices * (numeroVertices - 1) / 2;
        if (numeroArestas != quantidadeEsperadaArestas) {
            throw new IllegalArgumentException(
                "Para um grafo completo simples, o numero de arestas deve ser " + quantidadeEsperadaArestas + "."
            );
        }

        // A matriz precisa ter uma linha para cada cidade.
        if (matrizDistancias.length != numeroVertices) {
            throw new IllegalArgumentException("A matriz de distancias deve ter o mesmo numero de linhas dos vertices.");
        }

        for (int linha = 0; linha < numeroVertices; linha++) {
            // Cada linha tambem precisa ter uma coluna para cada cidade.
            if (matrizDistancias[linha].length != numeroVertices) {
                throw new IllegalArgumentException("A matriz de distancias deve ser quadrada.");
            }

            // A distancia de uma cidade para ela mesma deve ser zero.
            if (Math.abs(matrizDistancias[linha][linha]) > TOLERANCIA_SIMETRIA) {
                throw new IllegalArgumentException("A diagonal principal da matriz deve conter zeros.");
            }

            for (int coluna = linha + 1; coluna < numeroVertices; coluna++) {
                // Distancias negativas nao fazem sentido neste problema.
                if (matrizDistancias[linha][coluna] < 0.0) {
                    throw new IllegalArgumentException("As distancias nao podem ser negativas.");
                }

                // Como o problema e simetrico, dist(i, j) precisa ser igual a dist(j, i).
                if (Math.abs(matrizDistancias[linha][coluna] - matrizDistancias[coluna][linha]) > TOLERANCIA_SIMETRIA) {
                    throw new IllegalArgumentException("A matriz informada nao e simetrica.");
                }
            }
        }
    }

    private double[][] copiarMatriz(double[][] matrizOriginal) {
        // Fazemos uma copia da matriz para evitar alteracoes externas.
        double[][] copia = new double[matrizOriginal.length][];

        for (int linha = 0; linha < matrizOriginal.length; linha++) {
            copia[linha] = matrizOriginal[linha].clone();
        }

        return copia;
    }

    public int[] construirCidadesInternas() {
        // A cidade 1 e fixa, entao apenas as cidades 2 ate n entram no cromossomo.
        // Essas cidades sao as que realmente serao ordenadas pelas random keys.
        int[] cidadesInternas = new int[numeroVertices - 1];

        for (int indice = 0; indice < cidadesInternas.length; indice++) {
            cidadesInternas[indice] = indice + 2;
        }

        return cidadesInternas;
    }

    public double getDistancia(int cidadeOrigem, int cidadeDestino) {
        // As cidades sao numeradas a partir de 1 para o usuario.
        // Mas o vetor em Java comeca em 0, por isso subtraimos 1.
        return matrizDistancias[cidadeOrigem - 1][cidadeDestino - 1];
    }

    public int getNumeroVertices() {
        return numeroVertices;
    }

    public int getNumeroArestas() {
        return numeroArestas;
    }
}
