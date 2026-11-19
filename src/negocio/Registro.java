package negocio;

import java.time.LocalDateTime;

import dominio.Veiculo;
import util.Formatador;

// Registro = uma "linha" do histórico: uma passagem de um veículo pelo estacionamento
public class Registro {

    // [ATRIBUTOS - snake_case] [ENCAPSULAMENTO: private]
    private Veiculo veiculo;
    private int vaga_numero;
    private LocalDateTime entrada;
    private LocalDateTime saida;
    private double valor_pago;

    // [MÉTODO CONSTRUTOR] o registro nasce na ENTRADA, ainda sem saída e sem valor pago
    public Registro(Veiculo veiculo, int vaga_numero, LocalDateTime entrada) {
        this.veiculo = veiculo;
        this.vaga_numero = vaga_numero;
        this.entrada = entrada;
        this.saida = null;
        this.valor_pago = 0.0;
    }

    // Quando o veículo sai, o registro é "fechado" com horário e valor
    public void registrarSaida(LocalDateTime saida, double valor_pago) {
        this.saida = saida;
        this.valor_pago = valor_pago;
    }

    // Se não tem saída, o veículo ainda está no estacionamento
    public boolean isEmAberto() {
        return this.saida == null;
    }

    // [GETTERS]
    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    public int getVagaNumero() {
        return this.vaga_numero;
    }

    public LocalDateTime getEntrada() {
        return this.entrada;
    }

    public LocalDateTime getSaida() {
        return this.saida;
    }

    public double getValorPago() {
        return this.valor_pago;
    }

    public String getPlaca() {
        return this.veiculo.getPlaca();
    }

    @Override
    public String toString() {
        String situacao = "ainda estacionado";
        if (!isEmAberto()) {
            situacao = "saída " + Formatador.hora(this.saida) + " | pago " + Formatador.moeda(this.valor_pago);
        }
        return this.veiculo.getPlaca() + " | " + this.veiculo.getTipo()
                + " | " + this.veiculo.getMarca() + " " + this.veiculo.getModelo()
                + " | Vaga " + Formatador.numeroVaga(this.vaga_numero)
                + " | entrada " + Formatador.hora(this.entrada)
                + " | " + situacao;
    }
}
