package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import reconhecimento.ReconhecimentoFacialFacade;

/**
 *
 * @author lukascouto
 */
public class Usuario {
    
    public String usuarioGetId() throws MalformedURLException, IOException {
        ReconhecimentoFacialFacade reconhecimentoFacialFacade = new ReconhecimentoFacialFacade();
        
        URL myURL = new URL("http://localhost:9000/usuario/id");
        HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + reconhecimentoFacialFacade.setToken() + "");
        connection.setDoOutput(true);
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder results = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            
            results.append(line);
        }
        connection.disconnect(); 
        String idUsuario = results.toString();
        return idUsuario;       
    }  
}