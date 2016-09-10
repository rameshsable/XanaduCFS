package com.xanadutec.coreflex.operators;

import java.util.List;

import com.xanadutec.coreflex.model.Operator;

public interface OperatorDao {

	public List<Operator> getListOfActiveOperators();
	public Operator getOperatorById(int id);
}
