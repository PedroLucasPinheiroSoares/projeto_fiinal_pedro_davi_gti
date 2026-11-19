package dominio;

import java.time.Duration;
import java.time.LocalDateTime;

import util.Formatador;

// [CLASSE ABSTRATA] igual Animal e Pessoa das aulas de herança: não dá para fazer "new Veiculo()".
// Só existem Carro, Moto e Caminhonete (as classes filhas).
public abstract class Veiculo {

    // [ATRIBUTOS - snake_case] 
    // [PROTECTED] as filhas (Carro, Moto, Caminhonete) enxergam, como o "nome" da Pessoa.
    protected String placa;
    protected String marca;
    protected String modelo;
    protected LocalDateTime entrada;   // [NOVO - ainda não visto] LocalDateTime guarda data + hora
    protected LocalDateTime saida;
    protected Vaga vaga;               // [RELACIONAMENTO] o veículo sabe em qual vaga está

    // [MÉTODO CONSTRUTOR] mesmo nome da classe, sem tipo de retorno
    public Veiculo(String placa, String marca, String modelo) {
        this.placa = Formatador.normalizarPlaca(placa);
        this.marca = marca;
        this.modelo = modelo;
    }

    // [MÉTODOS ABSTRATOS] sem corpo: cada filha é OBRIGADA a escrever o seu (igual o som() do Animal)
    // [POLIMORFISMO] o mesmo método calcularValor() dá um resultado diferente em cada tipo de veículo
    public abstract double calcularValor();
    public abstract String getTipo();
    public abstract double getValorHora();

    // ---------------- [GETTERS E SETTERS] ----------------
    // [ENCAPSULAMENTO] os atributos ficam protegidos e o acesso é feito por estes métodos

    // [GET] só lê o valor
    public String getPlaca() {
        return this.placa;
    }

    public String getMarca() {
        return this.marca;
    }

    public String getModelo() {
        return this.modelo;
    }

    public LocalDateTime getEntrada() {
        return this.entrada;
    }

    // [SET] altera o valor
    public void setEntrada(LocalDateTime entrada) {
        this.entrada = entrada;
    }

    public LocalDateTime getSaida() {
        return this.saida;
    }

    public void setSaida(LocalDateTime saida) {
        this.saida = saida;
    }

    public Vaga getVaga() {
        return this.vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    // ---------------- PERMANÊNCIA ----------------

    // Tempo real que o veículo ficou, em minutos
    public long getPermanenciaMinutos() {
        if (this.entrada == null || this.saida == null) {
            return 0;
        }
        return Duration.between(this.entrada, this.saida).toMinutes();
    }

    // Horas COBRADAS: qualquer fração de hora conta como hora completa (mínimo 1 hora)
    public long getPermanencia() {
        if (this.entrada == null || this.saida == null) {
            return 0;
        }
        long segundos = Duration.between(this.entrada, this.saida).getSeconds();
        long horas = segundos / 3600;
        if (segundos % 3600 > 0) {
            horas = horas + 1;
        }
        if (horas < 1) {
            horas = 1;
        }
        return horas;
    }

    // [NOVO - ainda não visto] @Override: avisa que estamos reescrevendo um método que já existe
    // (todo objeto Java já tem toString; aqui trocamos o texto padrão por um nosso)
    @Override
    public String toString() {
        String numero_vaga = "--";
        if (this.vaga != null) {
            numero_vaga = Formatador.numeroVaga(this.vaga.getNumero());
        }
        return "[" + getTipo() + "] Placa: " + this.placa
                + " | " + this.marca + " " + this.modelo
                + " | Entrada: " + Formatador.hora(this.entrada)
                + " | Vaga: " + numero_vaga;
    }
}
