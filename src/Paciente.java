import exceptions.DatosInvalidosException;

public class Paciente {
    int dni;
    String nombre;
    String apellido;
    int edad;
    String genero;
    int telefono;
    String direccion;
    String historiaMedica;
    String fechaIngreso;

    public Paciente() {
    }

    public Paciente(int dni, String nombre, String apellido, int edad, String genero, int telefono, String direccion, String historiaMedica, String fechaIngreso) throws DatosInvalidosException {
        validarId(dni, "El DNI del paciente debe ser mayor a 0.");
        validarTexto(nombre, "El nombre del paciente es obligatorio.");
        validarTexto(apellido, "El apellido del paciente es obligatorio.");
        validarTexto(fechaIngreso, "La fecha de ingreso es obligatoria.");

        this.dni = dni;
        this.nombre = nombre.trim();
        this.apellido = apellido.trim();
        this.edad = edad;
        this.genero = normalizarGenero(genero);
        this.telefono = telefono;
        this.direccion = direccion;
        this.historiaMedica = historiaMedica;
        this.fechaIngreso = fechaIngreso.trim();
    }

    static String normalizarGenero(String genero) {
        if (genero == null) {
            return "-";
        }

        String valor = genero.trim().toLowerCase();

        if (valor.equals("f") || valor.equals("m")) {
            return valor;
        }

        return "-";
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
