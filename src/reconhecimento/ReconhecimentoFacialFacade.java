package reconhecimento;

import connection.Classificador;
import connection.Pessoa;
import java.io.IOException;
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
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJsdWthc2NvdXRvLmd0QGxpdmUuY29tIiwicm9sZSI6IlJPTEVfVVNVQVJJTyIsImNyZWF0ZWQiOjE1MzU5MTQ3NDYxODIsImV4cCI6MTUzNjUxOTU0Nn0.668QGt7H4lkH-8LWqatl_gdoXHywy2BnGecAD_QFfW-kjFkG8ErJyiRoK23k5AM43a9SNmaEoKh_9Y1lbGxYUw";
        return token;
    }
    
    // São aceitos apenas dois parâmetros: "webcam" ou "cameraip"
    public void salvarPessoa() throws IOException, FrameGrabber.Exception, InterruptedException {
        this.pessoa.salvarPessoa("webcam");
    }
    
    public void buscarPessoa() throws IOException {
        this.pessoa.buscarPessoa();
    }
    
    // Abre a webcam que por default tem o valor 0.
    public void capturaWebCam(String nome, String id) throws FrameGrabber.Exception, InterruptedException {
        this.captura.capturaWebCam(nome, id);
    }
    
    // Recebe a url da câmera ip e abre a imagem em tempo real.
    public void capturaCameraIp() {
        this.captura.capturaCameraIp();
    }
    
    // Realiza o treinamento das imagens e gera um classificador salva dentro da aplicação com o id do usuário da API.
    public void treinar() throws IOException {
        this.treinamento.treinar();
    }
    
    // Salva o classificador gerado no servidor primeiro chamando o id do usuário
    public void salvarClassificador() throws IOException {
        this.classificador.salvarClassificador();
    }
    
    // Abre o video da webcam e realiza o reconhecimento facial
    public void reconhecerWebCam() throws FrameGrabber.Exception, IOException {
        this.reconhecimento.reconhecerWebCam();
    }
}
