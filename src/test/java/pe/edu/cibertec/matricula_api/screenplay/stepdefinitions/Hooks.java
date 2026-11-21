package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.Before;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hooks {

    private static final String NOMBRE = "Marcos Jemerzon Esteban Ñaupari";
    private static final String CODIGO = "i202404300";

    @Before(order = 0)
    public void identificacion() {
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        System.out.println(" ");
        System.out.println("Alumno : " + NOMBRE);
        System.out.println("Codigo : " + CODIGO);
        System.out.println("Fecha  : " + fecha);
        System.out.println(" ");
    }

    @Before(order = 1)
    public void beforeScenario() {
        OnStage.setTheStage(new OnlineCast());
    }
}