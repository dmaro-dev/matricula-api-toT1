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