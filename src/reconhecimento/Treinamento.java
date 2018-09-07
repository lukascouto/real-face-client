package reconhecimento;

import connection.Usuario;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import static org.bytedeco.javacpp.opencv_core.CV_32SC1;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_face.FaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.EigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.FisherFaceRecognizer;
import static org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.IOException;

/**
 *
 * @author lukascouto
 */
public class Treinamento {

    public void treinar() throws IOException {
        
        Usuario usuario = new Usuario();
        String idUsuario = usuario.usuarioGetId();
        
        File diretorio = new File("src/fotos");
        FilenameFilter filtroImagem = (File dir, String nome) -> nome.endsWith(".jpg") || nome.endsWith(".gif") || nome.endsWith(".png");

        File[] arquivos = diretorio.listFiles(filtroImagem);
        MatVector fotos = new MatVector(arquivos.length);
        Mat rotulos = new Mat(arquivos.length, 1, CV_32SC1);
        IntBuffer rotulosBuffer = rotulos.createBuffer();
        int contador = 0;
        for (File imagem : arquivos) {
            Mat foto = imread(imagem.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            int classe = Integer.parseInt(imagem.getName().split("\\.")[1]);
            //classe = 1;
            //System.out.println(classe);
            resize(foto, foto, new Size(160, 160));
            fotos.put(contador, foto);
            rotulosBuffer.put(contador, classe);
            contador++;
            classe++;
        }

        //FaceRecognizer eigenfaces = EigenFaceRecognizer.create();
        //FaceRecognizer fisherfaces = FisherFaceRecognizer.create();
        FaceRecognizer lbph = LBPHFaceRecognizer.create();

        //eigenfaces.train(fotos, rotulos);
        //eigenfaces.save("src/recursos/classificadorEigenFaces.yml");
        //fisherfaces.train(fotos, rotulos);
        //fisherfaces.save("src/recursos/classificadorFisherFaces.yml");
        lbph.train(fotos, rotulos);
        lbph.save("src/recursos/"+idUsuario+".classificadorLBPH.yml");

    }
}
