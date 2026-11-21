# language: es
Característica: Matrícula del alumno
  Como alumno de CIBERTEC
  Quiero cotizar, matricularme y pagar mis cursos
  Para llevarlos en el periodo abierto

  Antecedentes:
    Dado que el alumno "i202610001" inicia sesion con la clave "Cibertec2026"

  Escenario: Cotizar el curso
    Cuando cotiza el curso "TS5514"
    Entonces el codigo de respuesta es 200
    Y la cotizacion es:
      | creditos | subtotal | descuento | total |
      | 4        | 480,0    | 20        | 384,0 |

  Escenario: Matricularse y pagar el curso
    Cuando cotiza el curso "TS5514"
    Y se matricula en el curso "TS5514"
    Entonces el codigo de respuesta es 201
    Y la matricula queda "REGISTRADA" con monto "384,0" igual al de la cotizacion
    Y la respuesta cumple el esquema "matricula.json"
    Cuando paga la matricula con el metodo "YAPE"
    Entonces el codigo de respuesta es 200
    Y la matricula queda "PAGADA" con fecha de pago
    Cuando consulta sus pagos
    Entonces el codigo de respuesta es 200
    Y existe un pago de la matricula con metodo "YAPE" y numero de operacion valido