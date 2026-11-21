# language: es
Característica: Calificación de matrículas
  Como docente de CIBERTEC
  Quiero registrar las notas de mis alumnos
  Para que el historial académico se actualice

  Esquema del escenario: El docente registra la nota y el historial se actualiza
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con el usuario "i202610005" y la clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PI5620"
    Y que Marco consulta la API de matricula
    Y inicia sesion con el usuario "D0001" y la clave "Cibertec2026"
    Cuando registra la nota <nota> en la matricula de la alumna
    Entonces el codigo de respuesta es 200
    Cuando Rosa consulta el historial de "i202610005"
    Entonces el codigo de respuesta es 200
    Y la respuesta cumple el esquema "historial.json"
    Y el historial muestra para "PI5620" la condicion "<condicion>", <creditos> creditos aprobados y promedio ponderado <promedio>

    Ejemplos:
      | nota | condicion   | creditos | promedio |
      | 10   | DESAPROBADO | 7        | 12,92    |
      | 12,5 | DESAPROBADO | 7        | 14,08    |

  Escenario: Un docente que no dicta el curso intenta registrar la nota
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con el usuario "i202610005" y la clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PI5620"
    Y que Lucia consulta la API de matricula
    Y inicia sesion con el usuario "D0002" y la clave "Cibertec2026"
    Cuando registra la nota 15 en la matricula de la alumna
    Entonces el codigo de respuesta es 403
    Y el codigo de error de la respuesta es "ACCESO_DENEGADO"
    Y la respuesta cumple el esquema "error.json"

  Escenario: No se puede calificar una matrícula que no está pagada
    Dado que Rosa consulta la API de matricula
    Y inicia sesion con el usuario "i202610005" y la clave "Cibertec2026"
    Y obtiene el id de su matricula del curso "PS5511"
    Y que Marco consulta la API de matricula
    Y inicia sesion con el usuario "D0001" y la clave "Cibertec2026"
    Cuando registra la nota 15 en la matricula de la alumna
    Entonces el codigo de respuesta es 409
    Y el codigo de error de la respuesta es "MATRICULA_NO_PAGADA"
    Y la respuesta cumple el esquema "error.json"