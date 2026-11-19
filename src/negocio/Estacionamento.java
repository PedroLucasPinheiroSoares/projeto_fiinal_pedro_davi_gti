package negocio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dominio.Vaga;
import dominio.Veiculo;
import util.Formatador;

// Classe principal da camada de negócio: guarda as vagas e o histórico e aplica as regras do enunciado.
//
// Como os erros funcionam aqui: quando uma regra é violada o método devolve null
// e deixa o motivo em "mensagem_erro" (quem chamou lê com getMensagemErro()).
public class Estacionamento {

    // [ATRIBUTOS - snake_case] [ENCAPSULAMENTO: private]
    // [RELACIONAMENTO 1:N] um Estacionamento tem VÁRIAS Vagas e VÁRIOS Registros (igual List<Professor> da revisão)
    private List<Vaga> vagas = new ArrayList<>();
    private List<Registro> historico = new ArrayList<>();
    private String mensagem_erro = "";

    // [MÉTODO CONSTRUTOR] cria as vagas numeradas de 1 até a quantidade informada
    public Estacionamento(int quantidade_vagas) {
        int i = 1;
        while (i <= quantidade_vagas) {
            this.vagas.add(new Vaga(i));
            i++;
        }
    }

    // [GET] motivo do último erro
    public String getMensagemErro() {
        return this.mensagem_erro;
    }

    // [GET] quantidade total de vagas
    public int getTotalVagas() {
        return this.vagas.size();
    }

    // [GET] todas as vagas (livres e ocupadas)
    public List<Vaga> getVagas() {
        return this.vagas;
    }

    // ===================== ENTRADA =====================

    // [SOBRECARGA] mesmo nome, parâmetros diferentes: esta versão usa a hora de agora
    public Registro registrarEntrada(Veiculo veiculo, int vaga_numero) {
        return registrarEntrada(veiculo, vaga_numero, LocalDateTime.now());
    }

    // Esta versão recebe o horário (usada na demonstração com horários fixos)
    // Retorna o Registro se deu certo, ou null se alguma regra impediu
    public Registro registrarEntrada(Veiculo veiculo, int vaga_numero, LocalDateTime horario_entrada) {

        // Regra: veículo não pode entrar se já está estacionado
        Veiculo ja_estacionado = consultarVeiculo(veiculo.getPlaca());
        if (ja_estacionado != null) {
            this.mensagem_erro = "O veículo de placa " + veiculo.getPlaca() + " já está estacionado na vaga "
                    + Formatador.numeroVaga(ja_estacionado.getVaga().getNumero()) + ".";
            return null;
        }

        // Regra: só entra se houver vaga disponível
        if (verificarVagasDisponiveis().size() == 0) {
            this.mensagem_erro = "Estacionamento lotado! Não há vagas disponíveis.";
            return null;
        }

        Vaga vaga = buscarVaga(vaga_numero);
        if (vaga == null) {
            this.mensagem_erro = "A vaga " + vaga_numero + " não existe. Escolha entre 1 e " + this.vagas.size() + ".";
            return null;
        }

        // Regra: cada vaga recebe apenas um veículo por vez
        if (!vaga.ocupar(veiculo)) {
            this.mensagem_erro = "A vaga " + Formatador.numeroVaga(vaga_numero) + " já está ocupada.";
            return null;
        }

        veiculo.setEntrada(horario_entrada);
        veiculo.setSaida(null);
        veiculo.setVaga(vaga);

        Registro registro = new Registro(veiculo, vaga.getNumero(), horario_entrada);
        this.historico.add(registro);
        return registro;
    }

    // ===================== SAÍDA =====================

    // [SOBRECARGA] saída pelo número da vaga, hora de agora
    public Registro registrarSaida(int vaga_numero) {
        return registrarSaida(vaga_numero, LocalDateTime.now());
    }

    // [SOBRECARGA] saída pela placa, hora de agora
    public Registro registrarSaida(String placa) {
        return registrarSaida(placa, LocalDateTime.now());
    }

