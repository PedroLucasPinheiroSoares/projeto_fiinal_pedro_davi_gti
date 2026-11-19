package dominio;

// [HERANÇA] Moto "é um" Veiculo
public class Moto extends Veiculo {

    public static final double VALOR_HORA = 7.00;

    // [MÉTODO CONSTRUTOR]
    public Moto(String placa, String marca, String modelo) {
        super(placa, marca, modelo);
    }

    // [POLIMORFISMO] cálculo próprio da Moto (tarifa mais barata)
    @Override
    public double calcularValor() {
        return getPermanencia() * VALOR_HORA;
    }

    @Override
    public String getTipo() {
        return "Moto";
    }

    @Override
    public double getValorHora() {
        return VALOR_HORA;
    }
}
