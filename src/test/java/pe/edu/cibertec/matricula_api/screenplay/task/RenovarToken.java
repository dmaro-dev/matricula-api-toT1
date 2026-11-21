package pe.edu.cibertec.matricula_api.screenplay.task;

import io.restassured.http.ContentType;
import net.serenitybdd.annotations.Step;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import java.util.Map;

public class RenovarToken implements Task {

    private final String memoria;

    public RenovarToken(String memoria) {
        this.memoria = memoria;
    }

    public static RenovarToken usando(String memoria) {
        return new RenovarToken(memoria);
    }

    @Override
    @Step("{0} renueva sus tokens usando #memoria")
    public <T extends Actor> void performAs(T actor) {
        String refresh = actor.recall(memoria);
        actor.attemptsTo(Post.to("/api/auth/refresh")
                .with(req -> req.contentType(ContentType.JSON).body(Map.of("refreshToken", refresh))));
        if (SerenityRest.lastResponse().statusCode() == 200) {
            actor.remember("accessToken", SerenityRest.lastResponse().jsonPath().getString("accessToken"));
            actor.remember("refreshToken", SerenityRest.lastResponse().jsonPath().getString("refreshToken"));
        }
    }
}