package fr.ankeraout.mcank;

/**
 * This enum contains the list of all the known blocks in the game.
 * @author Ankeraout
 *
 */
public enum Blocks {
	AIR(0),
	STONE(1),
	GRASS(2),
	DIRT(3),
	COBBLESTONE(4),
	PLANKS(5),
	SAPLING(6),
	BEDROCK(7),
	FLOWING_WATER(8),
	STILL_WATER(9),
	FLOWING_LAVA(10),
	STILL_LAVA(11),
	SAND(12),
	GRAVEL(13),
	ORE_GOLD(14),
	ORE_IRON(15),
	ORE_COAL(16),
	LOG(17),
	LEAVES(18),
	SPONGE(19),
	GLASS(20),
	CLOTH_RED(21),
	CLOTH_ORANGE(22),
	CLOTH_YELLOW(23),
	CLOTH_LIME(24),
	CLOTH_GREEN(25),
	CLOTH_TEAL(26),
	CLOTH_AQUA(27),
	CLOTH_CYAN(28),
	CLOTH_BLUE(29),
	CLOTH_INDIGO(30),
	CLOTH_VIOLET(31),
	CLOTH_MAGENTA(32),
	CLOTH_PINK(33),
	CLOTH_BLACK(34),
	CLOTH_GRAY(35),
	CLOTH_WHITE(36),
	DANDELION(37),
	ROSE(38),
	MUSHROOM_BROWN(39),
	MUSHROOM_RED(40),
	GOLD(41),
	IRON(42),
	SLAB_DOUBLE(43),
	SLAB(44),
	BRICKS(45),
	TNT(46),
	BOOKSHELF(47),
	COBBLESTONE_MOSSY(48),
	OBSIDIAN(49),
	SLAB_COBBLESTONE(50),
	ROPE(51),
	SANDSTONE(52),
	SNOW(53),
	FIRE(54),
	CLOTH_LIGHTPINK(55),
	CLOTH_FORESTGREEN(56),
	CLOTH_BROWN(57),
	CLOTH_DEEPBLUE(58),
	CLOTH_TURQUOISE(59),
	ICE(60),
	CERAMIC_TILE(61),
	MAGMA(62),
	PILLAR(63),
	CRATE(64),
	STONE_BRICK(65);
	
	/**
	 * The ID of the block in the game.
	 */
	private int blockId;
	
	/**
	 * Creates a new value in the {@link Blocks} enum, with the given block ID.
	 * @param blockId The ID of the block to create.
	 */
	private Blocks(int blockId) {
		this.blockId = blockId;
	}
	
	/**
	 * Returns the ID of the block corresponding to the enum value.
	 * @return The ID of the block
	 */
	public int getBlockId() {
		return this.blockId;
	}
}
