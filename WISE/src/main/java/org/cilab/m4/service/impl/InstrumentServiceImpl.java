package org.cilab.m4.service.impl;

import java.util.List;
import java.util.Map;

import org.cilab.m4.dao.InstrumentDAO;
import org.cilab.m4.model.Instrument;
import org.cilab.m4.service.InstrumentService;

public class InstrumentServiceImpl implements InstrumentService {

	/**
	 * Class Name:	InstrumentServiceImpl.java
	 * Description: 	
	 * 
	 * @author Meilan Jiang
	 * @since 2016.04.28
	 * @version 1.2
	 * 
	 * Copyright(c) 2016 by CILAB All right reserved.
	 */
	
	InstrumentDAO instrumentDao;	
	public void setInstrumentDao(InstrumentDAO instrumentDao){
		this.instrumentDao = instrumentDao;
	}
	@Override
	public boolean newInstance(Instrument inst) {
		
		return instrumentDao.create(inst);		
	}

	@Override
	public Instrument readInstance(int instID) {
		Instrument inst = instrumentDao.read(instID);
		return inst;
	}

	@Override
	public boolean updateInstance(Instrument inst) {
		return instrumentDao.update(inst);
	}

	@Override
	public boolean deleteInstance(int instID) {
		return instrumentDao.delete(instID);
	}

	@Override
	public List<Instrument> readCollection() {
		return instrumentDao.list();
	}

	@Override
	public List<Instrument> search(Map<String, String> map) {
		return instrumentDao.search(map);
	}
	@Override
	public List<Instrument> listSearch(Map<String, List<String>> map) {
		return instrumentDao.listSearch(map);
	}

}
