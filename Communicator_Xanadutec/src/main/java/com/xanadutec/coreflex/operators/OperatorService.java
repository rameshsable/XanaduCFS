package com.xanadutec.coreflex.operators;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.UserModel;

public interface OperatorService {

	public List<Operator> getListOfActiveOperators();
	public Operator getOperatorById(int id);

}
