package com.xanadutec.coreflex.operators;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.UserModel;

@Service("OperatorService")
public class OperatorServiceImpl implements OperatorService{

	@Autowired
	private OperatorDao operatorDao;
	
	@Override
	public List<Operator> getListOfActiveOperators() {
		// TODO Auto-generated method stub
		return operatorDao.getListOfActiveOperators();
	}

	@Override
	public Operator getOperatorById(int id) {
		// TODO Auto-generated method stub
		return operatorDao.getOperatorById(id);
	}
	

}
