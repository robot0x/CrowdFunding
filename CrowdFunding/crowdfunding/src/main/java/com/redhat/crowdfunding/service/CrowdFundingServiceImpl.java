package com.redhat.crowdfunding.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import com.redhat.crowdfunding.contract.CrowdFundingContract;
import com.redhat.crowdfunding.model.Fund;
import com.redhat.crowdfunding.util.Consts;
import com.redhat.crowdfunding.util.Util;

/**
 * @author littleredhat
 */
public class CrowdFundingServiceImpl implements CrowdFundingService {

	private CrowdFundingContract contract;

	public CrowdFundingServiceImpl() throws IOException, CipherException {
		// 获取凭证
		Credentials credentials = WalletUtils.loadCredentials(Consts.PASSWORD, Consts.PATH);
		contract = Util.GetCrowdFundingContract(credentials, Consts.CROWDFUNDING_ADDR);
	}

	public CrowdFundingServiceImpl(String password, String content) throws IOException, CipherException {
		// 获取凭证
		File tmp = Util.StoreFile(content);
		Credentials credentials = WalletUtils.loadCredentials(password, tmp);
		contract = Util.GetCrowdFundingContract(credentials, Consts.CROWDFUNDING_ADDR);
	}

	/**
	 * 获取数量
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public int getFundCount() throws InterruptedException, ExecutionException {
		return contract.getFundCount().get().getValue().intValue();
	}

	/**
	 * 获取列表
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public List<Fund> getFunds(int pageIndex) throws InterruptedException, ExecutionException {
		List<Fund> fList = new ArrayList<Fund>();
		// getFundCount
		int count = contract.getFundCount().get().getValue().intValue();
		int from = Consts.PAGE * pageIndex;
		int to = Math.min(Consts.PAGE * (pageIndex + 1), count);
		for (int i = from; i < to; i++) {
			// getFundInfo
			List<Type> info = contract.getFundInfo(i).get();
			Fund fund = new Fund();
			fund.setOwner(info.get(0).toString());
			fund.setNumber(Integer.parseInt(info.get(1).getValue().toString()));
			fund.setCoin(new BigInteger(info.get(2).getValue().toString()).divide(Consts.ETHER).intValue());
			fList.add(fund);
		}
		return fList;
	}

	/**
	 * 发起众筹
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public boolean raiseFund(String owner) throws InterruptedException, ExecutionException {
		boolean res = contract.isExist(owner).get().getValue();
		if (!res) { // 不存在
			contract.raiseFund(owner);
			return true;
		}
		return false;
	}

	/**
	 * 发送金币
	 * 
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public boolean sendCoin(String owner, int coin) throws InterruptedException, ExecutionException {
		boolean res = contract.isExist(owner).get().getValue();
		if (res) { // 存在
			contract.sendCoin(owner, coin);
			return true;
		}
		return false;
	}
}
