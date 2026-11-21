# language: es
Característica: Seguridad de la sesión
  Como usuario de la API de matrícula
  Quiero que mi sesión esté protegida
  Para que nadie use mis credenciales de forma indebida

  Escenario: Rotación del refresh token
    Dado que Ana consulta la API de matricula
    Y inicia sesion con el usuario "i202610001" y la clave "Cibertec2026"
    Cuando renueva sus tokens con el refresh token vigente
    Entonces el codigo de respuesta es 200
    Y recibe tokens nuevos distintos a los originales
    Cuando intenta renovar con el refresh token original
    Entonces el codigo de respuesta es 401
    Y el codigo de error de la respuesta es "REFRESH_REUTILIZADO"
    Y la respuesta cumple el esquema "error.json"
    Cuando consulta su perfil con su access token actual
    Entonces el codigo de respuesta es 401
    Y el codigo de error de la respuesta es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: Cierre de sesión
    Dado que Ana consulta la API de matricula
    Y inicia sesion con el usuario "i202610001" y la clave "Cibertec2026"
    Cuando cierra sesion
    Entonces el codigo de respuesta es 204
    Cuando consulta su perfil con su access token actual
    Entonces el codigo de respuesta es 401
    Y el codigo de error de la respuesta es "TOKEN_INVALIDO"
    Y la respuesta cumple el esquema "error.json"

  Esquema del escenario: Bloqueo de cuenta por intentos fallidos, intento <intento>
    Dado que Jorge consulta la API de matricula
    Cuando inicia sesion con el usuario "i202610004" y la clave "<clave>"
    Entonces el codigo de respuesta es <status>
    Y el codigo de error de la respuesta es "<codigo>"
    Y la respuesta cumple el esquema "error.json"

    Ejemplos:
      | intento | clave        | status | codigo                 |
      | 1       | Incorrecta1  | 401    | CREDENCIALES_INVALIDAS |
      | 2       | Incorrecta2  | 401    | CREDENCIALES_INVALIDAS |
      | 3       | Incorrecta3  | 401    | CREDENCIALES_INVALIDAS |
      | 4       | Incorrecta4  | 401    | CREDENCIALES_INVALIDAS |
      | 5       | Incorrecta5  | 423    | CUENTA_BLOQUEADA       |
      | 6       | Cibertec2026 | 423    | CUENTA_BLOQUEADA       |

  Escenario: Alumno sin permiso de administrador
    Dado que Ana consulta la API de matricula
    Y inicia sesion con el usuario "i202610001" y la clave "Cibertec2026"
    Cuando consulta la lista de alumnos
    Entonces el codigo de respuesta es 403
    Y el codigo de error de la respuesta es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"