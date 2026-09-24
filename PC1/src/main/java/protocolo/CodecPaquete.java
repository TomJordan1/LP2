package protocolo;

import excepciones.ComandoInvalidoException;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// Serialización BINARIA de paquetes (el texto no sirve para imágenes ni sonido).
//
//   ┌──────────┬──────────────────┬──────────────┬──────────────────┐
//   │ tipo (1) │ origen (UTF)     │ largo (4)    │ datos (largo)    │
//   └──────────┴──────────────────┴──────────────┴──────────────────┘

public class CodecPaquete {

    public void escribir(DataOutputStream salida, Paquete p) throws IOException {
        salida.writeByte(p.tipo().codigo());
        salida.writeUTF(p.origen());
        salida.writeInt(p.datos().length);
        salida.write(p.datos());
    }

    public Paquete leer(DataInputStream entrada) throws IOException {
        TipoPaquete tipo = TipoPaquete.desde(entrada.readByte());
        String origen = entrada.readUTF();
        int largo = entrada.readInt();
        if (largo < 0 || largo > tipo.maxBytes()) {
            throw new ComandoInvalidoException("Paquete " + tipo + " de " + largo + " bytes fuera de rango");
        }
        byte[] datos = new byte[largo];
        entrada.readFully(datos);
        return new Paquete(tipo, origen, datos);
    }
}
