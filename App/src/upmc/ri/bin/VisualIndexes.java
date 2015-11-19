package upmc.ri.bin;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

import upmc.ri.index.VIndexFactory;
import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.STrainingSample;
import upmc.ri.utils.PCA;

public class VisualIndexes {

	public static DataSet<double[], String> create(String sourcePath) throws Exception {
		Set<String> classnames = ImageNetParser.classesImageNet();

		List<STrainingSample<double[], String>> listtrain = new ArrayList<STrainingSample<double[], String>>();
		List<STrainingSample<double[], String>> listtest = new ArrayList<STrainingSample<double[], String>>();
		
		/* Calcul des BoW de chaque image de chaque classe: */
		for (String classname : classnames) {
			int count = 0;
			for (List<Integer> words : ImageNetParser.getWords(sourcePath + classname + ".txt")) {
				double[] bow = VIndexFactory.computeBow(words);
				/* les 800 premières images en train, le reste en test */
				if (count < 800) {
					listtrain.add(new STrainingSample<double[], String>(bow, classname));
				} else {
					listtest.add(new STrainingSample<double[], String>(bow, classname));
				}
				count++;
			}
		}
		return new DataSet<double[], String>(listtrain, listtest);
	}
	
	public static DataSet<double[], String> create(String sourcePath, String targetPath, int nbPca) throws Exception {
		DataSet<double[], String> dataset = VisualIndexes.create(sourcePath);
		String strPCA = " ";
		if (nbPca > 0){
			dataset = PCA.computePCA(dataset, nbPca);
			strPCA = " with PCA (" + nbPca + " components) ";
		}
		FileOutputStream fileOut = new FileOutputStream(targetPath);
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(dataset);
		out.close();
		System.out.printf("Dataset" + strPCA + "saved in " + targetPath);
		return dataset;
	}
	
	public static void main(String[] args) throws Exception {
		int nbPCA = 250;
		String sourcePath = "/users/nfs/Enseignants/thomen/Bases/ImageNet/BoF/txt/";
		String targetPath = "/Vrac/3152691/RI_Image/bows_" + nbPCA + ".ser";
		VisualIndexes.create(sourcePath, targetPath, nbPCA);
	}
	
}