package reconhecimento;

import connection.Classificador;
import connection.Pessoa;
import java.io.IOException;
import java.net.MalformedURLException;
import org.bytedeco.javacv.FrameGrabber;

/**
 *
 * @author lukascouto
 */
public class ReconhecimentoFacialFacade {

    private final Captura captura = new Captura();
    private final Treinamento treinamento = new Treinamento();
    private final Reconhecimento reconhecimento = new Reconhecimento();
    private final Classificador classificador = new Classificador();
    private final Pessoa pessoa = new Pessoa();

    // Recebe um token válido gerado no momento do cadastro de usuário.
    public String setToken() {
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWthc0BlbWFpbC5jb20iLCJyb2xlIjoiUk9MRV9VU1VBUklPIiwiY3JlYXRlZCI6MTUzNjk3NTQ3MDQzMiwiZXhwIjoxNTM3NTgwMjcwfQ.TExwRyBfZ8tiDhg-w04L6J3H_bB0_w2XtOjJXhEKkmfA_5imYsSN1YPBrxVF4OFfTZ6OtcXDDH2kruAIU4swew";
        return token;
    }
    
    // Aqui deve ser passado como parâmetro a url da câmera IP.
    //Exemplo de url aceito: http://192.168.1.5:8080/video
    public String urlCameraIp() {
        String url = "http://192.168.1.5:8080/video";
        return url;
    }

    // São aceitos apenas dois parâmetros: "webcam" ou "cameraip"
    public void salvarPessoa() throws IOException, FrameGrabber.Exception, InterruptedException, Exception {
        this.pessoa.salvarPessoa("cameraip");
    }

    public void buscarPessoa() throws IOException {
        this.pessoa.buscarPessoa();
    }

    // Abre a webcam que por default tem o valor 0.
    public void capturaWebCam(String nome, String id) throws FrameGrabber.Exception, InterruptedException {
        this.captura.capturaWebCam(nome, id);
    }

    // Recebe a url da câmera ip e abre a imagem em tempo real.
    public void capturaCameraIp(String nome, String id) throws FrameGrabber.Exception, InterruptedException, Exception {
        this.captura.capturaCameraIp(nome, id);
    }

    // Realiza o treinamento das imagens e gera um classificador salva dentro da aplicação com o id do usuário da API.
    public void treinar() throws IOException {
        this.treinamento.treinar();
    }

    // Salva o classificador no servidor (backup)
    public void salvarClassificador() throws IOException {
        this.classificador.salvarClassificador();
    }

    // Abre o video da webcam e realiza o reconhecimento facial
    public void reconhecerWebCam() throws FrameGrabber.Exception, IOException {
        this.reconhecimento.reconhecerWebCam();
    }

    // Abre o vídeo da câmera IP e realiza o reconhecimento facial
    public void reconhecerCameraIp() throws IOException {
        this.reconhecimento.reconhecerCameraIp();
    }
}