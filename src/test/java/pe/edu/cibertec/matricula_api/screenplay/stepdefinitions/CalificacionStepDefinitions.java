package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.CalificarMatricula;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarHistorial;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarRecurso;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;

public class CalificacionStepDefinitions {

    private long idMatricula;

    @When("obtiene el id de su matricula del curso {string}")
    public void obtieneElId(String curso) {
        theActorInTheSpotlight().attemptsTo(ConsultarRecurso.en("/api/matriculas"));
        idMatricula = SerenityRest.lastResponse().jsonPath()
                .getLong("find { it.codigoCurso == '" + curso + "' }.id");
    }

    @When("registra la nota {double} en la matricula de la alumna")
    public void registraLaNota(double nota) {
        theActorInTheSpotlight().attemptsTo(CalificarMatricula.conNota(idMatricula, nota));
    }

    @When("{word} consulta el historial de {string}")
    public void consultaElHistorial(String nombre, String codigo) {
        theActorCalled(nombre).attemptsTo(ConsultarHistorial.de(codigo));
    }

    @Then("el historial muestra para {string} la condicion {string}, {int} creditos aprobados y promedio ponderado {double}")
    public void elHistorialMuestra(String curso, String condicion, int creditos, double promedio) {
        theActorInTheSpotlight().should(
                seeThat(ElHistorial.condicionDe(curso), equalTo(condicion)),
                seeThat(ElHistorial.creditosAprobados(), equalTo(creditos)),
                seeThat(ElHistorial.promedioPonderado(), closeTo(promedio, 0.001)));
    }
}