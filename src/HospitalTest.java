import java.nio.file.Files;
import java.nio.file.Path;

import exceptions.DatosInvalidosException;
import exceptions.DoctorNoEncontradoException;
import exceptions.LimiteDoctoresException;
import exceptions.LimitePacientesException;

public class HospitalTest {
    public static void main(String[] args) throws Exception {
        ejecutarTodos();
    }

    public static void ejecutarTodos() throws Exception {
        testAgregarDoctor();
        testNoPermiteMasDeDiezDoctores();
        testNoPermiteDoctorDuplicado();
        testRegistrarPaciente();
        testNoRegistraPacienteEnDoctorInexistente();
        testNoPermiteMasDeDiezPacientesPorDoctor();
        testCalculaTotalPacientes();
        testGuardarYCargarJson();
        testValidacionDatosDoctor();
        testValidacionDatosPaciente();

        System.out.println("Todos los tests pasaron correctamente.");
    }

    static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    static void verificarExcepcion(AccionConExcepcion accion, Class<? extends Exception> tipoEsperado, String mensaje) {
        try {
            accion.ejecutar();
            throw new AssertionError(mensaje);
        } catch (Exception e) {
            verificar(tipoEsperado.isInstance(e), "Se esperaba " + tipoEsperado.getSimpleName() + " pero se obtuvo " + e.getClass().getSimpleName());
        }
    }

    @FunctionalInterface
    interface AccionConExcepcion {
        void ejecutar() throws Exception;
    }

    static void testAgregarDoctor() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");
        Doctor doctor = new Doctor("Ana", "Perez", 1001, "Clinica");

        hospital.agregarDoctor(doctor);
        verificar(hospital.buscarDoctor("1001") != null, "Deberia encontrar el doctor por identificacion");
    }

    static void testNoPermiteMasDeDiezDoctores() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        for (int i = 1; i <= 10; i++) {
            hospital.agregarDoctor(new Doctor("Doctor", "Numero" + i, i, "Clinica"));
        }

        verificarExcepcion(
                () -> hospital.agregarDoctor(new Doctor("Extra", "Doctor", 11, "Clinica")),
                LimiteDoctoresException.class,
                "No deberia permitir mas de 10 doctores");
    }

    static void testNoPermiteDoctorDuplicado() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        hospital.agregarDoctor(new Doctor("Ana", "Perez", 1001, "Clinica"));

        verificarExcepcion(
                () -> hospital.agregarDoctor(new Doctor("Juan", "Gomez", 1001, "Pediatria")),
                DatosInvalidosException.class,
                "No deberia permitir dos doctores con la misma identificacion");
    }

    static void testRegistrarPaciente() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        hospital.agregarDoctor(new Doctor("Ana", "Perez", 1001, "Clinica"));
        hospital.registrarPaciente(1001, crearPaciente(30000001));

        verificar(hospital.buscarPaciente(30000001) != null, "Deberia encontrar el paciente registrado");
    }

    static void testNoRegistraPacienteEnDoctorInexistente() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        verificarExcepcion(
                () -> hospital.registrarPaciente(9999, crearPaciente(30000001)),
                DoctorNoEncontradoException.class,
                "No deberia registrar pacientes en un doctor inexistente");
    }

    static void testNoPermiteMasDeDiezPacientesPorDoctor() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        hospital.agregarDoctor(new Doctor("Ana", "Perez", 1001, "Clinica"));

        for (int i = 1; i <= 10; i++) {
            hospital.registrarPaciente(1001, crearPaciente(30000000 + i));
        }

        verificarExcepcion(
                () -> hospital.registrarPaciente(1001, crearPaciente(30000011)),
                LimitePacientesException.class,
                "No deberia permitir mas de 10 pacientes por doctor");
    }

    static void testCalculaTotalPacientes() throws Exception {
        Hospital hospital = new Hospital("Test", "2026-05-26");

        hospital.agregarDoctor(new Doctor("Ana", "Perez", 1001, "Clinica"));
        hospital.agregarDoctor(new Doctor("Juan", "Gomez", 1002, "Pediatria"));
        hospital.registrarPaciente(1001, crearPaciente(30000001));
        hospital.registrarPaciente(1001, crearPaciente(30000002));
        hospital.registrarPaciente(1002, crearPaciente(30000003));

        verificar(hospital.calcularTotalPacientes() == 3, "El total de pacientes deberia ser 3");
    }

    static void testGuardarYCargarJson() throws Exception {
        String archivoTest = "hospital-test.json";
        Hospital hospital = new Hospital("Test JSON", "2026-05-26");

        hospital.agregarDoctor(new Doctor("Ana", "Perez", 1001, "Clinica"));
        hospital.registrarPaciente(1001, crearPaciente(30000001));

        JsonManager.guardarHospital(hospital, archivoTest);
        Hospital hospitalCargado = JsonManager.cargarHospital(archivoTest);

        verificar(hospitalCargado != null, "Deberia cargar el hospital desde JSON");
        verificar(hospitalCargado.buscarDoctor("1001") != null, "Deberia cargar el doctor desde JSON");
        verificar(hospitalCargado.buscarPaciente(30000001) != null, "Deberia cargar el paciente desde JSON");
        verificar(hospitalCargado.calcularTotalPacientes() == 1, "Deberia conservar el total de pacientes");

        Files.deleteIfExists(Path.of(archivoTest));
    }

    static void testValidacionDatosDoctor() {
        verificarExcepcion(
                () -> new Doctor("", "Perez", 1001, "Clinica"),
                DatosInvalidosException.class,
                "No deberia permitir un doctor sin nombre");

        verificarExcepcion(
                () -> new Doctor("Ana", "Perez", 0, "Clinica"),
                DatosInvalidosException.class,
                "No deberia permitir una identificacion medica invalida");
    }

    static void testValidacionDatosPaciente() {
        verificarExcepcion(
                () -> new Paciente(0, "Juan", "Gomez", 30, "Masculino", 1122334455, "Calle 1", "Control", "2026-05-26"),
                DatosInvalidosException.class,
                "No deberia permitir un paciente con DNI invalido");

        verificarExcepcion(
                () -> new Paciente(12345678, "", "Gomez", 30, "Masculino", 1122334455, "Calle 1", "Control", "2026-05-26"),
                DatosInvalidosException.class,
                "No deberia permitir un paciente sin nombre");
    }

    static Paciente crearPaciente(int dni) throws DatosInvalidosException {
        return new Paciente(
                dni,
                "Paciente",
                "Test" + dni,
                30,
                "No especifica",
                1100000000,
                "Direccion test",
                "Historia medica test",
                "2026-05-26");
    }
}
