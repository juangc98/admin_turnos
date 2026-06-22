## Trabajo Practico | UCES

Sistema de consola para gestionar doctores y pacientes en un hospital. Permite cargar datos desde JSON, operar mediante un menu interactivo, guardar resultados al finalizar y ejecutar tests unitarios.

Juan Gómez Carrillo
Programación II
Prof. Manuel Adrian Caceres

---

## Requisitos de la consigna

Según la consigna elegida:
-Diseñar los TAD correspondientes ( 1 puntos )
-Invariantes de representación ( 1 puntos )
-Diseño e implementación de clases ( 4 puntos )
-Administración de excepciones ( 1 punto )
-Mostrar resultados en pantalla ( 1 punto )
-Cargar los objetos desde un archivo json ( 0,5 puntos )
-Guardar los resultados en un nuevo archivo json ( 0,5 punto )
-Realizar test unitarios (1 punto)

---

## Consigna

Deben desarrollar un sistema para gestionar doctores y pacientes en un hospital. El sistema debe permitir la administracion de hasta un maximo de 10 doctores.

**Hospital:** nombre del sistema y fecha de creacion.

**Doctor:** nombre, apellido, identificacion medica (unica), especialidad y lista de pacientes atendidos.

**Paciente:** DNI como identificador unico, nombre, apellido, edad, genero, telefono, direccion, historia medica y fecha de ingreso.

**Reglas:**
- No se puede registrar un paciente en un doctor inexistente.
- Cada doctor atiende como maximo 10 pacientes.
- No se permiten doctores ni pacientes duplicados.

---

## Estructura del proyecto

```
admin_turnos/
├── src/
│   ├── Main.java              # Punto de entrada
│   ├── GestorHospital.java    # Menu y operaciones de consola
│   ├── Hospital.java          # Logica del hospital
│   ├── Doctor.java            # Doctor y sus pacientes
│   ├── Paciente.java          # Datos del paciente
│   ├── JsonManager.java       # Carga y guardado con Gson
│   ├── HospitalTest.java      # Tests unitarios
│   └── exceptions/            # Excepciones propias
├── lib/
│   └── gson-*.jar             # Dependencia Gson (descargar manualmente)
├── bin/                       # Clases compiladas (.class)
├── hospital.json              # Datos iniciales
└── resultados.json            # Generado al cerrar el programa
```

---

## Dependencias

El proyecto **no usa Maven**. Gson se agrega como archivo `.jar` dentro de `lib/`.

---

## Como correr el proyecto

### Compilar

Desde la raiz del proyecto (PowerShell):

```powershell
javac -cp "lib/*" -d bin src\*.java src\exceptions\*.java
```

### Ejecutar el programa

```powershell
java -cp "bin;lib/*" Main
```

Al iniciar, el sistema intenta cargar `hospital.json`. Al salir (opcion `0`), guarda el estado en `resultados.json`.

### Ejecutar tests

**Desde el menu:** opcion `8. Correr tests`

**Por consola:**

```powershell
java -cp "bin;lib/*" HospitalTest
```

## Menu del sistema

| 1 | Agregar doctor |
| 2 | Buscar doctor (por ID, nombre o apellido) |
| 3 | Ver todos los doctores |
| 4 | Registrar nuevo paciente |
| 5 | Buscar paciente (por DNI) |
| 6 | Ver todos los pacientes |
| 7 | Calcular total de pacientes atendidos |
| 8 | Correr tests unitarios |
| 0 | Salir y guardar en `resultados.json` |

---

## Excepciones propias

`DoctorNoEncontradoException` | Se registra un paciente en un doctor inexistente. 
`LimiteDoctoresException` | Se superan los 10 doctores.
`LimitePacientesException` | Un doctor ya tiene 10 pacientes. 
`DatosInvalidosException` | Datos obligatorios vacios, DNI/ID invalido o duplicado.

---

## Archivos JSON

**`hospital.json`** — datos iniciales al abrir el programa.

**`resultados.json`** — estado final al cerrar (doctores, pacientes y totales actualizados).

El DNI del paciente se usa como clave unica en el JSON y en el sistema.

-----------------------------------------------------------------------
-----------------------------------------------------------------------

Juan Gómez Carrillo
Programación II
Prof. Manuel Adrian Caceres