    // [SOBRECARGA] saída pela placa com horário informado
    public Registro registrarSaida(String placa, LocalDateTime horario_saida) {
        String placa_normalizada = Formatador.normalizarPlaca(placa);
        Veiculo veiculo = consultarVeiculo(placa_normalizada);

        // Regra: saída só para veículo que está estacionado
        if (veiculo == null) {
            this.mensagem_erro = "Nenhum veículo estacionado com a placa " + placa_normalizada + ". Saída não registrada.";
            return null;
        }
        return registrarSaida(veiculo.getVaga().getNumero(), horario_saida);
    }

    // [SOBRECARGA] saída pelo número da vaga com horário informado (versão principal)
    public Registro registrarSaida(int vaga_numero, LocalDateTime horario_saida) {
        Vaga vaga = buscarVaga(vaga_numero);
        if (vaga == null) {
            this.mensagem_erro = "A vaga " + vaga_numero + " não existe.";
            return null;
        }
        if (vaga.isDisponivel()) {
            this.mensagem_erro = "A vaga " + Formatador.numeroVaga(vaga_numero) + " já está livre. Não há veículo para sair.";
            return null;
        }

        Veiculo veiculo = vaga.getVeiculo();
        if (horario_saida.isBefore(veiculo.getEntrada())) {
            this.mensagem_erro = "O horário de saída não pode ser anterior ao de entrada.";
            return null;
        }

        veiculo.setSaida(horario_saida);

        // [POLIMORFISMO] "veiculo" é do tipo Veiculo, mas na hora de calcular o Java usa a regra
        // do objeto real (Carro, Moto ou Caminhonete)
        double valor = veiculo.calcularValor();

        Registro registro = buscarRegistroEmAberto(veiculo);
        registro.registrarSaida(horario_saida, valor);

        vaga.liberar();
        veiculo.setVaga(null);
        return registro;
    }

    // ===================== CONSULTAS =====================

    // Procura um veículo ESTACIONADO pela placa. Retorna null se não achar.
    public Veiculo consultarVeiculo(String placa) {
        String placa_normalizada = Formatador.normalizarPlaca(placa);
        for (Vaga vaga : this.vagas) {
            Veiculo veiculo = vaga.getVeiculo();
            if (veiculo != null && veiculo.getPlaca().equals(placa_normalizada)) {
                return veiculo;
            }
        }
        return null;
    }

    // Todas as passagens de uma placa pelo estacionamento (para quem já saiu)
    public List<Registro> consultarHistoricoPorPlaca(String placa) {
        String placa_normalizada = Formatador.normalizarPlaca(placa);
        List<Registro> encontrados = new ArrayList<>();
        for (Registro registro : this.historico) {
            if (registro.getPlaca().equals(placa_normalizada)) {
                encontrados.add(registro);
            }
        }
        return encontrados;
    }

    // Lista os veículos que estão estacionados agora
    public List<Veiculo> consultarVeiculosEstacionados() {
        List<Veiculo> estacionados = new ArrayList<>();
        for (Vaga vaga : this.vagas) {
            if (!vaga.isDisponivel()) {
                estacionados.add(vaga.getVeiculo());
            }
        }
        return estacionados;
    }

    // Lista só as vagas livres
    public List<Vaga> verificarVagasDisponiveis() {
        List<Vaga> livres = new ArrayList<>();
        for (Vaga vaga : this.vagas) {
            if (vaga.isDisponivel()) {
                livres.add(vaga);
            }
        }
        return livres;
    }

    public List<Registro> visualizarHistorico() {
        return this.historico;
    }

    // Soma o valor pago de todos os registros
    public double calcularFaturamentoTotal() {
        double total = 0;
        for (Registro registro : this.historico) {
            total = total + registro.getValorPago();
        }
        return total;
    }

    // ===================== MÉTODOS AUXILIARES (private: só esta classe usa) =====================

    private Vaga buscarVaga(int numero) {
        for (Vaga vaga : this.vagas) {
            if (vaga.getNumero() == numero) {
                return vaga;
            }
        }
        return null;
    }

    // Procura, do fim para o começo, o registro ainda sem saída daquele veículo
    private Registro buscarRegistroEmAberto(Veiculo veiculo) {
        int i = this.historico.size() - 1;
        while (i >= 0) {
            Registro registro = this.historico.get(i);
            if (registro.getVeiculo() == veiculo && registro.isEmAberto()) {
                return registro;
            }
            i--;
        }
        return null;
    }
}
