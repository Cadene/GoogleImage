package upmc.ri.struct.model;

import upmc.ri.struct.STrainingSample;
import upmc.ri.struct.instantiation.IStructInstantiation;
import upmc.ri.utils.VectorOperations;

public class LinearStructModel_Ex<X, Y> extends LinearStructModel<X, Y> {

	public LinearStructModel_Ex (IStructInstantiation<X, Y> instantiation, int dimpsi) {
		super(dimpsi);
		this.instantiation = instantiation;
	}
	
	@Override
	public Y predict(STrainingSample<X, Y> ts) {
		X xi = ts.input;
		Y argmax = null;
		double max = - Double.MAX_VALUE;
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
		double max = - Double.MAX_VALUE;
		for (Y y : this.instantiation.enumerateY()) {
			double rslt = this.instantiation.delta(y, yi) + VectorOperations.dot(this.parameters, this.instantiation.psi(xi, y));
			if (max < rslt) {
				max = rslt;
				argmax = y;
			}
		}
		return argmax;
	}
}
