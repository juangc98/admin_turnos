import java.util.LinkedHashMap;
import java.util.Map;

import exceptions.DatosInvalidosException;
import exceptions.LimitePacientesException;

public class Doctor {
    String nombre;
    String apellido;
    int identificacionMedica;
    String especialidad;
    Map<Integer, Paciente> pacientes;

    public Doctor() {
        this.pacientes = new LinkedHashMap<>();
    }

    public Doctor(String nombre, String apellido, int identificacionMedica, String especialidad) throws DatosInvalidosException {
        validarTexto(nombre, "El nombre del doctor es obligatorio.");
        validarTexto(apellido, "El apellido del doctor es obligatorio.");
        validarTexto(especialidad, "La especialidad del doctor es obligatoria.");
        validarId(identificacionMedica, "La identificacion medica debe ser mayor a 0.");

        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.identificacionMedica = identificacionMedica;
        this.especialidad = especialidad.trim();
        this.pacientes = new LinkedHashMap<>();
    }

    public void agregarPaciente(Paciente paciente) throws LimitePacientesException, DatosInvalidosException {
        if (pacientes.size() >= 10) {
            throw new LimitePacientesException("El doctor ya tiene 10 pacientes atendidos.");
        }

        if (pacientes.containsKey(paciente.dni)) {
            throw new DatosInvalidosException("El doctor ya tiene un paciente con ese DNI.");
        }

        pacientes.put(paciente.dni, paciente);
    }

    private static void validarTexto(String valor, String mensaje) throws DatosInvalidosException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatosInvalidosException(mensaje);
        }
    }

    private static void validarId(int id, String mensaje) throws DatosInvalidosException {
        if (id <= 0) {
            throw new DatosInvalidosException(mensaje);
        }
    }
}
