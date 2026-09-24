package equipo04;

import equipo04.contexto04.Anexo;
import equipo04.contexto04.Clausula;
import equipo04.contexto04.ContratoLegal;
import equipo04.contexto04.GestorContratos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== DEMOSTRACIÓN DEL CONTEXTO 4.4 (PATRÓN PROTOTYPE) ===\n");

        // 1. Crear componentes base
        List<Clausula> clausulas = new ArrayList<>();
        clausulas.add(new Clausula("Cláusula 1", "Confidencialidad y no divulgación."));
        clausulas.add(new Clausula("Cláusula 2", "Jurisdicción y resolución de conflictos."));

        List<Anexo> anexos = new ArrayList<>();
        anexos.add(new Anexo("Anexo A - Reglamento Interno", new byte[]{0x01, 0x02}));

        // 2. Instanciar prototipo y registrar en Gestor
        ContratoLegal contratoBase = new ContratoLegal("ISO-9001-v2026-Legal", clausulas, anexos);
        GestorContratos gestor = new GestorContratos();
        gestor.registrarPrototipo("ContratoComercial", contratoBase);

        // 3. Clonar y personalizar los contratos individuales
        System.out.println("\n[Acción] Clonando prototipo base para Cliente 1...");
        ContratoLegal cliente1 = gestor.obtenerClon("ContratoComercial", ContratoLegal.class);
        cliente1.personalizarCampos("Empresa Alfa S.A. & Corp Beta",
                new BigDecimal("150000.00"), LocalDate.now());

        System.out.println("[Acción] Clonando prototipo base para Cliente 2...");
        ContratoLegal cliente2 = gestor.obtenerClon("ContratoComercial", ContratoLegal.class);
        cliente2.personalizarCampos("TechSolutions Ltda. & Maicol Araya Urbina",
                new BigDecimal("45000.50"), LocalDate.now().plusDays(5));

        // 4. Imprimir resultados
        cliente1.mostrarDetalles();
        cliente2.mostrarDetalles();
    }
}
