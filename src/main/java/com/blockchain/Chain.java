package com.blockchain;

import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

public class Chain {
	protected final Logger logger = Logger.getLogger(getClass());

	public static ArrayList<Block> blockchain = new ArrayList<Block>();
	public static int difficulty = 3;
	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();
	public static float minimumTransaction = 0.1f;
	public static Transaction genesisTransaction;
	public static Wallet walletA;
	public static Wallet walletB;

	public ArrayList<Block> transaction() {
		// add our blocks to the blockchain ArrayList:
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); // Setup Bouncey castle as a
																						// Security Provider

		// Create wallets:
		walletA = new Wallet();
		walletB = new Wallet();
		Wallet coinbase = new Wallet();

		// create genesis transaction, which sends 100 NoobCoin to walletA:
		genesisTransaction = new Transaction(coinbase.publicKey, walletA.publicKey, 100f, null);
		genesisTransaction.generateSignature(coinbase.privateKey); // manually sign the genesis transaction
		genesisTransaction.transactionId = "0"; // manually set the transaction id
		genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.recipient, genesisTransaction.value,
				genesisTransaction.transactionId)); // manually add the Transactions Output
		UTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0)); // its important to store
																							// our first transaction in
																							// the UTXOs list.

		logger.info("Creating and Mining Genesis block... ");
		Block genesis = new Block("0");
		genesis.addTransaction(genesisTransaction);
		addBlock(genesis);

		// testing
		Block block1 = new Block(genesis.hash);
		logger.info("WalletA's balance is: " + walletA.getBalance());
		logger.info("WalletA is Attempting to send funds (40) to WalletB...");
		block1.addTransaction(walletA.sendFunds(walletB.publicKey, 40f));
		addBlock(block1);
		logger.info("WalletA's balance is: " + walletA.getBalance());
		logger.info("WalletB's balance is: " + walletB.getBalance());

		Block block2 = new Block(block1.hash);
		logger.info("WalletA Attempting to send more funds (1000) than it has...");
		block2.addTransaction(walletA.sendFunds(walletB.publicKey, 1000f));
		addBlock(block2);
		logger.info("WalletA's balance is: " + walletA.getBalance());
		logger.info("WalletB's balance is: " + walletB.getBalance());

		Block block3 = new Block(block2.hash);
		logger.info("WalletB is Attempting to send funds (20) to WalletA...");
		block3.addTransaction(walletB.sendFunds(walletA.publicKey, 20));
		logger.info("WalletA's balance is: " + walletA.getBalance());
		logger.info("WalletB's balance is: " + walletB.getBalance());

		isChainValid();
		logger.info(blockchain.toString());
		return blockchain;

	}

	public Boolean isChainValid() {

		Block currentBlock;
		Block previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String, TransactionOutput> tempUTXOs = new HashMap<String, TransactionOutput>(); // a temporary working
																									// list of unspent
																									// transactions at a
																									// given block
																									// state.
		tempUTXOs.put(genesisTransaction.outputs.get(0).id, genesisTransaction.outputs.get(0));

		// loop through blockchain to check hashes:
		for (int i = 1; i < blockchain.size(); i++) {

			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				logger.info("#Current Hashes not equal");
				return false;
			}
			// compare previous hash and registered previous hash
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				logger.info("#Previous Hashes not equal");
				return false;
			}
			// check if hash is solved
			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				logger.info("#This block hasn't been mined");
				return false;
			}

			// loop thru blockchains transactions:
			TransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				Transaction currentTransaction = currentBlock.transactions.get(t);

				if (!currentTransaction.verifySignature()) {
					logger.info("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					logger.info("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (TransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if (tempOutput == null) {
						logger.info("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if (input.UTXO.value != tempOutput.value) {
						logger.info("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for (TransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.id, output);
				}

				if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					logger.info("#Transaction(" + t + ") output recipient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
					logger.info("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}

			}

		}
		logger.info("Blockchain is valid");
		return true;
	}

	public static void addBlock(Block newBlock) {
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
}