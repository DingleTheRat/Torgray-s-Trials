package net.dinglezz.torgrays_trials.data_generation;

import com.ususstudios.torgrays_datagen.DataGenerationException;
import com.ususstudios.torgrays_datagen.dataclasses.LootTableItem;
import net.dinglezz.torgrays_trials.object.OBJ_Coins;
import net.dinglezz.torgrays_trials.object.shield.OBJ_Shield_Amethyst;
import net.dinglezz.torgrays_trials.object.weapon.OBJ_Sword_Amethyst;

public class LootTableGenerator extends com.ususstudios.torgrays_datagen.generators.LootTableGenerator {
	
	@Override
	public void registerAll() throws DataGenerationException {
		registerMultiSelect("chests/amethyst", "Amethyst Chest", new LootTableItem[]{
				new LootTableItem(OBJ_Sword_Amethyst.class, 1, 1),
				new LootTableItem(OBJ_Shield_Amethyst.class, 1, 1),
				new LootTableItem(OBJ_Coins.class, 1, 4)
		});
	}
}
