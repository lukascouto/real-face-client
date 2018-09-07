/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import reconhecimento.ReconhecimentoFacialFacade;

/**
 *
 * @author lukascouto
 */
public class Classificador {

    public void salvarClassificador() throws MalformedURLException, IOException {
        
        ReconhecimentoFacialFacade reconhecimentoFacialFacade = new ReconhecimentoFacialFacade();
        
        Usuario usuario = new Usuario();
        String idUsuario = usuario.usuarioGetId();

        String url = "http://localhost:9000/classificador/salvar";
        String charset = "UTF-8";
        String param = "value";
        File binaryFile = new File("src/recursos/"+ idUsuario +".classificadorLBPH.yml");
        String boundary = Long.toHexString(System.currentTimeMillis());
        String CRLF = "\r\n";

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        connection.setRequestProperty("Authorization", "Bearer " + reconhecimentoFacialFacade.setToken() + "");
        connection.setRequestProperty("form-data", "file");

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(binaryFile.toPath(), output);
            output.flush();
            writer.append(CRLF).flush();

            writer.append("--" + boundary + "--").append(CRLF).flush();
        }

        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); // Deve retornar 200

    }

}
