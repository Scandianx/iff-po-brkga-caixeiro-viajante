// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.util.Arrays;
import java.util.Random;

public class IndividuoBRKGA implements Comparable<IndividuoBRKGA> {

    // O cromossomo e formado por random keys.
    // Cada posicao esta associada a uma cidade interna do problema.
    private final double[] cromossomo;

    // A solucao decodificada guarda a rota e o custo total desse individuo.
    private SolucaoPCVS solucao;

    public IndividuoBRKGA(double[] cromossomo) {
        this.cromossomo = cromossomo.clone();
    }

    public static IndividuoBRKGA criarAleatorio(int tamanhoCromossomo, Random geradorAleatorio) {
        // Cada gene e uma random key no intervalo [0, 1).
        // Depois essas chaves serao ordenadas para montar a rota.
        double[] cromossomo = new double[tamanhoCromossomo];

        for (int indiceGene = 0; indiceGene < tamanhoCromossomo; indiceGene++) {
            cromossomo[indiceGene] = geradorAleatorio.nextDouble();
        }

        return new IndividuoBRKGA(cromossomo);
    }

    public int getTamanhoCromossomo() {
        return cromossomo.length;
    }

    public double getGene(int indice) {
        return cromossomo[indice];
    }

    public double[] getCromossomoCopia() {
        return cromossomo.clone();
    }

    public void setSolucao(SolucaoPCVS solucao) {
        this.solucao = solucao;
    }

    public SolucaoPCVS getSolucao() {
        return solucao;
    }

    public double getFitness() {
        if (solucao == null) {
            throw new IllegalStateException("O individuo precisa ser avaliado antes de consultar o fitness.");
        }

        // Neste problema, o fitness e exatamente o custo total da rota.
        // Como queremos minimizar, menor fitness significa melhor individuo.
        return solucao.getCustoTotal();
    }

    @Override
    public int compareTo(IndividuoBRKGA outroIndividuo) {
        // Permite ordenar individuos do menor custo para o maior custo.
        return Double.compare(this.getFitness(), outroIndividuo.getFitness());
    }

    @Override
    public String toString() {
        return "IndividuoBRKGA{fitness=" + getFitness() + ", cromossomo=" + Arrays.toString(cromossomo) + "}";
    }
}
