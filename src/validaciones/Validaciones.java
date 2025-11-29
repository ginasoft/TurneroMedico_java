package validaciones;

public class Validaciones {

    public static void validarTextoNoVacio(String texto, String nombreCampo)
            throws TextoException {
        if (texto == null || texto.isBlank()) {
            throw new TextoException(nombreCampo + " no puede estar vacío");
        }
    }

    public static void validarNumeroPositivo(double numero, String nombreCampo)
            throws NumeroException {
        if (numero <= 0) {
            throw new NumeroException(nombreCampo + " debe ser mayor que cero");
        }
    }

    public static void validarDni(String dni, String nombreCampo)
            throws TextoException {
        if (dni == null || !dni.matches("\\d{1,8}")) {
            throw new TextoException(nombreCampo + " inválido: debe contener entre 1 y 8 dígitos");
        }
    }
}
