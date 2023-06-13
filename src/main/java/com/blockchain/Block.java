package com.blockchain;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import com.blockchain.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Block {
	transient Logger logger = Logger.getLogger(getClass());

	public String hash;
	public String previousHash;
	private String data; // our data will be a simple message.
	private long timeStamp; // as number of milliseconds since 1/1/1970.
	private int nonce;
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>(); // our data will be a simple message.

	// Block Constructor.
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();

		this.hash = calculateHash(); // Making sure we do this after we set the other values.
	}

	public Block(String hash, String previousHash, String data) {
		this.hash = hash;
		this.previousHash = previousHash;
		this.data = data;
	}

	public Block(String data, String previousHash) {
		this.data = data;
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash(); // Making sure we do this after we set the other values.
	}

	public String calculateHash() {
		return StringUtility.applySha256(previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + data);
	}

	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
		while (!hash.substring(0, difficulty).equals(target)) {
			nonce++;
			hash = calculateHash();
		}
		logger.info("Block Mined!!! : " + hash);
	}

	// Add transactions to this block
	public boolean addTransaction(Transaction transaction) {
		// process transaction and check if valid, unless block is genesis block then
		// ignore.
		if (transaction == null)
			return false;
		if ((previousHash != "0")) {
			if ((transaction.processTransaction() != true)) {
				System.out.println("Transaction failed to process. Discarded.");
				return false;
			}
		}

		transactions.add(transaction);
		System.out.println("Transaction Successfully added to Block");
		return true;
	}

	@Override
	public String toString() {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
		Gson gson = builder.create();
		return gson.toJson(this);
	}
}