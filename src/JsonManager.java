import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonManager {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Hospital cargarHospital(String rutaArchivo) {
        try {
            if (!Files.exists(Path.of(rutaArchivo))) {
                return null;
            }

            FileReader reader = new FileReader(rutaArchivo);
            Hospital hospital = GSON.fromJson(reader, Hospital.class);
            reader.close();

            prepararMapas(hospital);
            return hospital;
        } catch (Exception e) {
            System.out.println("No se pudo cargar el archivo JSON.");
            return null;
        }
    }

    public static void guardarHospital(Hospital hospital, String rutaArchivo) {
        try {
            FileWriter writer = new FileWriter(rutaArchivo);
            GSON.toJson(hospital, writer);
            writer.close();
        } catch (Exception e) {
            System.out.println("No se pudo guardar el archivo JSON.");
        }
    }

    private static void prepararMapas(Hospital hospital) {
        if (hospital == null) {
            return;
        }

        if (hospital.doctores == null) {
            hospital.doctores = new LinkedHashMap<>();
        }

        hospital.doctores.values().forEach(doctor -> {
            if (doctor.pacientes == null) {
                doctor.pacientes = new LinkedHashMap<>();
                return;
            }

            Map<Integer, Paciente> pacientesPorDni = new LinkedHashMap<>();
            doctor.pacientes.values().forEach(paciente -> {
                paciente.genero = Paciente.normalizarGenero(paciente.genero);
                pacientesPorDni.put(paciente.dni, paciente);
            });
            doctor.pacientes = pacientesPorDni;
        });
    }
}
