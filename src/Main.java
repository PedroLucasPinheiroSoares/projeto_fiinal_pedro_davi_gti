import apresentacao.ConsoleUI;
import negocio.Estacionamento;

// Classe principal: é aqui que o programa começa (compile e execute este arquivo).
//
// LEGENDA DAS ANOTAÇÕES USADAS NOS ARQUIVOS:
//   [MÉTODO CONSTRUTOR]  -> método que cria o objeto (mesmo nome da classe)
//   [ATRIBUTOS - snake_case] -> variáveis da classe escritas_assim
//   [GET] / [SET]        -> ler / alterar um atributo
//   [ENCAPSULAMENTO]     -> atributo private/protected com acesso por get/set
//   [HERANÇA]            -> extends
//   [POLIMORFISMO]       -> mesmo método, comportamento diferente em cada classe
//   [INTERFACE]          -> implements
//   [SOBRECARGA]         -> mesmo nome de método com parâmetros diferentes
//   [RELACIONAMENTO]     -> uma classe usando outra
//   [NOVO - ainda não visto] -> conceito que ainda não apareceu nas suas aulas
public class Main {
    public static void main(String[] args) {
        Estacionamento estacionamento = new Estacionamento(5);   // 5 vagas
        ConsoleUI console = new ConsoleUI(estacionamento);

        console.iniciar();
    }
}
