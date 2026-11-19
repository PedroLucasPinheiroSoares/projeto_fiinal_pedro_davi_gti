# Sistema de Gerenciamento de Estacionamento (Java, console)

Projeto Final de POO. Versão 1: o usuário só digita o NÚMERO DA OPÇÃO do menu.
Veículos, vagas, horários e placas são objetos prontos, criados com os construtores.

## Estrutura (3 camadas do diagrama UML)

```
sistema-estacionamento/
├── src/
│   ├── Main.java                 <- compile e execute este
│   ├── apresentacao/             (Presentation Layer)
│   │   ├── Menu.java             interface
│   │   └── ConsoleUI.java        implements Menu + Scanner
│   ├── negocio/                  (Business Layer)
│   │   ├── Estacionamento.java
│   │   └── Registro.java
│   ├── dominio/                  (Domain Layer)
│   │   ├── Veiculo.java          classe abstrata
│   │   ├── Carro.java / Moto.java / Caminhonete.java
│   │   ├── Vaga.java
│   │   └── StatusVaga.java       enum
│   └── util/Formatador.java
├── compilar_e_executar.bat
└── compilar_e_executar.sh
```

## Executar

VS Code: abra a pasta, abra `src/Main.java` e clique em Run.

Terminal, dentro da pasta do projeto:

```
javac -encoding UTF-8 -d bin src/Main.java src/*/*.java
java -cp bin Main
```

(Windows com acento errado: `chcp 65001` antes.)

## Como usar a demonstração

Cada vez que você escolhe uma opção, o sistema usa o PRÓXIMO dado pronto:

| Opção | O que acontece a cada vez que você escolhe |
| --- | --- |
| 1 | 1ª a 5ª: entram 5 veículos. 6ª: placa repetida (erro). 7ª: estacionamento cheio (erro). 8ª: entra, se já houve saída |
| 2 | 1ª: sai ABC1234. 2ª: placa inexistente (erro). 3ª: sai a caminhonete |
| 3 / 4 / 6 / 7 | consultam o estado atual (vagas, estacionados, histórico, faturamento) |
| 5 | 1ª: veículo estacionado. 2ª: placa inexistente. 3ª: veículo que já saiu (histórico) |
| 8 | reinicia tudo |
| 0 | sai |

Roteiro sugerido: 1 1 1 1 1 1 1 3 4 5 5 2 2 2 5 1 3 4 6 7 0

## Legenda das anotações nos arquivos

- `[MÉTODO CONSTRUTOR]` método que cria o objeto
- `[ATRIBUTOS - snake_case]` variáveis da classe
- `[GET]` / `[SET]` ler / alterar atributo
- `[ENCAPSULAMENTO]` private/protected + get/set
- `[HERANÇA]` extends, `[INTERFACE]` implements
- `[POLIMORFISMO]` mesmo método, comportamento diferente por classe
- `[SOBRECARGA]` mesmo nome de método, parâmetros diferentes
- `[NOVO - ainda não visto]` conceito que ainda não apareceu nas aulas
