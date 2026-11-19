package dominio;

// [HERANÇA] Caminhonete "é um" Veiculo
public class Caminhonete extends Veiculo {

    public static final double VALOR_HORA = 18.00;

    // [MÉTODO CONSTRUTOR]
    public Caminhonete(String placa, String marca, String modelo) {
        super(placa, marca, modelo);
    }

    // [POLIMORFISMO] cálculo próprio da Caminhonete (tarifa mais cara)
    @Override
    public double calcularValor() {
        return getPermanencia() * VALOR_HORA;
    }

    @Override
    public String getTipo() {
        return "Caminhonete";
    }

    @Override
    public double getValorHora() {
        return VALOR_HORA;
    }
}
