package reconhecimento;

import java.awt.event.KeyEvent;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import static org.bytedeco.javacpp.opencv_imgcodecs.imwrite;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_imgproc.*;

/**
 *
 * @author lukascouto
 */
public class Captura {
    
    public void capturaWebCam(String nome, String id) throws FrameGrabber.Exception, InterruptedException {
        
        KeyEvent tecla = null;
        OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);
        camera.start();
        
        CascadeClassifier detectorFace = new CascadeClassifier("src/recursos/haarcascade-frontalface-alt.xml");
                
        CanvasFrame cFrame = new CanvasFrame("Preview", CanvasFrame.getDefaultGamma() / camera.getGamma());
        Frame frameCapturado = null;
        Mat imagemColorida = new Mat();
        int numeroAmostras = 5;
        int amostra = 1;
        
        while ((frameCapturado = camera.grab()) != null) {
            imagemColorida = converteMat.convert(frameCapturado);
            Mat imagemCinza = new Mat();
            cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
            RectVector facesDetectadas = new RectVector();
            detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 1, 0, new Size(150,150), new Size(500,500));
            if (tecla == null) {
                tecla = cFrame.waitKey(5);
            }
            for (int i=0; i < facesDetectadas.size(); i++) {
                Rect dadosFace = facesDetectadas.get(0);
                rectangle(imagemColorida, dadosFace, new Scalar(0,0,255, 0));
                Mat faceCapturada = new Mat(imagemCinza, dadosFace);
                resize(faceCapturada, faceCapturada, new Size(160,160));
                if (tecla == null) {
                tecla = cFrame.waitKey(5);
                }
                if (tecla != null) {
                    if (tecla.getKeyChar() == 'q') {
                        if (amostra <= numeroAmostras) {
                            imwrite("src/fotos/"+nome+"."+id+"."+amostra+".jpg", faceCapturada);
                            System.out.println("Foto "+amostra+" capturada\n");
                            amostra++;
                        }
                    }
                    tecla = null;
                }
            }
            if (tecla == null) {
                tecla = cFrame.waitKey(20);
            }
            if (cFrame.isVisible()) {
                cFrame.showImage(frameCapturado);
            }
            if (amostra > numeroAmostras) {
                break;
            }
        }
        cFrame.dispose();
        camera.stop();             
    }
    
    public void capturaCameraIp () {
        
        System.out.println("Este método ainda não foi implementado.");
        
    }
}
