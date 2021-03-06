package com.redhat.crowdfunding.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.redhat.crowdfunding.contract.CrowdFundingContract;

/**
 * @author littleredhat
 */
public class Util {

	/**
	 * 存储文件
	 * 
	 * @param content
	 * @return
	 */
	public static File StoreFile(String content) {
		// 临时文件
		File tmp = null;
		try {
			tmp = File.createTempFile(Consts.PREFIX, Consts.SUFFIX);
			// 自动删除
			tmp.deleteOnExit();
			// 写入内容
			BufferedWriter out = new BufferedWriter(new FileWriter(tmp));
			out.write(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return tmp;
	}

	/**
	 * 获取合约
	 * 
	 * @param credentials
	 * @param contractAddress
	 * @return
	 */
	public static CrowdFundingContract GetCrowdFundingContract(Credentials credentials, String contractAddress) {
		// defaults to http://localhost:8545/
		Web3j web3j = Web3j.build(new HttpService());
		return new CrowdFundingContract(contractAddress, web3j, credentials, Consts.GAS_PRICE, Consts.GAS_LIMIT);
	}
}
