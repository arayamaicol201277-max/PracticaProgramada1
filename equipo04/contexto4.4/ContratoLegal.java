package equipo04.contexto04;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ContratoLegal implements IContratoPrototype {

    private final String formatoInstitucional;
    private final List<Clausula> clausulasEstandarizadas;
    private final List<Anexo> anexosFijos;

    private String nombrePartes;
    private BigDecimal monto;
    private LocalDate fecha;

    public ContratoLegal(String formatoInstitucional, List<Clausula> clausulas, List<Anexo> anexos) {
        this.formatoInstitucional = Objects.requireNonNull(formatoInstitucional, "El formato no puede ser nulo");
        this.clausulasEstandarizadas = new ArrayList<>(Objects.requireNonNull(clausulas, "Las cláusulas no pueden ser nulas"));
        this.anexosFijos = new ArrayList<>(Objects.requireNonNull(anexos, "Los anexos no pueden ser nulos"));
        System.out.println("[Sistema Contexto 4.4] Procesando y maquetando estructura del contrato base...");
    }


    private ContratoLegal(ContratoLegal prototipo) {
        this.formatoInstitucional = prototipo.formatoInstitucional;
        this.clausulasEstandarizadas = new ArrayList<>(prototipo.clausulasEstandarizadas);
        this.anexosFijos = new ArrayList<>(prototipo.anexosFijos);
        this.nombrePartes = prototipo.nombrePartes;
        this.monto = prototipo.monto;
        this.fecha = prototipo.fecha;
    }

    @Override
    public ContratoLegal clonar() {
        return new ContratoLegal(this);
    }

    public void personalizarCampos(String partes, BigDecimal monto, LocalDate fecha) {
        this.nombrePartes = partes;
        this.monto = monto;
        this.fecha = fecha;
    }

    public void mostrarDetalles() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        System.out.println("\n==================================================");
        System.out.println("FORMATO: " + formatoInstitucional);
        System.out.println("PARTES: " + (nombrePartes != null ? nombrePartes : "Pendiente"));
        System.out.println("MONTO: " + (monto != null ? String.format(Locale.US, "$%.2f", monto) : "Pendiente"));
        System.out.println("FECHA DE FIRMA: " + (fecha != null ? fecha.format(formatter) : "Pendiente"));
        System.out.println("CLÁUSULAS INCLUIDAS:");
        for (Clausula c : clausulasEstandarizadas) {
            System.out.println(" - " + c.getTitulo() + ": " + c.getTexto());
        }
        System.out.println("ANEXOS INCLUIDOS:");
        for (Anexo a : anexosFijos) {
            System.out.println(" - " + a.getNombre());
        }
        System.out.println("==================================================");
    }
}
