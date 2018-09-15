/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JOptionPane;
import org.bytedeco.javacv.FrameGrabber;
import reconhecimento.ReconhecimentoFacialFacade;

/**
 *
 * @author lukascouto
 */
public class Pessoa {

    public void salvarPessoa(String tipoCaptura) throws MalformedURLException, IOException, FrameGrabber.Exception, InterruptedException, Exception {

        ReconhecimentoFacialFacade reconhecimentoFacialFacade = new ReconhecimentoFacialFacade();

        String nome = JOptionPane.showInputDialog("Digite o seu nome:");

        String uri = "http://localhost:9000/pessoa/salvar";
        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + reconhecimentoFacialFacade.setToken() + "");

        String input = "{\"nome\" : \"" + nome + "\"}";

        OutputStream os = connection.getOutputStream();
        os.write(input.getBytes());
        os.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(
                (connection.getInputStream())));

        // Recupera o id da pessoa que foi cadastrada
        String idPessoa = br.readLine();

        if (tipoCaptura.equalsIgnoreCase("webcam")) {
            reconhecimentoFacialFacade.capturaWebCam(nome, idPessoa);
        } else if (tipoCaptura.equalsIgnoreCase("cameraip")) {
            reconhecimentoFacialFacade.capturaCameraIp(nome, idPessoa);
        } else {
            System.out.println("Informe um parâmetro válido em 'salvarPessoa'.");
            System.out.println("Os parâmetros aceitos são 'webcam' ou 'cameraip'.");
        }
        connection.disconnect();
    }

    public String buscarPessoa() throws MalformedURLException, IOException {

        ReconhecimentoFacialFacade reconhecimentoFacialFacade = new ReconhecimentoFacialFacade();

        URL myURL = new URL("http://localhost:9000/pessoa/buscar");
        HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + reconhecimentoFacialFacade.setToken() + "");
        connection.setDoOutput(true);
        connection.connect();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder results = new StringBuilder();
        String line;
        String pessoasJson;
        while ((line = reader.readLine()) != null) {
            
            results.append(line);
        }
        
        while ((line = reader.readLine()) != null) {
            results.append(line);
        }
        pessoasJson = results.toString();
      
        return pessoasJson;
    }
}
