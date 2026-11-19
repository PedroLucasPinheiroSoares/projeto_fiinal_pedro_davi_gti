package dominio;

import util.Formatador;

// Vaga do estacionamento: recebe um veículo por vez
public class Vaga {

    // [ATRIBUTOS - snake_case] 
    // [ENCAPSULAMENTO] private: só a própria Vaga mexe nestes dados, os outros usam os métodos abaixo
    private int numero;
    private StatusVaga status;
    private Veiculo veiculo;

    // [MÉTODO CONSTRUTOR] toda vaga já nasce LIVRE e sem veículo
    public Vaga(int numero) {
        this.numero = numero;
        this.status = StatusVaga.LIVRE;
        this.veiculo = null;
    }

    // [GETTERS]
    public int getNumero() {
        return this.numero;
    }

    public StatusVaga getStatus() {
        return this.status;
    }

    public Veiculo getVeiculo() {
        return this.veiculo;
    }

    // Pergunta simples: a vaga está livre?  (true ou false)
    public boolean isDisponivel() {
        return this.status == StatusVaga.LIVRE;
    }

    // Ocupa a vaga. Retorna false se ela já estava ocupada (regra: 1 veículo por vez)
    public boolean ocupar(Veiculo veiculo) {
        if (!isDisponivel()) {
            return false;
        }
        this.veiculo = veiculo;
        this.status = StatusVaga.OCUPADA;
        return true;
    }

    // Libera a vaga: volta a ficar LIVRE e sem veículo
    public void liberar() {
        this.veiculo = null;
        this.status = StatusVaga.LIVRE;
    }

    // Texto usado ao imprimir a vaga
    @Override
    public String toString() {
        String texto = "Vaga " + Formatador.numeroVaga(this.numero) + " - " + this.status;
        if (this.veiculo != null) {
            texto = texto + " - " + this.veiculo.getTipo() + " " + this.veiculo.getPlaca();
        }
        return texto;
    }
}
