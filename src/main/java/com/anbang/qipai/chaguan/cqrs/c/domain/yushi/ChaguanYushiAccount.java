package com.anbang.qipai.chaguan.cqrs.c.domain.yushi;

import java.math.BigDecimal;

import com.dml.accounting.AccountOwner;
import com.dml.accounting.AccountingRecord;
import com.dml.accounting.AccountingSummary;
import com.dml.accounting.InsufficientBalanceException;

/**
 * 茶馆玉石账户
 * 
 * @author lsc
 *
 */
public class ChaguanYushiAccount {
	private String id;

	/**
	 * 货币。茶馆玉石。
	 */
	private String currency;

	/**
	 * 持有人
	 */
	private AccountOwner owner;

	/**
	 * 记账流水号
	 */
	private long accountingNo;

	/**
	 * 余额
	 */
	private double balance;

	/**
	 * 帐户存入
	 * 
	 * @param amount
	 * @param summary
	 * @return 记账记录
	 */
	public AccountingRecord deposit(double amount, AccountingSummary summary, long depositTime) {
		BigDecimal b1 = new BigDecimal(Double.toString(amount));
		BigDecimal b2 = new BigDecimal(Double.toString(balance));
		balance = b1.add(b2).doubleValue();
		accountingNo++;
		AccountingRecord ar = new AccountingRecord();
		ar.setAccountId(id);
		ar.setAccountingAmount(amount);
		ar.setAccountingNo(accountingNo);
		ar.setBalanceAfter(balance);
		ar.setSummary(summary);
		ar.setAccountingTime(depositTime);
		return ar;
	}

	/**
	 * 帐户取出。余额不足会抛出异常。
	 * 
	 * @param amount
	 * @param summary
	 * @return 记账记录
	 * @throws InsufficientBalanceException
	 */
	public AccountingRecord withdraw(double amount, AccountingSummary summary, long withdrawTime)
			throws InsufficientBalanceException {
		if (balance >= amount) {
			BigDecimal b1 = new BigDecimal(Double.toString(amount));
			BigDecimal b2 = new BigDecimal(Double.toString(balance));
			balance = b2.subtract(b1).doubleValue();
			accountingNo++;
			AccountingRecord ar = new AccountingRecord();
			ar.setAccountId(id);
			ar.setAccountingAmount(b1.multiply(new BigDecimal("-1")).doubleValue());
			ar.setAccountingNo(accountingNo);
			ar.setBalanceAfter(balance);
			ar.setSummary(summary);
			ar.setAccountingTime(withdrawTime);
			return ar;
		} else {
			throw new InsufficientBalanceException();
		}
	}

	/**
	 * 帐户取出。余额不足也强制取出，变成负债。
	 * 
	 * @param amount
	 * @param summary
	 * @return 记账记录
	 */
	public AccountingRecord withdrawAnyway(double amount, AccountingSummary summary, long withdrawTime) {
		BigDecimal b1 = new BigDecimal(Double.toString(amount));
		BigDecimal b2 = new BigDecimal(Double.toString(balance));
		balance = b2.subtract(b1).doubleValue();
		accountingNo++;
		AccountingRecord ar = new AccountingRecord();
		ar.setAccountId(id);
		ar.setAccountingAmount(b1.multiply(new BigDecimal("-1")).doubleValue());
		ar.setAccountingNo(accountingNo);
		ar.setBalanceAfter(balance);
		ar.setSummary(summary);
		ar.setAccountingTime(withdrawTime);
		return ar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public AccountOwner getOwner() {
		return owner;
	}

	public void setOwner(AccountOwner owner) {
		this.owner = owner;
	}

	public long getAccountingNo() {
		return accountingNo;
	}

	public void setAccountingNo(long accountingNo) {
		this.accountingNo = accountingNo;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
}
