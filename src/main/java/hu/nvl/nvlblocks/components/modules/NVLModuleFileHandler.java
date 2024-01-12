package hu.nvl.nvlblocks.components.modules;
/* -------------------------------------------------------------------------------------
 * File by NVL 2018
 * For v1.10
 * 
 * v190403: Add folder providers, fix data folder function
 * v190402: Redesign folder creation with absolute path, Add PrepareNVLLogFile
 * v190221: Initial version
 * ------------------------------------------------------------------------------------- */
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import hu.nvl.nvlblocks.components.base_classes.NVLBaseClass;
import net.minecraftforge.fml.loading.FMLLoader;

public class NVLModuleFileHandler extends NVLBaseClass {
// ------------------------------------------ Create NVL Data Folder -----------------------------------------------	
	public static File prepareNVLLogFile(String Name) {
		String baseDir = FMLLoader.getGamePath().toString();
		System.out.println("NVLLogger: "+baseDir);
        File fld = new File(baseDir+"/logs/NVL");
        if (!fld.exists()) System.out.println("NVLLogger: MKDir "+fld.getAbsolutePath()+": "+fld.mkdir());
        File fh = new File(baseDir+"/logs/NVL/"+Name+".txt");
        if (!fh.exists()) {
			try {
				System.out.println("NVLLogger: CreateFile "+fh.getAbsolutePath()+": "+fh.createNewFile());
			} catch (IOException e) {
				System.out.println("NVLLogger: CreateFile exception: "+e);
			}
        }
     //   else System.out.println("NVLLogger: File exists: "+fh.getAbsolutePath());
		return fh;
	}
// -------------------------------------------- Load Text File
	public static ArrayList<String> loadTextFile(String fileName) throws IOException {
		ArrayList<String> r = new ArrayList<>();
		InputStream st = NVLModuleFileHandler.class.getResourceAsStream(fileName);
		if (st != null) {
			InputStreamReader isReader = new InputStreamReader(st, StandardCharsets.UTF_8);
			LineNumberReader reader = new LineNumberReader(isReader);
			String line;
			while ((line = reader.readLine()) != null) r.add(line);
			st.close();
		}
		return r;
	}
}
