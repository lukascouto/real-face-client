/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reconhecimento;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import connection.Pessoa;
import connection.Usuario;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import model.PessoaModel;
import org.bytedeco.javacpp.DoublePointer;
import org.bytedeco.javacpp.IntPointer;
import static org.bytedeco.javacpp.opencv_core.FONT_HERSHEY_PLAIN;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.RectVector;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;

import static org.bytedeco.javacpp.opencv_imgproc.COLOR_BGRA2GRAY;
import static org.bytedeco.javacpp.opencv_imgproc.cvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.putText;
import static org.bytedeco.javacpp.opencv_imgproc.rectangle;
import static org.bytedeco.javacpp.opencv_imgproc.resize;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

/**
 *
 * @author Lukas
 */
public class Reconhecimento {

    public void reconhecerWebCam() throws FrameGrabber.Exception, IOException {

        Pessoa pessoa = new Pessoa(); 
        String pessoasJson = pessoa.buscarPessoa();
        
        Type pessoaListType = new TypeToken<ArrayList<PessoaModel>>() {}.getType();

        java.util.List<PessoaModel> pessoas = new Gson().fromJson(pessoasJson, pessoaListType);
        
        OpenCVFrameConverter.ToMat converteMat = new OpenCVFrameConverter.ToMat();
        OpenCVFrameGrabber camera = new OpenCVFrameGrabber(0);

        //String[] pessoas = {"", "", "Olga", "Lukas"};
        camera.start();

        CascadeClassifier detectorFace = new CascadeClassifier("src/recursos/haarcascade-frontalface-alt.xml");

        try {
            Usuario usuario = new Usuario();
            String idUsuario = usuario.usuarioGetId();

            //FaceRecognizer reconhecedor = EigenFaceRecognizer.create();
            //reconhecedor.read("src/recursos/classificadorEigenFaces.yml");
            //reconhecedor.setThreshold(0);
            //FaceRecognizer reconhecedor = FisherFaceRecognizer.create();
            //reconhecedor.read("src/recursos/classificadorFisherFaces.yml");
            FaceRecognizer reconhecedor = LBPHFaceRecognizer.create();
            reconhecedor.read("src/recursos/"+ idUsuario + ".classificadorLBPH.yml");
            reconhecedor.setThreshold(70);

            CanvasFrame cFrame = new CanvasFrame("Reconhecimento", CanvasFrame.getDefaultGamma() / camera.getGamma());
            Frame frameCapturado = null;
            Mat imagemColorida = new Mat();

            while ((frameCapturado = camera.grab()) != null) {
                imagemColorida = converteMat.convert(frameCapturado);
                Mat imagemCinza = new Mat();
                cvtColor(imagemColorida, imagemCinza, COLOR_BGRA2GRAY);
                RectVector facesDetectadas = new RectVector();
                detectorFace.detectMultiScale(imagemCinza, facesDetectadas, 1.1, 2, 0, new Size(100, 100), new Size(500, 500));
                for (int i = 0; i < facesDetectadas.size(); i++) {
                    Rect dadosFace = facesDetectadas.get(i);
                    rectangle(imagemColorida, dadosFace, new Scalar(0, 255, 0, 4));
                    Mat faceCapturada = new Mat(imagemCinza, dadosFace);
                    resize(faceCapturada, faceCapturada, new Size(160, 160));

                    IntPointer rotulo = new IntPointer(1);
                    DoublePointer confianca = new DoublePointer(1);
                    reconhecedor.predict(faceCapturada, rotulo, confianca);
                    int predicao = rotulo.get(0);
                    String nome = null;
                    if (predicao == -1) {
                        nome = "Desconhecido";
                    } else {
                        //nome = pessoas[predicao] + " - " + confianca.get(0);
                        for (PessoaModel p : pessoas) {
                            if (p.getId() == predicao) {
                                nome = p.getNome();
                            }
                        }
                    }
                    int x = Math.max(dadosFace.tl().x() - 10, 0);
                    int y = Math.max(dadosFace.tl().y() - 10, 0);
                    putText(imagemColorida, nome, new Point(x, y), FONT_HERSHEY_PLAIN, 1.4, new Scalar(0, 255, 0, 4));
                }
                if (cFrame.isVisible()) {
                    cFrame.showImage(frameCapturado);
                }
            }
            cFrame.dispose();
            camera.stop();

        } catch (IOException e) {

            System.out.println("Não foi possível encontrar o usuário da API.");
        }
    }
}
