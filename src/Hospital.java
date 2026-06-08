import java.util.LinkedHashMap;
import java.util.Map;

import exceptions.DatosInvalidosException;
import exceptions.DoctorNoEncontradoException;
import exceptions.LimiteDoctoresException;
import exceptions.LimitePacientesException;

public class Hospital {
    String nombreSistema;
    String fechaCreacion;
    Map<Integer, Doctor> doctores;

    public Hospital() {
        this.doctores = new LinkedHashMap<>();
    }

    public Hospital(String nombreSistema, String fechaCreacion) {
        this.nombreSistema = nombreSistema;
        this.fechaCreacion = fechaCreacion;
        this.doctores = new LinkedHashMap<>();
    }

    public void agregarDoctor(Doctor doctor) throws LimiteDoctoresException, DatosInvalidosException {
        if (doctores.size() >= 10) {
            throw new LimiteDoctoresException("No se pueden agregar mas de 10 doctores.");
        }

        if (doctores.containsKey(doctor.identificacionMedica)) {
            throw new DatosInvalidosException("Ya existe un doctor con esa identificacion medica.");
        }

        doctores.put(doctor.identificacionMedica, doctor);
    }

    public Doctor buscarDoctor(String busqueda) {
        if (busqueda.matches("\\d+")) {
            int identificacionMedica = Integer.parseInt(busqueda);
            return doctores.get(identificacionMedica);
        }

        return doctores.values()
                .stream()
                .filter(doctor -> doctor.nombre.equalsIgnoreCase(busqueda) || doctor.apellido.equalsIgnoreCase(busqueda))
                .findFirst()
                .orElse(null);
    }

    public Paciente buscarPaciente(int dni) {
        return doctores.values()
                .stream()
                .map(doctor -> doctor.pacientes.get(dni))
                .filter(paciente -> paciente != null)
                .findFirst()
                .orElse(null);
    }

    public void registrarPaciente(int identificacionDoctor, Paciente paciente) throws DoctorNoEncontradoException, LimitePacientesException, DatosInvalidosException {
        Doctor doctor = doctores.get(identificacionDoctor);

        if (doctor == null) {
            throw new DoctorNoEncontradoException("No existe un doctor con esa identificacion medica.");
        }

        if (buscarPaciente(paciente.dni) != null) {
            throw new DatosInvalidosException("Ya existe un paciente con ese DNI.");
        }

        doctor.agregarPaciente(paciente);
    }

    public int calcularTotalPacientes() {
        return doctores.values()
                .stream()
                .mapToInt(doctor -> doctor.pacientes.size())
                .sum();
    }
}
