import java.util.Formatter;
import java.util.Random;

public class PROYECTO {

    private static final String[] NOMBRES = {"Pablo", "Eimer", "Brayan", "Manuel", "Ricardo", "Nayeli", "Diego", "Juan", "Paula", "Marco", "Carlos", "Irene", "Pedro"};
    private static final String[] APELLIDOS = {"Garcia", "Martinez", "Lopez", "Hernandez", "Gonzalez", "Perez", "Sanchez", "Ramirez", "Torres", "Flores"};

    public static void main(String[] args) {
        String[][] datosEntrada = new String[100][3];
        String[][] datosSalidaQuimica = new String[80][7];
        String[][] datosSalidaFisiorehabilitacion = new String[200][7];
        String[][] datosSalidaMedicina = new String[80][7];

        generarDatosEntrada(datosEntrada);
        calcularAdmisiones(datosEntrada, datosSalidaQuimica, datosSalidaFisiorehabilitacion, datosSalidaMedicina);
        escribirResultados("resultados_quimica.csv", datosSalidaQuimica);
        escribirResultados("resultados_fisiorehabilitacion.csv", datosSalidaFisiorehabilitacion);
        escribirResultados("resultados_medicina.csv", datosSalidaMedicina);

        System.out.println("Resultados de Admisiones - Carrera de Quimica:");
        imprimirResultados(datosSalidaQuimica);
        System.out.println("\nResultados de Admisiones - Carrera de Fisioterapia y Rehabilitacion:");
        imprimirResultados(datosSalidaFisiorehabilitacion);
        System.out.println("\nResultados de Admisiones - Carrera de Medicina:");
        imprimirResultados(datosSalidaMedicina);
    }

