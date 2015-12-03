package upmc.ri.bin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import upmc.ri.struct.DataSet;
import upmc.ri.struct.Evaluator;
import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.MultiClass;
import upmc.ri.struct.model.LinearStructModel;
import upmc.ri.struct.model.LinearStructModel_Ex;
import upmc.ri.struct.training.SGDTrainer;

public class MulticlassClassif {

	private DataSet<double[], String> dataSet;
	
	public MulticlassClassif(DataSet<double[], String> dataSet) {
		super();
		this.dataSet = dataSet;
	}
	
	public MulticlassClassif(String sourcePath) throws ClassNotFoundException, IOException {
		this(VisualIndexes.load(sourcePath));
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		int nbPCA = 250;
		String sourcePath = "/Vrac/3152691/RI_Image/bows_" + nbPCA + ".ser";
		int dimpsi = 0;
		Set<String> classes;
		
		double eps = 1e1;
		double lambda = 2e-6;
		int maxIter = 20;
		
		MulticlassClassif multiclassClassif = new MulticlassClassif(sourcePath);
		dimpsi = multiclassClassif.dataSet.listtest.get(0).input.length;
		classes = multiclassClassif.dataSet.outputs();
		MultiClass multiclass = new MultiClass(classes);
		LinearStructModel<double[], String> linear = new LinearStructModel_Ex<double[], String>(multiclass, dimpsi * classes.size());
		Evaluator<double[], String> evaluator = new Evaluator<double[], String>();
		evaluator.setModel(linear);
		evaluator.setListtrain(multiclassClassif.dataSet.listtrain);
		evaluator.setListtest(multiclassClassif.dataSet.listtest);
		SGDTrainer<double[], String> trainer = new SGDTrainer<double[], String>(evaluator, eps, lambda, maxIter);
		trainer.train(multiclassClassif.dataSet.listtrain, linear);
		evaluator.evaluate();
		System.out.println("Train Error : " + String.valueOf(evaluator.getErr_train()));
		System.out.println("Test  Error : " + String.valueOf(evaluator.getErr_test() ));
		ArrayList<String> predictions = new ArrayList<String>();
		ArrayList<String> gt = new ArrayList<String>();
		for (STrainingSample<double[], String> ts : multiclassClassif.dataSet.listtest) {
			predictions.add(linear.predict(ts));
			gt.add(ts.output);
		}
		multiclass.confusionMatrix(predictions, gt);
	}

}