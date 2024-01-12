package hu.nvl.nvlblocks.components.modules;
import hu.nvl.nvlblocks.NVLBlocks;
import net.minecraftforge.fml.loading.FMLEnvironment;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class NVLModuleLogger {
	private static File cfh,sfh = null;
	private static String MODID = "";
	private static final Boolean EnableLogging = true;

	public NVLModuleLogger(String newMODID) {
		MODID = newMODID;
	}
	public void logLine(String module, String message) {
		if (EnableLogging) {
			String part;
			if (FMLEnvironment.dist.isDedicatedServer()) part = "D";
			else part = "C";
			if (part.equals("C")) {
				if (cfh == null) cfh = NVLModuleFileHandler.prepareNVLLogFile(MODID + "_Client");
				if (cfh != null) feedIntoLog(cfh,module+": "+message);
				else System.out.println("C "+MODID+":"+module+": "+message);
			} else {
				if (sfh == null) sfh = NVLModuleFileHandler.prepareNVLLogFile(MODID + "_Server_"+part);
				if (sfh != null) feedIntoLog(sfh,module+": "+message);
				else System.out.println(part + " "+MODID+":"+module+": "+message);
			}
		}
	}

	private void feedIntoLog(File lfh,String Message) {
		NVLBlocks.LOGGER.debug("NVLLogger:" + lfh.getName()+":"+Message);
		try {
			PrintWriter out = new PrintWriter(new FileWriter(lfh, true));
		    out.println(Message);
		    out.close();
		} catch (IOException e) {
			System.out.println("NVLLogger: IO Exception:"+e);
		}
	}
	
}
