package feedproviders;

import java.io.File;
import java.io.FilenameFilter;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import controllers.Controller;
import expertadvisors.ExpertAdvisor;
import helpers.Bar;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author paco@hernandezgomez.com
 */
@Singleton
public class SimulationFeedProvider extends FeedProvider {
	
	ExpertAdvisor expertAdvisor;
	Controller controller;
	
	int memorySymbol[];
	long memoryTime[];
	double memoryOpen[];
	double memoryLow[];
	double memoryHigh[];
	double memoryClose[];
	double memoryVolume[];
	
	@Inject
	public SimulationFeedProvider(ExpertAdvisor expertAdvisor, Controller controller) {
		this.expertAdvisor = expertAdvisor;
		this.controller = controller;
	}
	
	HashMap<String, Integer> symbolsMap = new HashMap<String, Integer>();
	String[] symbols;
	
	@Override
	public void loadData() {
		
		try {
			log.info("Loading historical data");
			
			File historyFolder = new File("history");
			
			File[] files = historyFolder.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					//return name.contains("USD") && name.endsWith(".hst");
					return name.contains("EURUSD") && name.endsWith(".hst");
					//return name.endsWith(".hst");
					//return !name.contains("JPY") && name.endsWith(".hst");
				}
			});
			
			int totalBars = 0;
			
			RandomAccessFile[] randomAccessFiles = new RandomAccessFile[files.length];
			MappedByteBuffer[] buffers = new MappedByteBuffer[files.length];
			
			symbols = new String[files.length];
			
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				totalBars += (file.length() - 148) / 44;
				
				randomAccessFiles[i] = new RandomAccessFile(file, "r");
				MappedByteBuffer buffer = randomAccessFiles[i].getChannel().map(FileChannel.MapMode.READ_ONLY, 0,  file.length());
				buffer.order(ByteOrder.LITTLE_ENDIAN);
				buffers[i] = buffer;
				
				// Discard header
				int version = buffer.getInt();
				
				String copyright = "";
				for (int j = 0; j < 64; j++) {
					copyright += (char) buffer.get();
				}
				
				String symbol = "";
				for (int j = 0; j < 12; j++) {
					symbol += (char) buffer.get();
				}
				symbols[i] = symbol.trim();
				
				symbolsMap.put(symbols[i], i);
				
				int period = buffer.getInt();
				int digits = buffer.getInt();
				
				Date creationTime = new Date();
				creationTime.setTime((long) buffer.getInt() * 1000);
				
				Date lastSynchronizationTime = new Date();
				lastSynchronizationTime.setTime((long) buffer.getInt() * 1000);
				
				for (int j = 0; j < 13; j++) {
					buffer.getInt();
				}
				
				log.info(symbols[i] + ", version: " + version + ", " + copyright + ", period: " + period + ", digits: " + digits + ", creation date: " + creationTime);
			}
			
			memorySymbol = new int[totalBars];
			memoryTime = new long[totalBars];
			memoryOpen = new double[totalBars];
			memoryLow = new double[totalBars];
			memoryHigh = new double[totalBars];
			memoryClose = new double[totalBars];
			memoryVolume = new double[totalBars];
			
			Bar currentTick = new Bar();
			int currentSymbol = 0;
			Bar ticks[] = new Bar[buffers.length];
			
			int counter = 0;
			
			long time1 = new Date().getTime();
			
			do {
				currentTick.time = 0;
				
				for (int i = 0; i < buffers.length; i++) {
					if (ticks[i] == null)
						ticks[i] = new Bar();
					if (ticks[i].time == 0 && buffers[i].position() < buffers[i].limit()) {
						ticks[i].symbol = symbols[i];
						ticks[i].time = (long) buffers[i].getLong() * 1000;
						ticks[i].open = buffers[i].getDouble();
						ticks[i].high = buffers[i].getDouble();
						ticks[i].low = buffers[i].getDouble();
						ticks[i].close = buffers[i].getDouble();
						ticks[i].volume = buffers[i].getLong();
						ticks[i].spread = buffers[i].getInt();
						ticks[i].realVolume = buffers[i].getLong();
					}
					
					if (ticks[i].time != 0 && (currentTick.time == 0 || ticks[i].time < currentTick.time)) {
						currentTick = ticks[i];
						currentSymbol = i;
					}
				}
				
				if (currentTick.time != 0) {
					
					memorySymbol[counter] = currentSymbol;
					memoryTime[counter] = currentTick.time;
					memoryOpen[counter] = currentTick.open;
					memoryLow[counter] = currentTick.low;
					memoryHigh[counter] = currentTick.high;
					memoryClose[counter] = currentTick.close;
					memoryVolume[counter] = currentTick.volume;
					
					if (counter % 1000000 == 0) {
						System.out.print(".");
					}
					
					counter++;
				}
			}
			while (currentTick.time != 0);
			
			long time2 = new Date().getTime();
			
			for (int i = 0; i < randomAccessFiles.length; i++) {
				randomAccessFiles[i].close();
			}
			
			System.out.println();
			
			log.info(counter + " bars loaded in " + (time2 - time1) + "ms");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void simulate() {
		
		log.info("Init");
		controller.reset(symbolsMap, symbols);
		expertAdvisor.init();
		
		Calendar startDate = Calendar.getInstance();
		startDate.clear();
		startDate.set(Calendar.DAY_OF_MONTH, 1);
		startDate.set(Calendar.MONTH, Calendar.JANUARY);
		startDate.set(Calendar.YEAR, 2010);
		
		Calendar endDate = Calendar.getInstance();
		
		for (int i = 0; i < memoryTime.length; i++) {
			
			if (memoryTime[i] > endDate.getTimeInMillis()) {
				break;
			}
			
			controller.newBar(memorySymbol[i], memoryTime[i], memoryOpen[i], memoryLow[i], memoryHigh[i], memoryClose[i], memoryVolume[i]);

			if (memoryTime[i] >= startDate.getTimeInMillis()) {
				expertAdvisor.start();
				controller.refresh();
			}
		}
		
		log.info("Deinit");
		expertAdvisor.deinit();
	}
}
