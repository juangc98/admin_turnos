import java.time.LocalDate;
import java.util.Scanner;

import exceptions.DatosInvalidosException;
import exceptions.DoctorNoEncontradoException;
import exceptions.LimiteDoctoresException;
import exceptions.LimitePacientesException;

public class GestorHospital {
    private final Scanner scanner;
    private Hospital hospital;

    public GestorHospital(Scanner scanner, Hospital hospital) {
        this.scanner = scanner;
        this.hospital = hospital;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void iniciar() {
        System.out.println();
        System.out.println("=== Sistema de Gestion Hospitalaria ===");
        System.out.println();

        if (hospital == null) {
            String nombreSistema = leerTextoObligatorio("Nombre del sistema: ");
            hospital = new Hospital(nombreSistema, LocalDate.now().toString());
            System.out.println("Fecha de creacion: " + hospital.fechaCreacion);
        } else {
            System.out.println("Datos cargados desde hospital.json.");
            System.out.println("Fecha de creacion: " + hospital.fechaCreacion);
            System.out.println("--------------------------------");
            System.out.println();
        }
    }

    public void ejecutar() {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerNroEntero("Seleccione una opcion: ");
            System.out.println();
            System.out.println("--------------------------------");
            System.out.println();

            switch (opcion) {
                case 1:
                    agregarDoctor();
                    break;
                case 2:
                    buscarDoctor();
                    break;
                case 3:
                    verTodosDoctores();
                    break;
                case 4:
                    registrarPaciente();
                    break;
                case 5:
                    buscarPaciente();
                    break;
                case 6:
                    verTodosPacientes();
                    break;
                case 7:
                    System.out.println("Total de pacientes atendidos: " + hospital.calcularTotalPacientes());
                    break;
                case 8:
                    correrTests();
                    break;
                case 0:
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
                    break;
            }
        } while (opcion != 0);

        System.out.println("--------------------------------");
        System.out.println();
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("=== " + hospital.nombreSistema + " ===");
        System.out.println("--------------------------------");
        System.out.println("1. Agregar doctor");
        System.out.println("2. Buscar doctor");
        System.out.println("3. Ver todos los doctores");
        System.out.println("4. Registrar nuevo paciente");
        System.out.println("5. Buscar paciente");
        System.out.println("6. Ver todos los pacientes");
        System.out.println("7. Calcular total de pacientes atendidos");
        System.out.println("8. Correr tests");
        System.out.println("0. Salir");
    }

    private void correrTests() {
        System.out.println("Ejecutando tests unitarios...");

        try {
            HospitalTest.ejecutarTodos();
        } catch (AssertionError e) {
            System.out.println("Test fallido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al ejecutar tests: " + e.getMessage());
        }
    }

    private void buscarDoctor() {
        String busqueda = leerTextoObligatorio("ID, nombre o apellido del doctor: ");
        Doctor doctor = hospital.buscarDoctor(busqueda);

        if (doctor != null) {
            System.out.println("Doctor encontrado: " + doctor.nombre + " " + doctor.apellido + " - " + doctor.identificacionMedica + " - " + doctor.especialidad);
        } else {
            System.out.println("No se encontro un doctor con esos datos.");
        }
    }

    private void agregarDoctor() {
        try {
            String nombre = leerTextoObligatorio("Nombre del doctor: ");
            String apellido = leerTextoObligatorio("Apellido: ");
            int identificacion = leerNroEntero("Identificacion medica: ");
            String especialidad = leerTextoObligatorio("Especialidad: ");

            Doctor doctor = new Doctor(nombre, apellido, identificacion, especialidad);
            hospital.agregarDoctor(doctor);
            System.out.println("Doctor agregado correctamente.");
        } catch (DatosInvalidosException | LimiteDoctoresException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void verTodosDoctores() {
        hospital.doctores.values().forEach(doctor -> {
            System.out.println("Dr. " + doctor.nombre + " " + doctor.apellido + " - " + doctor.identificacionMedica + " - " + doctor.especialidad);
        });
    }

    private void verTodosPacientes() {
        hospital.doctores.values().forEach(doctor -> {
            doctor.pacientes.values().forEach(paciente -> {
                System.out.println("Paciente: " + paciente.nombre + " " + paciente.apellido + " - DNI: " + paciente.dni);
            });
        });
    }

    private void buscarPaciente() {
        int dni = leerNroEntero("DNI del paciente: ");
        Paciente paciente = hospital.buscarPaciente(dni);

        if (paciente != null) {
            System.out.println("Paciente encontrado: " + paciente.nombre + " " + paciente.apellido + " - DNI: " + paciente.dni);
        } else {
            System.out.println("No se encontro un paciente con ese DNI.");
        }
    }

    private void registrarPaciente() {
        try {
            verTodosDoctores();
            int identificacionDoctor = leerNroEntero("Identificacion medica del Dr. a cargo: ");
            int dniPaciente = leerNroEntero("DNI del paciente: ");
            String nombrePaciente = leerTextoObligatorio("Nombre: ");
            String apellidoPaciente = leerTextoObligatorio("Apellido: ");
            int edadPaciente = leerNroEntero("Edad: ");
            System.out.print("Genero (m/f): ");
            String generoPaciente = scanner.nextLine();
            int telefonoPaciente = leerNroEntero("Tel.: ");
            String direccionPaciente = leerTextoObligatorio("Direccion: ");
            String historiaMedicaPaciente = leerTextoObligatorio("Historia medica: ");
            String fechaIngreso = LocalDate.now().toString();

            Paciente paciente = new Paciente(dniPaciente, nombrePaciente, apellidoPaciente, edadPaciente, generoPaciente, telefonoPaciente, direccionPaciente, historiaMedicaPaciente, fechaIngreso);
            hospital.registrarPaciente(identificacionDoctor, paciente);
            System.out.println("Paciente registrado correctamente.");
        } catch (DatosInvalidosException | DoctorNoEncontradoException | LimitePacientesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String leerTextoObligatorio(String mensaje) {
        String texto;

        do {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();

            if (texto.isEmpty()) {
                System.out.println("Este dato es obligatorio.");
            }
        } while (texto.isEmpty());

        return texto;
    }

    private int leerNroEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        }
    }
}
