package apresentacao;

// [NOVO - ainda não visto] INTERFACE: é um "contrato". Só lista QUAIS métodos precisam existir
// (sem corpo). Quem usar "implements Menu" é obrigado a escrever todos eles.
// Parecido com o método abstrato do Animal, mas aqui a classe inteira é só contrato.
public interface Menu {

    void exibeMenuPrincipal();

    void processarEntrada(int opcao);
}
