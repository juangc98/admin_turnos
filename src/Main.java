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

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Hospital hospital = JsonManager.cargarHospital("hospital.json");

        GestorHospital gestor = new GestorHospital(scanner, hospital);
        gestor.iniciar();
        gestor.ejecutar();

        JsonManager.guardarHospital(gestor.getHospital(), "resultados.json");
        System.out.println("Resultados guardados en resultados.json.");
        scanner.close();
    }
}
