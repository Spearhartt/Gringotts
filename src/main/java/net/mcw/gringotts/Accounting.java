package net.mcw.gringotts;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Manages accounts.
 * 
 * @author jast
 *
 */
public class Accounting implements ConfigurationSerializable {
		
	private final Map<AccountHolder, Account> accounts;
	private final Map<Block, AccountChest> blockAccountChest = new HashMap<Block, AccountChest>();

	public Accounting() {
		this.accounts = new HashMap<AccountHolder, Account>();
	}
	
	/**
	 * Deserialization ctor.
	 * @param configMap
	 */
	@SuppressWarnings("unchecked")
	public Accounting(Map<String, Object> configMap) {
		
		Map<AccountHolder, Account> configAccounts = (Map<AccountHolder, Account>) configMap.get("accounts");
		this.accounts = configAccounts != null? configAccounts : new HashMap<AccountHolder, Account>();
		
		
		// reconstruct the block -> chest mapping from accounts
		for (Account account : accounts.values())
			for (AccountChest chest : account.getStorage())
				for (Block block : chest.getBlocks())
					blockAccountChest.put(block, chest);
	}

	/**
	 * Get the account associated with an account holder.
	 * @param owner account holder
	 * @return account associated with an account holder
	 */
	public Account getAccount(AccountHolder owner) {
		Account account = accounts.get(owner);
		if (account == null) {
			account = new Account(owner);
			accounts.put(owner,account);
		}
		
		return account;
	}
	
	/**
	 * Get the account chest associated with a block.
	 * @param block
	 * @return account chest associated with a block, or null if none
	 */
	public AccountChest chestAt(Block block) {
		return blockAccountChest.get(block);
	}
	
	/**
	 * Associa
	 * @param blocks
	 */
	public void addChest(AccountChest chest, Block... blocks) {
		for (Block block : blocks)
			blockAccountChest.put(block, chest);
	}

	public void removeChest(Block... blocks) {
		for (Block block : blocks)
			blockAccountChest.remove(block);
	}

	public Map<String, Object> serialize() {
		Map<String, Object> configMap = new HashMap<String, Object>(2);
		configMap.put("accounts", accounts);
		return configMap;
	}
	
}
