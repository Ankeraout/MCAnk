package fr.ankeraout.mcank.world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * This class contains the code for loading worlds with the following magic
 * value: 0x0000000000000000 (64-bit hexadecimal representation of value 0).
 * 
 * @author Ankeraout
 *
 */
public class WorldLoader0000000000000000 implements WorldLoader {
	private static String readString(DataInputStream dis) throws IOException {
		int stringLength = dis.readInt();
		byte[] stringBuffer = new byte[stringLength];
		return new String(stringBuffer);
	}

	@Override
	public long getMagicValue() {
		return 0;
	}

	@Override
	public World loadWorld(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		DataInputStream dis = new DataInputStream(fis);

		String name = WorldLoader0000000000000000.readString(dis);
		String motd = WorldLoader0000000000000000.readString(dis);
		int width = dis.readInt();
		int height = dis.readInt();
		int depth = dis.readInt();
		float spawnX = dis.readFloat();
		float spawnY = dis.readFloat();
		float spawnZ = dis.readFloat();
		float spawnYaw = dis.readFloat();
		float spawnPitch = dis.readFloat();
		int buildPermission = dis.readInt();
		int visitPermission = dis.readInt();

		dis.close();

		return new World(name, motd, width, height, depth, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch,
				buildPermission, visitPermission, file);
	}

	@Override
	public void loadBlockData(World world) throws IOException {
		FileInputStream fis = new FileInputStream(world.getWorldFile());
		DataInputStream dis = new DataInputStream(fis);

		// Skip header
		WorldLoader0000000000000000.readString(dis);
		WorldLoader0000000000000000.readString(dis);
		dis.readInt();
		dis.readInt();
		dis.readInt();
		dis.readFloat();
		dis.readFloat();
		dis.readFloat();
		dis.readFloat();
		dis.readFloat();
		dis.readInt();
		dis.readInt();

		// Read world data
		world.getLock().lock();
		
		int volume = world.getVolume();

		for (int i = 0; i < volume; i++) {
			world.blockData[i] = dis.readInt();
		}
		
		world.getLock().unlock();

		dis.close();
	}

}
