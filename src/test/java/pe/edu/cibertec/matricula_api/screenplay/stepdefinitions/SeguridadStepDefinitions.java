package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.screenplay.Actor;
import pe.edu.cibertec.matricula_api.screenplay.questions.ElCodigoDeError;
import pe.edu.cibertec.matricula_api.screenplay.task.CerrarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.ConsultarRecurso;
import pe.edu.cibertec.matricula_api.screenplay.task.IniciarSesion;
import pe.edu.cibertec.matricula_api.screenplay.task.RenovarToken;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class SeguridadStepDefinitions {

    @When("inicia sesion con el usuario {string} y la clave {string}")
    public void iniciaSesion(String usuario, String clave) {
        theActorInTheSpotlight().attemptsTo(IniciarSesion.como(usuario, clave));
    }

    @When("renueva sus tokens con el refresh token vigente")
    public void renueva() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.usando("refreshToken"));
    }

    @When("intenta renovar con el refresh token original")
    public void renuevaConElOriginal() {
        theActorInTheSpotlight().attemptsTo(RenovarToken.usando("refreshInicial"));
    }

    @Then("recibe tokens nuevos distintos a los originales")
    public void tokensNuevos() {
        Actor actor = theActorInTheSpotlight();
        String acceso = actor.recall("accessToken");
        String refresh = actor.recall("refreshToken");
        String accesoInicial = actor.recall("accessInicial");
        String refreshInicial = actor.recall("refreshInicial");
        assertThat(acceso, not(equalTo(accesoInicial)));
        assertThat(refresh, not(equalTo(refreshInicial)));
    }

    @When("cierra sesion")
    public void cierraSesion() {
        theActorInTheSpotlight().attemptsTo(CerrarSesion.actual());
    }

    @When("consulta su perfil con su access token actual")
    public void consultaSuPerfil() {
        theActorInTheSpotlight().attemptsTo(ConsultarRecurso.en("/api/auth/perfil"));
    }

    @When("consulta la lista de alumnos")
    public void consultaLosAlumnos() {
        theActorInTheSpotlight().attemptsTo(ConsultarRecurso.en("/api/alumnos"));
    }

    @Then("el codigo de error de la respuesta es {string}")
    public void elCodigoDeError(String codigo) {
        theActorInTheSpotlight().should(seeThat(ElCodigoDeError.enLaRespuesta(), equalTo(codigo)));
    }
}