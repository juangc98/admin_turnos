/*

Deben desarrollar un sistema para gestionar doctores y pacientes en un hospital. 
El sistema debe permitir la administración de hasta un máximo de 10 doctores y debe incluir la siguiente información:

Nombre del Sistema y Fecha de Creación: El sistema debe tener un nombre y se debe registrar la fecha de creación del sistema.

Datos de los Doctores: Para cada doctor, se deben registrar los siguientes datos:
    Nombre: El nombre del doctor, que es obligatorio.
    Número de Identificación Médica: Un número de identificación único para cada doctor, que también es obligatorio.
    Especialidad: La especialidad médica del doctor.
    Pacientes Atendidos: Cada doctor tiene la responsabilidad de atender a una lista de pacientes. Si se intenta agregar un paciente a un doctor inexistente, el sistema debe emitir un error. Cada paciente debe contener la siguiente información:

PACIENTES
    ID del Paciente: Un identificador único para el paciente.
    Nombre del Paciente: El nombre del paciente, que es obligatorio.
    Fecha de Ingreso: La fecha en que el paciente fue admitido en el hospital.

Límite de Pacientes Atendidos: Cada doctor puede atender a un máximo de 10 pacientes. 
El sistema debe controlar este límite.

Debes diseñar un programa que permita a los usuarios agregar doctores, registrar pacientes atendidos por los doctores y calcular la cantidad total de pacientes atendidos por todos los doctores.

*/

import java.time.LocalDate;
import java.util.Scanner;

import exceptions.DatosInvalidosException;
import exceptions.DoctorNoEncontradoException;
import exceptions.LimiteDoctoresException;
import exceptions.LimitePacientesException;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hospital hospital = JsonManager.cargarHospital("hospital.json");
        System.out.println();
        System.out.println("=== Sistema de Gestion Hospitalaria ===");
        System.out.println();

        if (hospital == null) {
            String nombreSistema = leerTextoObligatorio(scanner, "Nombre del sistema: ");
            hospital = new Hospital(nombreSistema, LocalDate.now().toString());
            System.out.println("Fecha de creacion: " + hospital.fechaCreacion);
        } else {
            System.out.println("Datos cargados desde hospital.json.");
            System.out.println("Fecha de creacion: " + hospital.fechaCreacion);
            System.out.println("--------------------------------");
            System.out.println();
        }

        int opcion;

        do {
            mostrarMenu(hospital);
            opcion = leerNroEntero(scanner, "Seleccione una opcion: ");
            // agregar espacio y lineas
            System.out.println();
            System.out.println("--------------------------------");
            System.out.println();

            switch (opcion) {
                case 1:
                    agregarDoctor(scanner, hospital);
                    break;
                case 2:
                    buscarDoctor(scanner, hospital);
                    break;
                case 3:
                    verTodosDoctores(hospital);
                    break;
                case 4:
                    registrarPaciente(scanner, hospital);
                    break;
                case 5:
                    buscarPaciente(scanner, hospital);
                    break;
                case 6:
                    verTodosPacientes(hospital);
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
        JsonManager.guardarHospital(hospital, "resultados.json");
        System.out.println("Resultados guardados en resultados.json.");
        scanner.close();
    }

    static void mostrarMenu(Hospital hospital) {
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

    static void correrTests() {
        System.out.println("Ejecutando tests unitarios...");

        try {
            HospitalTest.ejecutarTodos();
        } catch (AssertionError e) {
            System.out.println("Test fallido: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al ejecutar tests: " + e.getMessage());
        }
    }

    static void buscarDoctor(Scanner scanner, Hospital hospital) {
        String busqueda = leerTextoObligatorio(scanner, "ID, nombre o apellido del doctor: ");
        Doctor doctor = hospital.buscarDoctor(busqueda);

        if (doctor != null) {
            System.out.println("Doctor encontrado: " + doctor.nombre + " " + doctor.apellido + " - " + doctor.identificacionMedica + " - " + doctor.especialidad);
        } else {
            System.out.println("No se encontro un doctor con esos datos.");
        }
    }

    static void agregarDoctor(Scanner scanner, Hospital hospital) {
        try {
            String nombre = leerTextoObligatorio(scanner, "Nombre del doctor: ");
            String apellido = leerTextoObligatorio(scanner, "Apellido del doctor: ");
            int identificacion = leerNroEntero(scanner, "Identificacion medica: ");
            String especialidad = leerTextoObligatorio(scanner, "Especialidad: ");

            Doctor doctor = new Doctor(nombre, apellido, identificacion, especialidad);
            hospital.agregarDoctor(doctor);
            System.out.println("Doctor agregado correctamente.");
        } catch (DatosInvalidosException | LimiteDoctoresException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void verTodosDoctores(Hospital hospital) {
        hospital.doctores.values().forEach(doctor -> {
            System.out.println("Doctor: " + doctor.nombre + " " + doctor.apellido + " - " + doctor.identificacionMedica + " - " + doctor.especialidad);
        });
    }

    static void verTodosPacientes(Hospital hospital) {
        hospital.doctores.values().forEach(doctor -> {
            doctor.pacientes.values().forEach(paciente -> {
                System.out.println("Paciente: " + paciente.nombre + " " + paciente.apellido + " - DNI: " + paciente.dni);
            });
        });
    }

    static void buscarPaciente(Scanner scanner, Hospital hospital) {
        int dni = leerNroEntero(scanner, "DNI del paciente: ");
        Paciente paciente = hospital.buscarPaciente(dni);

        if (paciente != null) {
            System.out.println("Paciente encontrado: " + paciente.nombre + " " + paciente.apellido + " - DNI: " + paciente.dni);
        } else {
            System.out.println("No se encontro un paciente con ese DNI.");
        }
    }

    static void registrarPaciente(Scanner scanner, Hospital hospital) {
        try {
            int identificacionDoctor = leerNroEntero(scanner, "Identificacion medica del Dr. a cargo: ");
            int dniPaciente = leerNroEntero(scanner, "DNI del paciente: ");
            String nombrePaciente = leerTextoObligatorio(scanner, "Nombre: ");
            String apellidoPaciente = leerTextoObligatorio(scanner, "Apellido: ");
            int edadPaciente = leerNroEntero(scanner, "Edad: ");
            System.out.print("Genero (m/f): ");
            String generoPaciente = scanner.nextLine();
            int telefonoPaciente = leerNroEntero(scanner, "Tel.: ");
            String direccionPaciente = leerTextoObligatorio(scanner, "Direccion: ");
            String historiaMedicaPaciente = leerTextoObligatorio(scanner, "Historia medica: ");
            String fechaIngreso = LocalDate.now().toString();

            Paciente paciente = new Paciente(dniPaciente, nombrePaciente, apellidoPaciente, edadPaciente, generoPaciente, telefonoPaciente, direccionPaciente, historiaMedicaPaciente, fechaIngreso);
            hospital.registrarPaciente(identificacionDoctor, paciente);
            System.out.println("Paciente registrado correctamente.");
        } catch (DatosInvalidosException | DoctorNoEncontradoException | LimitePacientesException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static String leerTextoObligatorio(Scanner scanner, String mensaje) {
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

    static int leerNroEntero(Scanner scanner, String mensaje) {
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
