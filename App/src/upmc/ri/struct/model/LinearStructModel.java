package upmc.ri.struct.model;

import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.utils.VectorOperations;

public class LinearStructModel<X, Y> implements IStructModel<X, Y> {

	private IStructInstantiation<X, Y> instantiation;
	private double[] parameters;
	
	public LinearStructModel (IStructInstantiation<X, Y> instantiation, int dimpsi) {
		this.instantiation = instantiation;
		this.parameters = new double[dimpsi];
	}
	
	@Override
	public Y predict(STrainingSample<X, Y> ts) {
		X xi = ts.input;
		Y argmax = null;
		double max = -99999999.99;
		for (Y y : this.instantiation.enumerateY()) {
			double rslt = VectorOperations.dot(this.parameters, this.instantiation.psi(xi, y));
			if (max < rslt) {
				max = rslt;
				argmax = y;
			}
		}
		return argmax;
	}

	@Override
	public Y lai(STrainingSample<X, Y> ts) {
		X xi = ts.input;
		Y yi = ts.output;
		Y argmax = null;
		double max = -99999999.99;
		for (Y y : this.instantiation.enumerateY()) {
			double rslt = this.instantiation.delta(yi, y) + VectorOperations.dot(this.parameters, this.instantiation.psi(xi, y));
			if (max < rslt) {
				max = rslt;
				argmax = y;
			}
		}
		return argmax;
	}

	@Override
	public IStructInstantiation<X, Y> instantiation() {
		return this.instantiation;
	}

	@Override
	public double[] getParameters() {
		return this.parameters;
	}

}
