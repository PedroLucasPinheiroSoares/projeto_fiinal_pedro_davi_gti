package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Classe de apoio: só tem métodos static (igual as funções static das aulas 10_funcao),
// por isso é chamada direto: Formatador.moeda(12.0), sem precisar de "new".
public class Formatador {

    // [NOVO - ainda não visto] DateTimeFormatter: define o "molde" de exibição da hora/data
    private static DateTimeFormatter molde_hora = DateTimeFormatter.ofPattern("HH:mm");

    // Formata dinheiro: 22.0 vira "R$ 22,00"
    // [NOVO - ainda não visto] String.format: monta texto com casas decimais controladas
    public static String moeda(double valor) {
        String texto = String.format("%.2f", valor);
        return "R$ " + texto.replace(".", ",");
    }

    // Mostra só a hora: 14:30
    public static String hora(LocalDateTime data_hora) {
        if (data_hora == null) {
            return "--:--";
        }
        return data_hora.format(molde_hora);
    }

    // Número da vaga com dois dígitos: 5 vira "05"
    public static String numeroVaga(int numero) {
        return String.format("%02d", numero);
    }

    // Minutos viram "2h30min"
    public static String duracao(long minutos) {
        return (minutos / 60) + "h" + String.format("%02d", minutos % 60) + "min";
    }

    // Deixa a placa em maiúsculo e sem espaços: " abc1234 " vira "ABC1234"
    public static String normalizarPlaca(String placa) {
        if (placa == null) {
            return "";
        }
        return placa.trim().toUpperCase();
    }
}
