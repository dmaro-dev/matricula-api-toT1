package pe.edu.cibertec.matricula_api.screenplay.task;

import net.serenitybdd.annotations.Step;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

public class ConsultarRecurso implements Task {

    private final String ruta;

    public ConsultarRecurso(String ruta) {
        this.ruta = ruta;
    }

    public static ConsultarRecurso en(String ruta) {
        return new ConsultarRecurso(ruta);
    }

    @Override
    @Step("{0} consulta #ruta")
    public <T extends Actor> void performAs(T actor) {
        String token = actor.recall("accessToken");
        actor.attemptsTo(Get.resource(ruta)
                .with(req -> req.header("Authorization", "Bearer " + token)));
    }
}