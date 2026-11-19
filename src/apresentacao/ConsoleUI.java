package apresentacao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dominio.Caminhonete;
import dominio.Carro;
import dominio.Moto;
import dominio.Vaga;
import dominio.Veiculo;
import negocio.Estacionamento;
import negocio.Registro;
import util.Formatador;

// Tela do sistema (console).
//
// A ÚNICA coisa digitada pelo usuário é o NÚMERO DA OPÇÃO do menu (via Scanner).
// Todo o resto (veículos, vagas, horários, placas) já vem pronto, criado com os construtores.
//
// [INTERFACE] "implements Menu" = esta classe cumpre o contrato e escreve exibeMenuPrincipal() e processarEntrada()
public class ConsoleUI implements Menu {

    // [ATRIBUTOS - snake_case]
    private Estacionamento sistema;                      // [RELACIONAMENTO] a tela usa o Estacionamento
    private Scanner leitor = new Scanner(System.in);     // [NOVO - ainda não visto] Scanner lê o que o usuário digita
    private String linha = "====================================";

    // ---------- Dados prontos (fixos) ----------
    // [POLIMORFISMO] a lista é de Veiculo, mas guarda objetos Carro, Moto e Caminhonete
    private List<Veiculo> veiculos_entrada = new ArrayList<>();
    private int[] vagas_entrada = {5, 2, 3, 1, 4, 1, 2, 5};
    private List<LocalDateTime> horarios_entrada = new ArrayList<>();

    private String[] placas_saida = {"ABC1234", "ZZZ0000", "CAM9D87"};
    private List<LocalDateTime> horarios_saida = new ArrayList<>();

    private String[] placas_consulta = {"MOT5B67", "ZZZ9999", "ABC1234"};

    // Controlam qual dado pronto será usado na próxima vez que o usuário escolher a opção
    private int indice_entrada = 0;
    private int indice_saida = 0;
    private int indice_consulta = 0;

    // [MÉTODO CONSTRUTOR]
    public ConsoleUI(Estacionamento sistema) {
        this.sistema = sistema;
        carregarDadosProntos();
    }

    // Cria os objetos prontos. Fica separado para poder "reiniciar" a demonstração (opção 8)
    private void carregarDadosProntos() {
        this.veiculos_entrada.clear();
        this.horarios_entrada.clear();
        this.horarios_saida.clear();
        this.indice_entrada = 0;
        this.indice_saida = 0;
        this.indice_consulta = 0;

        // Veículos prontos (cada "new" chama o MÉTODO CONSTRUTOR da classe)
        this.veiculos_entrada.add(new Carro("ABC1234", "Toyota", "Corolla"));
        this.veiculos_entrada.add(new Moto("MOT5B67", "Honda", "CG 160"));
        this.veiculos_entrada.add(new Caminhonete("CAM9D87", "Ford", "Ranger"));
        this.veiculos_entrada.add(new Carro("XYZ4321", "Chevrolet", "Onix"));
        this.veiculos_entrada.add(new Moto("BRA2E19", "Yamaha", "Fazer 250"));
        this.veiculos_entrada.add(new Carro("ABC1234", "Toyota", "Corolla"));    // 6ª vez: placa repetida -> erro
        this.veiculos_entrada.add(new Carro("QWE5678", "Volkswagen", "Polo"));   // 7ª vez: estacionamento cheio -> erro
        this.veiculos_entrada.add(new Carro("QWE5678", "Volkswagen", "Polo"));   // 8ª vez: só entra depois de uma saída

        // Horários prontos
        LocalDate hoje = LocalDate.now();
        this.horarios_entrada.add(hoje.atTime(14, 30));
        this.horarios_entrada.add(hoje.atTime(14, 45));
        this.horarios_entrada.add(hoje.atTime(15, 0));
        this.horarios_entrada.add(hoje.atTime(15, 10));
        this.horarios_entrada.add(hoje.atTime(15, 20));
        this.horarios_entrada.add(hoje.atTime(15, 30));
        this.horarios_entrada.add(hoje.atTime(15, 40));
        this.horarios_entrada.add(hoje.atTime(18, 15));

        this.horarios_saida.add(hoje.atTime(17, 0));
        this.horarios_saida.add(hoje.atTime(17, 10));
        this.horarios_saida.add(hoje.atTime(18, 10));
    }

