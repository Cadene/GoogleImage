package upmc.ri.bin;

import java.util.ArrayList;
import java.util.List;

import upmc.ri.index.ImageFeatures;
import upmc.ri.index.VIndexFactory;
import upmc.ri.io.ImageNetParser;
import upmc.ri.struct.DataSet;
import upmc.ri.struct.STrainingSample;
import upmc.ri.utils.PCA;

public class VisualIndexes {

	public static DataSet<double[], String> create() throws Exception {
		String path2dataset = "/users/Etu3/3000693/Documents/RI/GoogleImage/sbow/";
		List<String> classnames = new ArrayList<String>();
		classnames.add("acoustic_guitar.txt");
		classnames.add("ambulance.txt");
		classnames.add("electric_guitar.txt");
		classnames.add("european_fire_salamander.txt");
		classnames.add("harp.txt");
		classnames.add("minivan.txt");
		classnames.add("taxi.txt");
		classnames.add("tree-frog.txt");
		classnames.add("wood-frog.txt");
		
		List<STrainingSample<double[], String>> listtrain = new ArrayList<STrainingSample<double[], String>>();
		List<STrainingSample<double[], String>> listtest = new ArrayList<STrainingSample<double[], String>>();
		for (String classname : classnames) {
			int count = 0;
			for (List<Integer> words : ImageNetParser.getWords(path2dataset + classname)) {
				double[] features = VIndexFactory.computeBow(words);
				if (count < 800) {
					listtrain.add(new STrainingSample<double[], String>(features, classname));
				} else {
					listtest.add(new STrainingSample<double[], String>(features, classname));
				}
				count++;
			}
		}
		DataSet<double[], String> dataset = new DataSet<double[], String>(listtrain, listtest);
		
		dataset = PCA.computePCA(dataset, 250);
		
		return dataset;
	}
}
