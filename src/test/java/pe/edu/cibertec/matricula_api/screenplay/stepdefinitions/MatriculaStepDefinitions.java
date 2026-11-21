package pe.edu.cibertec.matricula_api.screenplay.stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.thucydides.model.environment.SystemEnvironmentVariables;

import java.util.List;
import java.util.Map;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;

public class MatriculaStepDefinitions {

    private final String baseUrl = EnvironmentSpecificConfiguration
            .from(SystemEnvironmentVariables.currentEnvironmentVariables())
            .getProperty("restapi.baseurl");

    private String alumno;
    private String token;
    private double totalCotizado;
    private long idMatricula;

    @Given("que el alumno {string} inicia sesion con la clave {string}")
    public void iniciaSesion(String usuario, String clave) {
        theActorCalled(usuario).whoCan(CallAnApi.at(baseUrl));
        SerenityRest.given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(Map.of("username", usuario, "password", clave))
                .post("/api/auth/login");
        assertThat(SerenityRest.lastResponse().statusCode(), equalTo(200));
        alumno = usuario;
        token = SerenityRest.lastResponse().jsonPath().getString("accessToken");
    }

    @When("cotiza el curso {string}")
    public void cotiza(String curso) {
        SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("codigoAlumno", alumno, "cursos", List.of(curso)))
                .post("/api/matriculas/cotizacion");
        if (SerenityRest.lastResponse().statusCode() == 200) {
            totalCotizado = SerenityRest.lastResponse().jsonPath().getDouble("total");
        }
    }

    @Then("la cotizacion es:")
    public void laCotizacionEs(DataTable tabla) {
        Map<String, String> esperado = tabla.asMaps().get(0);
        var respuesta = SerenityRest.lastResponse();
        assertThat(respuesta.jsonPath().getInt("creditos"), equalTo(Integer.parseInt(esperado.get("creditos"))));
        assertThat(respuesta.jsonPath().getDouble("subtotal"), equalTo(numero(esperado.get("subtotal"))));
        assertThat(respuesta.jsonPath().getInt("descuento"), equalTo(Integer.parseInt(esperado.get("descuento"))));
        assertThat(respuesta.jsonPath().getDouble("total"), equalTo(numero(esperado.get("total"))));
    }

    @When("se matricula en el curso {string}")
    public void seMatricula(String curso) {
        SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("codigoAlumno", alumno, "codigoCurso", curso))
                .post("/api/matriculas");
        if (SerenityRest.lastResponse().statusCode() == 201) {
            idMatricula = SerenityRest.lastResponse().jsonPath().getLong("id");
        }
    }

    @Then("la matricula queda {string} con monto {string} igual al de la cotizacion")
    public void laMatriculaQueda(String estado, String monto) {
        var respuesta = SerenityRest.lastResponse();
        assertThat(respuesta.jsonPath().getString("estado"), equalTo(estado));
        assertThat(respuesta.jsonPath().getDouble("monto"), equalTo(numero(monto)));
        assertThat(respuesta.jsonPath().getDouble("monto"), equalTo(totalCotizado));
    }

    @When("paga la matricula con el metodo {string}")
    public void pagaLaMatricula(String metodo) {
        SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON)
                .body(Map.of("metodo", metodo))
                .patch("/api/matriculas/{id}/pagar", idMatricula);
    }

    @Then("la matricula queda {string} con fecha de pago")
    public void laMatriculaQuedaPagada(String estado) {
        var respuesta = SerenityRest.lastResponse();
        assertThat(respuesta.jsonPath().getString("estado"), equalTo(estado));
        assertThat(respuesta.jsonPath().getString("fechaPago"), notNullValue());
    }

    @When("consulta sus pagos")
    public void consultaSusPagos() {
        SerenityRest.given()
                .baseUri(baseUrl)
                .header("Authorization", "Bearer " + token)
                .get("/api/pagos");
    }

    @Then("existe un pago de la matricula con metodo {string} y numero de operacion valido")
    public void existeElPago(String metodo) {
        Map<String, Object> pago = SerenityRest.lastResponse().jsonPath()
                .getMap("find { it.idMatricula == " + idMatricula + " }");
        assertThat(pago, notNullValue());
        assertThat((String) pago.get("metodo"), equalTo(metodo));
        assertThat((String) pago.get("numeroOperacion"), matchesPattern("OP-\\d{6}"));
    }

    private static double numero(String texto) {
        return Double.parseDouble(texto.replace(',', '.'));
    }
}