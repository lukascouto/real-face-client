/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reconhecimento;

import java.io.IOException;
import java.net.MalformedURLException;
import org.bytedeco.javacv.FrameGrabber;

/**
 *
 * @author lukascouto
 */
public class Principal {

    public static void main(String args[]) throws FrameGrabber.Exception, InterruptedException, MalformedURLException, IOException {

        ReconhecimentoFacialFacade reconhecimentoFacialFacade = new ReconhecimentoFacialFacade();
        //reconhecimentoFacialFacade.captura();
        //reconhecimentoFacialFacade.treinar();
        //reconhecimentoFacialFacade.salvarPessoa();
        //reconhecimentoFacialFacade.buscarPessoa();
        //reconhecimentoFacialFacade.salvarClassificador();
        reconhecimentoFacialFacade.reconhecerWebCam();
         
    }
}
