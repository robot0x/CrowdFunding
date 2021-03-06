package com.redhat.crowdfunding.contract;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.utils.Async;

import com.redhat.crowdfunding.util.Consts;

/**
 * @author littleredhat
 */
public class CrowdFundingContract extends Contract implements CrowdFundingInterface {

	/**
	 * CrowdFunding合约
	 * 
	 * @param contractAddress
	 *            合约地址
	 * @param web3j
	 *            RPC请求
	 * @param credentials
	 *            钱包凭证
	 * @param gasPrice
	 *            GAS价格
	 * @param gasLimit
	 *            GAS上限
	 */
	public CrowdFundingContract(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice,
			BigInteger gasLimit) {
		super("", contractAddress, web3j, credentials, gasPrice, gasLimit);
	}

	/**
	 * 获取数量
	 */
	public Future<Uint256> getFundCount() {
		Function function = new Function("getFundCount", Arrays.asList(),
				Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
				}));
		return executeCallSingleValueReturnAsync(function);
	}

	/**
	 * 获取信息
	 */
	public CompletableFuture<List<Type>> getFundInfo(int i) {
		Function function = new Function("getFundInfo", Arrays.asList(new Uint256(BigInteger.valueOf(i))),
				Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {
				}, new TypeReference<Uint256>() {
				}, new TypeReference<Uint256>() {
				}));
		return executeCallMultipleValueReturnAsync(function);
	}

	/**
	 * 是否存在
	 */
	public Future<Bool> isExist(String owner) {
		Function function = new Function("isExist", Arrays.asList(new Address(owner)),
				Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {
				}));
		return executeCallSingleValueReturnAsync(function);
	}

	/**
	 * 发起众筹
	 */
	public Future<TransactionReceipt> raiseFund(String owner) {
		Function function = new Function("raiseFund", Arrays.asList(new Address(owner)),
				Arrays.<TypeReference<?>>asList());
		return executeTransactionAsync(function);
	}

	/**
	 * 发送金币
	 */
	public Future<TransactionReceipt> sendCoin(String owner, int coin) {
		Function function = new Function("sendCoin", Arrays.asList(new Address(owner)),
				Arrays.<TypeReference<?>>asList());
		return Async.run(() -> executeTransaction(FunctionEncoder.encode(function),
				BigInteger.valueOf(coin).multiply(Consts.ETHER)));
	}
}
