package com.quxin.freshfun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.AgentWithdrawMapper;
import com.quxin.freshfun.dao.AgentWithdrawWayMapper;
import com.quxin.freshfun.model.AgentWithdrawPOJO;
import com.quxin.freshfun.model.AgentWithdrawWayPOJO;
import com.quxin.freshfun.service.AgentWithdrawService;
/**
 * 商户提现service实现类
 * @author ziming
 * 2016-9-20
 */
@Service("agentWithdrawService")
public class AgentWithdrawServiceImpl implements AgentWithdrawService {
	@Autowired
	private AgentWithdrawWayMapper withdrawWayMapper;  //商户提现方式mapper
	
	@Autowired
	private AgentWithdrawMapper withdrawMapper;  //商户提现mapper
	
	@Override
	public int addWithdrawWay(AgentWithdrawWayPOJO agentWithdrawWay) {
		return withdrawWayMapper.insertSelective(agentWithdrawWay);
	}

	@Override
	public List<AgentWithdrawWayPOJO> queryWithdrawWay(Integer userId) {
		return withdrawWayMapper.selectWithdrawWayByUserId(userId);
	}

	@Override
	public int addWithdraw(AgentWithdrawPOJO agentWithdraw) {
		return withdrawMapper.insertSelective(agentWithdraw);
	}
	

	@Override
	public int modifyEscDefaultWay(Integer userId) {
		// TODO Auto-generated method stub
		return withdrawWayMapper.updateWayEscDefault(userId);
	}

	@Override
	public int modifySetDefaultWay(Integer id) {
		// TODO Auto-generated method stub
		return withdrawWayMapper.updateWaySetDefault(id);
	}

	@Override
	public AgentWithdrawWayPOJO queryDefaultWay(Integer userId) {
		List<AgentWithdrawWayPOJO> list = withdrawWayMapper.selectDefaultWay(userId);
		if(list!=null&&list.size()>0)
			return list.get(0);
		return null;
	}

	@Override
	public List<AgentWithdrawPOJO> queryWithdraw(Integer userId) {
		return withdrawMapper.selectAgentWithdrawByUserId(userId);
	}

}
