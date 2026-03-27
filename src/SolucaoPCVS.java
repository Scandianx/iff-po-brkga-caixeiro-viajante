// Filipe Scandiani Soave, Igor Almenara, Raphael Castelar
import java.util.StringJoiner;

public class SolucaoPCVS {

    // A rota ja vem completa, com a cidade 1 no inicio e no final.
    private final int[] rota;

    // Custo total do ciclo representado pela rota.
    private final double custoTotal;

    public SolucaoPCVS(int[] rota, double custoTotal) {
        this.rota = rota.clone();
        this.custoTotal = custoTotal;
    }

    public int[] getRota() {
        return rota.clone();
    }

    public double getCustoTotal() {
        return custoTotal;
    }

    public String formatarRota() {
        // Monta uma string amigavel para apresentacao da rota final.
        StringJoiner juntador = new StringJoiner(" -> ");

        for (int cidade : rota) {
            juntador.add(String.valueOf(cidade));
        }

        return juntador.toString();
    }
}
