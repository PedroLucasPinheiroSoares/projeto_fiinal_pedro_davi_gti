package dominio;

// [HERANÇA] Carro "é um" Veiculo: ganha placa, marca, modelo, getters, etc. sem reescrever
public class Carro extends Veiculo {

    // [NOVO - ainda não visto] static final = constante (valor que nunca muda e é igual para todos os Carros)
    public static final double VALOR_HORA = 12.00;

    // [MÉTODO CONSTRUTOR]
    // [NOVO - ainda não visto] super(...) chama o construtor da classe mãe (Veiculo) para ela guardar os dados
    public Carro(String placa, String marca, String modelo) {
        super(placa, marca, modelo);
    }

    // [POLIMORFISMO] implementação do método abstrato: cálculo próprio do Carro
    @Override
    public double calcularValor() {
        return getPermanencia() * VALOR_HORA;
    }

    @Override
    public String getTipo() {
        return "Carro";
    }

    @Override
    public double getValorHora() {
        return VALOR_HORA;
    }
}