    public static void generarDatosEntrada(String[][] datosEntrada) {
        Random rand = new Random();
        try {
            for (int i = 0; i < datosEntrada.length; i++) {
                String nombre = obtenerNombreAleatorio();
                String apellido = obtenerApellidoAleatorio();
                String carrera = obtenerCarreraAleatoria();
                int puntaje = rand.nextInt(101); 

                datosEntrada[i][0] = nombre + " " + apellido;
                datosEntrada[i][1] = carrera;
                datosEntrada[i][2] = String.valueOf(puntaje);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void calcularAdmisiones(String[][] datosEntrada, String[][] datosSalidaQuimica, String[][] datosSalidaFisiorehabilitacion, String[][] datosSalidaMedicina) {
        int contadorQuimica = 0;
        int contadorFisiorehabilitacion = 0;
        int contadorMedicina = 0;
        for (int i = 0; i < datosEntrada.length; i++) {
            String nombre = datosEntrada[i][0];
            String carrera = datosEntrada[i][1];
            int puntaje = Integer.parseInt(datosEntrada[i][2]);
            String admitido = "No";
            String beca = "No";
            double costoCarrera = 0;

            switch (carrera) {
                case "Quimica":
                    costoCarrera = 1500;
                    if (puntaje >= 80 && contadorQuimica < 80) {
                        datosSalidaQuimica[contadorQuimica][0] = nombre;
                        datosSalidaQuimica[contadorQuimica][1] = carrera;
                        datosSalidaQuimica[contadorQuimica][2] = String.valueOf(puntaje);
                        datosSalidaQuimica[contadorQuimica][3] = "Si"; 
                        datosSalidaQuimica[contadorQuimica][4] = "0";
                        datosSalidaQuimica[contadorQuimica][5] = String.valueOf(puntaje);
                        if (puntaje >= 95) {
                            datosSalidaQuimica[contadorQuimica][6] = "Si"; // Si el puntaje es mayor o igual a 95, asignamos beca
                            beca = "Si";
                        } else {
                            datosSalidaQuimica[contadorQuimica][6] = "No"; // Si no califica para la beca, asignamos "No"
                        }
                        contadorQuimica++;
                    }
                    break;
                case "Fisiorehabilitacion":
                    costoCarrera = 1250;
                    if (puntaje >= 90) {
                        contadorFisiorehabilitacion++;
                        admitido = "Si"; 
                    }
                    break;
                case "Medicina":
                    costoCarrera = 1700;
                    if ((puntaje >= 85 && contadorMedicina < 80) || puntaje >= 95) {
                        contadorMedicina++;
                        admitido = "Si";
                        if (puntaje >= 95) {
                            beca = "Si"; // Si el puntaje es mayor o igual a 95, asignamos beca
                        }
                    }
                    break;
            }

            // Verificar si el padre o madre es profesor de la UTPL
            if (esPadreMadreProfesor()) {
                costoCarrera *= 0.75; // Aplicar descuento del 25%
            }

            if (admitido.equals("Si")) {
                int puntajeAdicional = calcularPuntajeAdicional(nombre, carrera, puntaje);
                int puntajeTotal = Math.min(puntaje + puntajeAdicional, 100); 
                String[] fila = {nombre, carrera, String.valueOf(puntaje), admitido, String.valueOf(puntajeAdicional), String.valueOf(puntajeTotal), beca};
                switch (carrera) {
                    case "Quimica":
                        datosSalidaQuimica[contadorQuimica - 1] = fila;
                        break;
                    case "Fisiorehabilitacion":
                        datosSalidaFisiorehabilitacion[contadorFisiorehabilitacion - 1] = fila;
                        break;
                    case "Medicina":
                        datosSalidaMedicina[contadorMedicina - 1] = fila;
                        break;
                }
            }
        }
    }

    public static int calcularPuntajeAdicional(String nombre, String carrera, int puntaje) {
        Random rand = new Random();
        int puntajeAdicional = 0;
        if (puntaje < 95) {
            switch (carrera) {
                case "Medicina":
                    if (esAbanderado()) {
                        puntajeAdicional += 5;
                    }
                    if (esAfines(nombre)) {
                        puntajeAdicional += 2;
                    }
                    if (esCapacidadEspecial()) {
                        puntajeAdicional += 1;
                    }
                    break;
            }
        }
        return puntajeAdicional;
    }

    public static boolean esAbanderado() {
        Random rand = new Random();
        return rand.nextInt(100) < 20; 
    }

    public static boolean esAfines(String nombre) {
        Random rand = new Random();
        return rand.nextInt(100) < 10; 
    }

    public static boolean esCapacidadEspecial() {
        Random rand = new Random();
        return rand.nextInt(100) < 35; 
    }

    public static boolean esPadreMadreProfesor() {
        Random rand = new Random();
        return rand.nextInt(100) < 5; // Supongamos que el 5% de los estudiantes tienen un padre o madre profesor de la UTPL
    }

    public static String obtenerNombreAleatorio() {
        Random rand = new Random();
        return NOMBRES[rand.nextInt(NOMBRES.length)];
    }

    public static String obtenerApellidoAleatorio() {
        Random rand = new Random();
        return APELLIDOS[rand.nextInt(APELLIDOS.length)];
    }

    public static String obtenerCarreraAleatoria() {
        String[] carreras = {"Quimica", "Fisiorehabilitacion", "Medicina"};
        Random rand = new Random();
        return carreras[rand.nextInt(carreras.length)];
    }

    public static void escribirResultados(String nombreArchivo, String[][] datosSalida) {
        try {
            Formatter escritura = new Formatter(nombreArchivo);
            escritura.format("Nombre;Carrera;Puntaje;Admitido;PuntajeAdicional;PuntajeTotal;SolicitudBeca\n");
            for (String[] fila : datosSalida) {
                if (fila != null && fila[3] != null) {
                    for (int i = 0; i < fila.length; i++) {
                        if (fila[i] != null) {
                            escritura.format("%s;", fila[i]);
                        } else {
                            escritura.format(";");
                        }
                    }
                    escritura.format("\n");
                }
            }
            escritura.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void imprimirResultados(String[][] datosSalida) {
        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("| Nombre            | Carrera           | Puntaje | Admitido | Puntaje Adicional  | Puntaje Total | SolicitudBeca |");
        System.out.println("----------------------------------------------------------------------------------------------");
        for (String[] fila : datosSalida) {
            if (fila != null && fila[3] != null) {
                System.out.printf("| %-17s | %-17s | %-7s | %-8s | %-18s | %-13s | %-4s |%n", fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]);
            }
        }
        System.out.println("----------------------------------------------------------------------------------------------");
    }
}
/*
run:
Resultados de Admisiones - Carrera de Quimica:
----------------------------------------------------------------------------------------------
| Nombre            | Carrera           | Puntaje | Admitido | Puntaje Adicional  | Puntaje Total | SolicitudBeca |
----------------------------------------------------------------------------------------------
| Irene Perez       | Quimica           | 97      | Si       | 0                  | 97            | Si   |
| Manuel Flores     | Quimica           | 93      | Si       | 0                  | 93            | No   |
| Diego Perez       | Quimica           | 97      | Si       | 0                  | 97            | Si   |
| Carlos Sanchez    | Quimica           | 83      | Si       | 0                  | 83            | No   |
| Brayan Ramirez    | Quimica           | 93      | Si       | 0                  | 93            | No   |
| Carlos Perez      | Quimica           | 86      | Si       | 0                  | 86            | No   |
| Juan Hernandez    | Quimica           | 90      | Si       | 0                  | 90            | No   |
| Pablo Gonzalez    | Quimica           | 99      | Si       | 0                  | 99            | Si   |
| Pedro Perez       | Quimica           | 86      | Si       | 0                  | 86            | No   |
| Irene Ramirez     | Quimica           | 82      | Si       | 0                  | 82            | No   |
| Irene Flores      | Quimica           | 87      | Si       | 0                  | 87            | No   |
| Eimer Torres      | Quimica           | 82      | Si       | 0                  | 82            | No   |
| Pedro Perez       | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Carlos Flores     | Quimica           | 94      | Si       | 0                  | 94            | No   |
| Nayeli Lopez      | Quimica           | 95      | Si       | 0                  | 95            | Si   |
| Manuel Martinez   | Quimica           | 99      | Si       | 0                  | 99            | Si   |
| Ricardo Ramirez   | Quimica           | 85      | Si       | 0                  | 85            | No   |
| Eimer Ramirez     | Quimica           | 81      | Si       | 0                  | 81            | No   |
| Marco Hernandez   | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Diego Ramirez     | Quimica           | 93      | Si       | 0                  | 93            | No   |
| Pedro Flores      | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Carlos Lopez      | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Brayan Martinez   | Quimica           | 83      | Si       | 0                  | 83            | No   |
| Brayan Hernandez  | Quimica           | 86      | Si       | 0                  | 86            | No   |
| Carlos Gonzalez   | Quimica           | 94      | Si       | 0                  | 94            | No   |
| Pedro Sanchez     | Quimica           | 85      | Si       | 0                  | 85            | No   |
| Nayeli Martinez   | Quimica           | 88      | Si       | 0                  | 88            | No   |
| Paula Flores      | Quimica           | 90      | Si       | 0                  | 90            | No   |
| Juan Torres       | Quimica           | 94      | Si       | 0                  | 94            | No   |
| Pedro Martinez    | Quimica           | 98      | Si       | 0                  | 98            | Si   |
| Nayeli Torres     | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Ricardo Sanchez   | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Diego Martinez    | Quimica           | 81      | Si       | 0                  | 81            | No   |
| Ricardo Hernandez | Quimica           | 90      | Si       | 0                  | 90            | No   |
| Brayan Lopez      | Quimica           | 85      | Si       | 0                  | 85            | No   |
| Carlos Gonzalez   | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Pedro Ramirez     | Quimica           | 94      | Si       | 0                  | 94            | No   |
| Juan Martinez     | Quimica           | 82      | Si       | 0                  | 82            | No   |
| Marco Sanchez     | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Marco Flores      | Quimica           | 99      | Si       | 0                  | 99            | Si   |
| Pedro Hernandez   | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Carlos Martinez   | Quimica           | 82      | Si       | 0                  | 82            | No   |
| Ricardo Hernandez | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Manuel Martinez   | Quimica           | 99      | Si       | 0                  | 99            | Si   |
| Juan Sanchez      | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Juan Perez        | Quimica           | 99      | Si       | 0                  | 99            | Si   |
| Paula Gonzalez    | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Brayan Perez      | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Pedro Flores      | Quimica           | 90      | Si       | 0                  | 90            | No   |
| Carlos Gonzalez   | Quimica           | 81      | Si       | 0                  | 81            | No   |
| Juan Lopez        | Quimica           | 87      | Si       | 0                  | 87            | No   |
| Diego Ramirez     | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Marco Ramirez     | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Diego Hernandez   | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Diego Torres      | Quimica           | 86      | Si       | 0                  | 86            | No   |
| Carlos Flores     | Quimica           | 87      | Si       | 0                  | 87            | No   |
| Paula Torres      | Quimica           | 98      | Si       | 0                  | 98            | Si   |
| Carlos Sanchez    | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Eimer Martinez    | Quimica           | 97      | Si       | 0                  | 97            | Si   |
| Marco Hernandez   | Quimica           | 81      | Si       | 0                  | 81            | No   |
| Nayeli Hernandez  | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Ricardo Ramirez   | Quimica           | 80      | Si       | 0                  | 80            | No   |
| Nayeli Perez      | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Eimer Perez       | Quimica           | 100     | Si       | 0                  | 100           | Si   |
| Diego Flores      | Quimica           | 87      | Si       | 0                  | 87            | No   |
| Irene Flores      | Quimica           | 80      | Si       | 0                  | 80            | No   |
| Irene Lopez       | Quimica           | 98      | Si       | 0                  | 98            | Si   |
| Irene Hernandez   | Quimica           | 89      | Si       | 0                  | 89            | No   |
| Nayeli Lopez      | Quimica           | 97      | Si       | 0                  | 97            | Si   |
| Marco Garcia      | Quimica           | 80      | Si       | 0                  | 80            | No   |
| Pedro Ramirez     | Quimica           | 96      | Si       | 0                  | 96            | Si   |
| Brayan Lopez      | Quimica           | 92      | Si       | 0                  | 92            | No   |
| Paula Martinez    | Quimica           | 91      | Si       | 0                  | 91            | No   |
| Carlos Lopez      | Quimica           | 80      | Si       | 0                  | 80            | No   |
| Paula Garcia      | Quimica           | 89      | Si       | 0                  | 89            | No   |
----------------------------------------------------------------------------------------------

Resultados de Admisiones - Carrera de Fisioterapia y Rehabilitacion:
----------------------------------------------------------------------------------------------
| Nombre            | Carrera           | Puntaje | Admitido | Puntaje Adicional  | Puntaje Total | SolicitudBeca |
----------------------------------------------------------------------------------------------
| Carlos Garcia     | Fisiorehabilitacion | 96      | Si       | 0                  | 96            | No   |
| Ricardo Perez     | Fisiorehabilitacion | 93      | Si       | 0                  | 93            | No   |
| Nayeli Flores     | Fisiorehabilitacion | 90      | Si       | 0                  | 90            | No   |
| Irene Sanchez     | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Pablo Sanchez     | Fisiorehabilitacion | 95      | Si       | 0                  | 95            | No   |
| Manuel Flores     | Fisiorehabilitacion | 90      | Si       | 0                  | 90            | No   |
| Pedro Torres      | Fisiorehabilitacion | 95      | Si       | 0                  | 95            | No   |
| Diego Lopez       | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Pablo Gonzalez    | Fisiorehabilitacion | 95      | Si       | 0                  | 95            | No   |
| Ricardo Lopez     | Fisiorehabilitacion | 90      | Si       | 0                  | 90            | No   |
| Brayan Sanchez    | Fisiorehabilitacion | 93      | Si       | 0                  | 93            | No   |
| Carlos Torres     | Fisiorehabilitacion | 99      | Si       | 0                  | 99            | No   |
| Pedro Lopez       | Fisiorehabilitacion | 90      | Si       | 0                  | 90            | No   |
| Pablo Perez       | Fisiorehabilitacion | 93      | Si       | 0                  | 93            | No   |
| Juan Perez        | Fisiorehabilitacion | 96      | Si       | 0                  | 96            | No   |
| Eimer Torres      | Fisiorehabilitacion | 96      | Si       | 0                  | 96            | No   |
| Irene Lopez       | Fisiorehabilitacion | 99      | Si       | 0                  | 99            | No   |
| Irene Ramirez     | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Juan Ramirez      | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Paula Ramirez     | Fisiorehabilitacion | 98      | Si       | 0                  | 98            | No   |
| Manuel Sanchez    | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Eimer Sanchez     | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Nayeli Martinez   | Fisiorehabilitacion | 94      | Si       | 0                  | 94            | No   |
| Pablo Martinez    | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Diego Sanchez     | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Eimer Sanchez     | Fisiorehabilitacion | 100     | Si       | 0                  | 100           | No   |
| Juan Perez        | Fisiorehabilitacion | 94      | Si       | 0                  | 94            | No   |
| Eimer Martinez    | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Eimer Sanchez     | Fisiorehabilitacion | 90      | Si       | 0                  | 90            | No   |
| Pablo Perez       | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Eimer Garcia      | Fisiorehabilitacion | 97      | Si       | 0                  | 97            | No   |
| Ricardo Ramirez   | Fisiorehabilitacion | 96      | Si       | 0                  | 96            | No   |
| Pedro Martinez    | Fisiorehabilitacion | 98      | Si       | 0                  | 98            | No   |
| Ricardo Hernandez | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
| Pedro Sanchez     | Fisiorehabilitacion | 95      | Si       | 0                  | 95            | No   |
| Nayeli Flores     | Fisiorehabilitacion | 92      | Si       | 0                  | 92            | No   |
----------------------------------------------------------------------------------------------

Resultados de Admisiones - Carrera de Medicina:
----------------------------------------------------------------------------------------------
| Nombre            | Carrera           | Puntaje | Admitido | Puntaje Adicional  | Puntaje Total | SolicitudBeca |
----------------------------------------------------------------------------------------------
| Paula Garcia      | Medicina          | 98      | Si       | 0                  | 98            | Si   |
| Manuel Sanchez    | Medicina          | 87      | Si       | 1                  | 88            | No   |
| Diego Hernandez   | Medicina          | 87      | Si       | 0                  | 87            | No   |
| Pablo Flores      | Medicina          | 100     | Si       | 0                  | 100           | Si   |
| Juan Garcia       | Medicina          | 95      | Si       | 0                  | 95            | Si   |
| Ricardo Garcia    | Medicina          | 87      | Si       | 0                  | 87            | No   |
| Irene Sanchez     | Medicina          | 98      | Si       | 0                  | 98            | Si   |
| Brayan Lopez      | Medicina          | 100     | Si       | 0                  | 100           | Si   |
| Diego Sanchez     | Medicina          | 93      | Si       | 0                  | 93            | No   |
| Eimer Perez       | Medicina          | 98      | Si       | 0                  | 98            | Si   |
| Paula Sanchez     | Medicina          | 91      | Si       | 0                  | 91            | No   |
| Brayan Garcia     | Medicina          | 88      | Si       | 1                  | 89            | No   |
| Ricardo Hernandez | Medicina          | 87      | Si       | 0                  | 87            | No   |
| Eimer Ramirez     | Medicina          | 86      | Si       | 0                  | 86            | No   |
| Pablo Gonzalez    | Medicina          | 88      | Si       | 0                  | 88            | No   |
| Pedro Flores      | Medicina          | 91      | Si       | 5                  | 96            | No   |
| Marco Garcia      | Medicina          | 100     | Si       | 0                  | 100           | Si   |
| Marco Sanchez     | Medicina          | 96      | Si       | 0                  | 96            | Si   |
| Pedro Flores      | Medicina          | 91      | Si       | 0                  | 91            | No   |
| Paula Torres      | Medicina          | 94      | Si       | 3                  | 97            | No   |
| Marco Flores      | Medicina          | 91      | Si       | 1                  | 92            | No   |
| Diego Gonzalez    | Medicina          | 93      | Si       | 5                  | 98            | No   |
| Nayeli Ramirez    | Medicina          | 86      | Si       | 5                  | 91            | No   |
| Marco Lopez       | Medicina          | 89      | Si       | 1                  | 90            | No   |
| Nayeli Gonzalez   | Medicina          | 97      | Si       | 0                  | 97            | Si   |
| Marco Torres      | Medicina          | 95      | Si       | 0                  | 95            | Si   |
| Irene Gonzalez    | Medicina          | 92      | Si       | 0                  | 92            | No   |
| Carlos Flores     | Medicina          | 87      | Si       | 1                  | 88            | No   |
| Nayeli Sanchez    | Medicina          | 97      | Si       | 0                  | 97            | Si   |
| Pablo Hernandez   | Medicina          | 98      | Si       | 0                  | 98            | Si   |
| Manuel Garcia     | Medicina          | 90      | Si       | 0                  | 90            | No   |
| Manuel Flores     | Medicina          | 88      | Si       | 5                  | 93            | No   |
| Diego Garcia      | Medicina          | 85      | Si       | 0                  | 85            | No   |
| Brayan Torres     | Medicina          | 92      | Si       | 5                  | 97            | No   |
| Nayeli Lopez      | Medicina          | 91      | Si       | 0                  | 91            | No   |
| Diego Gonzalez    | Medicina          | 97      | Si       | 0                  | 97            | Si   |
| Eimer Perez       | Medicina          | 96      | Si       | 0                  | 96            | Si   |
| Marco Hernandez   | Medicina          | 93      | Si       | 5                  | 98            | No   |
| Ricardo Ramirez   | Medicina          | 92      | Si       | 0                  | 92            | No   |
| Carlos Garcia     | Medicina          | 92      | Si       | 5                  | 97            | No   |
| Nayeli Hernandez  | Medicina          | 85      | Si       | 0                  | 85            | No   |
| Juan Hernandez    | Medicina          | 95      | Si       | 0                  | 95            | Si   |
| Juan Martinez     | Medicina          | 97      | Si       | 0                  | 97            | Si   |
| Brayan Hernandez  | Medicina          | 100     | Si       | 0                  | 100           | Si   |
| Diego Sanchez     | Medicina          | 97      | Si       | 0                  | 97            | Si   |
| Juan Hernandez    | Medicina          | 91      | Si       | 6                  | 97            | No   |
| Manuel Torres     | Medicina          | 87      | Si       | 0                  | 87            | No   |
| Irene Martinez    | Medicina          | 99      | Si       | 0                  | 99            | Si   |
| Marco Sanchez     | Medicina          | 93      | Si       | 0                  | 93            | No   |
| Pablo Sanchez     | Medicina          | 85      | Si       | 2                  | 87            | No   |
| Manuel Ramirez    | Medicina          | 95      | Si       | 0                  | 95            | Si   |
----------------------------------------------------------------------------------------------------------
*/