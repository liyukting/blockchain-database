package com.blockchain;

import java.util.ArrayList;

import org.apache.log4j.Logger;

public class FirstChain {
	protected final Logger logger = Logger.getLogger(getClass());

	public static ArrayList<Block> blockChain = new ArrayList();

	public static int difficulty = 5;

	public ArrayList<Block> test() {
		blockChain.add(new Block("first", "0"));
		logger.info("Trying to Mine block 1... ");
		blockChain.get(0).mineBlock(difficulty);

		blockChain.add(new Block("second", blockChain.get(blockChain.size() - 1).hash));
		logger.info("Trying to Mine block 2... ");
		blockChain.get(1).mineBlock(difficulty);

		blockChain.add(new Block("third", blockChain.get(blockChain.size() - 1).hash));
		logger.info("Trying to Mine block 3... ");

		blockChain.get(2).mineBlock(difficulty);

		logger.info("Blockchain is Valid: " + isChainValid());
		logger.info(blockChain);
		return blockChain;
	}

	public ArrayList<Block> appendBlock(String data) {
		// Validate
		if (!isChainValid()) {
			logger.info("Blockchain is invalid!");
			return null;
		}
		if (blockChain.size() == 0)
			blockChain.add(new Block(data, "0"));
		else
			blockChain.add(new Block(data, blockChain.get(blockChain.size() - 1).hash));
		// Mine block
		logger.info("Trying to Mine block " + blockChain.size() + "... ");
		blockChain.get(blockChain.size()-1).mineBlock(difficulty);
		// Validate
		if (!isChainValid()) {
			blockChain.remove(blockChain.size() - 1);
			logger.info("Blockchain is invalid!");
			return null;
		}
		logger.info(blockChain);
		return blockChain;
	}

	public Boolean isChainValid() {

		Block currentBlock;
		Block previousBlock;
		boolean flag = true;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');

		for (int i = 1; i < blockChain.size(); i++) {
			currentBlock = blockChain.get(i);
			previousBlock = blockChain.get(i - 1);
			if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
				logger.info("Current Hashes not equal");
				flag = false;
			}
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				logger.info("Previous Hashes not equal");
				flag = false;
			}

			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				logger.info("This block hasn't been mined");
				flag = false;
			}
		}

		return flag;
	}
}