    // ===================== LOOP PRINCIPAL =====================

    // Repete o menu até o usuário digitar 0 (sair)
    public void iniciar() {
        int opcao = -1;

        while (opcao != 0) {
            System.out.println(" ");
            exibeMenuPrincipal();
            opcao = lerOpcao();
            processarEntrada(opcao);
        }

        this.leitor.close();
    }

    // ÚNICA entrada do usuário: o número da opção
    private int lerOpcao() {
        // Se acabou a entrada (ex.: Ctrl+D), encerra como se tivesse escolhido sair
        if (!this.leitor.hasNext()) {
            return 0;
        }
        // Se digitou número, usa; se digitou texto/letra, devolve -1 (opção inválida)
        if (this.leitor.hasNextInt()) {
            return this.leitor.nextInt();
        }
        this.leitor.next();
        return -1;
    }

    // ===================== MENU (métodos do contrato Menu) =====================

    @Override
    public void exibeMenuPrincipal() {
        System.out.println(this.linha);
        System.out.println("SISTEMA DE ESTACIONAMENTO");
        System.out.println(this.linha);
        System.out.println("1 - Registrar entrada de veículo");
        System.out.println("2 - Registrar saída de veículo");
        System.out.println("3 - Consultar vagas disponíveis");
        System.out.println("4 - Listar veículos estacionados");
        System.out.println("5 - Consultar veículo pela placa");
        System.out.println("6 - Visualizar histórico");
        System.out.println("7 - Calcular faturamento total");
        System.out.println("8 - Reiniciar demonstração");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    @Override
    public void processarEntrada(int opcao) {
        if (opcao == 1) {
            registrarEntrada();
        } else if (opcao == 2) {
            registrarSaida();
        } else if (opcao == 3) {
            consultarVagas();
        } else if (opcao == 4) {
            listarVeiculos();
        } else if (opcao == 5) {
            consultarVeiculoPorPlaca();
        } else if (opcao == 6) {
            visualizarHistorico();
        } else if (opcao == 7) {
            exibirFaturamento();
        } else if (opcao == 8) {
            reiniciar();
        } else if (opcao == 0) {
            System.out.println("Encerrando o sistema. Até logo!");
        } else {
            System.out.println("[ERRO] Opção inválida. Escolha uma opção do menu.");
        }
    }

    // ===================== DADOS PRONTOS (no lugar da digitação) =====================

    // No sistema final isto leria do teclado; agora devolve o próximo veículo pronto
    public Veiculo lerDadosVeiculo() {
        return this.veiculos_entrada.get(this.indice_entrada);
    }

    // Devolve o número da vaga pronto para esta entrada
    public int lerNumeroVaga() {
        return this.vagas_entrada[this.indice_entrada];
    }

    // ===================== OPERAÇÕES =====================

    private void registrarEntrada() {
        cabecalho("ENTRADA DE VEÍCULO");

        if (this.indice_entrada >= this.veiculos_entrada.size()) {
            System.out.println("Acabaram os veículos prontos. Use a opção 8 para reiniciar a demonstração.");
            return;
        }

        Veiculo veiculo = lerDadosVeiculo();
        int vaga_numero = lerNumeroVaga();
        LocalDateTime horario = this.horarios_entrada.get(this.indice_entrada);
        this.indice_entrada++;

        System.out.println("Informe a placa: " + veiculo.getPlaca());
        System.out.println("Informe a marca: " + veiculo.getMarca());
        System.out.println("Informe o modelo: " + veiculo.getModelo());
        System.out.println("Tipo de veículo: " + veiculo.getTipo());
        System.out.println("Vaga desejada: " + Formatador.numeroVaga(vaga_numero));

        Registro registro = this.sistema.registrarEntrada(veiculo, vaga_numero, horario);

        if (registro == null) {
            System.out.println("[ERRO] " + this.sistema.getMensagemErro());
        } else {
            System.out.println("Veículo registrado com sucesso!");
            System.out.println("Vaga ocupada: " + Formatador.numeroVaga(registro.getVagaNumero()));
            System.out.println("Horário de entrada: " + Formatador.hora(registro.getEntrada()));
        }
    }

    private void registrarSaida() {
        cabecalho("SAÍDA DE VEÍCULO");

        if (this.indice_saida >= this.placas_saida.length) {
            System.out.println("Acabaram as saídas prontas. Use a opção 8 para reiniciar a demonstração.");
            return;
        }

        String placa = this.placas_saida[this.indice_saida];
        LocalDateTime horario = this.horarios_saida.get(this.indice_saida);
        this.indice_saida++;

        System.out.println("Informe a placa: " + placa);

        Registro registro = this.sistema.registrarSaida(placa, horario);

        if (registro == null) {
            System.out.println("[ERRO] " + this.sistema.getMensagemErro());
        } else {
            Veiculo veiculo = registro.getVeiculo();
            System.out.println("Horário de entrada: " + Formatador.hora(registro.getEntrada()));
            System.out.println("Horário de saída: " + Formatador.hora(registro.getSaida()));
            System.out.println("Tempo de permanência: " + Formatador.duracao(veiculo.getPermanenciaMinutos())
                    + " (" + veiculo.getPermanencia() + " hora(s) cobrada(s) a "
                    + Formatador.moeda(veiculo.getValorHora()) + ")");
            System.out.println("Valor do estacionamento: " + Formatador.moeda(registro.getValorPago()));
            System.out.println("Vaga " + Formatador.numeroVaga(registro.getVagaNumero()) + " liberada com sucesso!");
        }
    }

    private void consultarVagas() {
        cabecalho("CONSULTA DE VAGAS");

        for (Vaga vaga : this.sistema.getVagas()) {
            System.out.println(vaga);
        }

        System.out.println("Livres: " + this.sistema.verificarVagasDisponiveis().size()
                + " | Ocupadas: " + this.sistema.consultarVeiculosEstacionados().size()
                + " | Total: " + this.sistema.getTotalVagas());
    }

    private void listarVeiculos() {
        cabecalho("VEÍCULOS ESTACIONADOS");

        List<Veiculo> estacionados = this.sistema.consultarVeiculosEstacionados();

        if (estacionados.size() == 0) {
            System.out.println("Nenhum veículo estacionado no momento.");
        }

        // [POLIMORFISMO] println(item) chama o toString() de Veiculo para cada objeto da lista
        for (Veiculo item : estacionados) {
            System.out.println(item);
        }
    }

    private void consultarVeiculoPorPlaca() {
        cabecalho("CONSULTA POR PLACA");

        if (this.indice_consulta >= this.placas_consulta.length) {
            System.out.println("Acabaram as consultas prontas. Use a opção 8 para reiniciar a demonstração.");
            return;
        }

        String placa = this.placas_consulta[this.indice_consulta];
        this.indice_consulta++;

        System.out.println("Informe a placa: " + placa);

        Veiculo veiculo = this.sistema.consultarVeiculo(placa);

        if (veiculo != null) {
            System.out.println("Veículo estacionado:");
            System.out.println(veiculo);
            return;
        }

        List<Registro> passagens = this.sistema.consultarHistoricoPorPlaca(placa);

        if (passagens.size() == 0) {
            System.out.println("[ERRO] Veículo com placa " + Formatador.normalizarPlaca(placa) + " não encontrado.");
        } else {
            System.out.println("O veículo não está estacionado, mas tem passagens no histórico:");
            for (Registro item : passagens) {
                System.out.println(item);
            }
        }
    }

    private void visualizarHistorico() {
        cabecalho("HISTÓRICO DE ENTRADAS E SAÍDAS");

        List<Registro> historico = this.sistema.visualizarHistorico();

        if (historico.size() == 0) {
            System.out.println("Histórico vazio.");
        }

        for (Registro item : historico) {
            System.out.println(item);
        }
    }

    private void exibirFaturamento() {
        cabecalho("FATURAMENTO TOTAL");
        System.out.println("Total arrecadado: " + Formatador.moeda(this.sistema.calcularFaturamentoTotal()));
    }

    // Volta tudo ao começo: estacionamento vazio e dados prontos do início
    private void reiniciar() {
        this.sistema = new Estacionamento(this.sistema.getTotalVagas());
        carregarDadosProntos();
        System.out.println("Demonstração reiniciada! Estacionamento vazio e dados prontos recarregados.");
    }

    private void cabecalho(String titulo) {
        System.out.println(this.linha);
        System.out.println(titulo);
        System.out.println(this.linha);
    }
}
