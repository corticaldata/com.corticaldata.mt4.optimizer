package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import expertadvisors.ExpertAdvisor;
import feedproviders.FeedProvider;
import helpers.Bar;
import helpers.Trade;
import com.google.inject.Singleton;

/**
 * @author paco@hernandezgomez.com
 */
@Singleton
public class VirtualController extends Controller {
	
	FeedProvider feedProvider;
	
	double accountBalance;
	double accountEquity;
	
	int maxPeriods;
	
	String symbol;
	
	/**
	 * First dimension for symbol, second dimension por timeframe (7)
	 */
	ArrayList<Bar>[][] bars;
	
	ArrayList<Trade> openTrades = null;
	ArrayList<Trade> closedTrades = null;
	
	Trade selectedTrade = null;
	
	int currentTicket = 1;
	
	// Dimension for symbol
	double open[];
	double low[];
	double high[];
	double close[];
	double volume[];
	
	HashMap<String, Integer> symbolsMap;
	String[] symbols;
	
	double pointMultiplier;
	long currentTime;
	
	double maxEquity;
	double maxDrawdownPercent;
	
	@Override
	public void reset(HashMap<String, Integer> symbolsMap, String[] symbols, double pointMultiplier) {
		
		this.symbolsMap = symbolsMap;
		this.symbols = symbols;
		
		accountBalance = 10000;
		accountEquity = accountBalance;
		
		maxEquity = accountEquity;
		maxDrawdownPercent = 0;
		
		maxPeriods = 120;
		
		bars = new ArrayList[symbolsMap.size()][7];
		
		for (int i = 0; i < symbolsMap.size(); i++) {
			bars[i][ExpertAdvisor.PERIOD_M1] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_M5] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_M15] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_M30] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_H1] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_H4] = new ArrayList<Bar>();
			bars[i][ExpertAdvisor.PERIOD_D1] = new ArrayList<Bar>();
		}
		
		openTrades = new ArrayList<Trade>();
		closedTrades = new ArrayList<Trade>();
		
		selectedTrade = null;
		
		currentTicket = 1;
		
		// Dimension for symbol
		open = new double[symbolsMap.size()];
		low = new double[symbolsMap.size()];
		high = new double[symbolsMap.size()];
		close = new double[symbolsMap.size()];
		volume = new double[symbolsMap.size()];
		
		symbolsMap = new HashMap<String, Integer>();
		
		this.pointMultiplier = pointMultiplier;
		currentTime = 0;
	}
	
	@Override
	public long timeCurrent() {
		return currentTime;
	}
	
	@Override
	public boolean isConnected() {
		return true;
	}
	
	@Override
	public boolean isTradeAllowed() {
		return true;
	}
	
	@Override
	public boolean isTradeContextBusy() {
		return false;
	}
	
	@Override
	public String symbol() {
		return symbol;
	}
	
	@Override
	public int iBars(String symbol, int timeframe) {
		if (symbolsMap.get(symbol) == null)
			return 0;
		return bars[symbolsMap.get(symbol)][timeframe].size();
	}
	
	@Override
	public double iHigh(String symbol, int timeframe, int shift) {
		return bars[symbolsMap.get(symbol)][timeframe].get(shift).high;
	}
	
	@Override
	public double iLow(String symbol, int timeframe, int shift) {
		return bars[symbolsMap.get(symbol)][timeframe].get(shift).low;
	}
	
	@Override
	public double iOpen(String symbol, int timeframe, int shift) {
		return bars[symbolsMap.get(symbol)][timeframe].get(shift).open;
	}
	
	@Override
	public double iClose(String symbol, int timeframe, int shift) {
		return bars[symbolsMap.get(symbol)][timeframe].get(shift).close;
	}
	
	@Override
	public double iVolume(String symbol, int timeframe, int shift) {
		return bars[symbolsMap.get(symbol)][timeframe].get(shift).volume;
	}
	
	@Override
	public boolean refreshRates() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public double marketInfo(String symbol, int type) {
		double value = 0;
		
		int symbolPosition = symbolsMap.get(symbol);
		
		switch (type) {
			case ExpertAdvisor.MODE_TICKVALUE:
				value = 1;
				break;
				
			case ExpertAdvisor.MODE_MINLOT:
				value = 0.01;
				break;
				
			case ExpertAdvisor.MODE_MAXLOT:
				value = 999999;
				break;
				
			case ExpertAdvisor.MODE_LOTSIZE:
				value = 100000;
				break;

			case ExpertAdvisor.MODE_LOTSTEP:
				value = 0.01;
				break;
				
			case ExpertAdvisor.MODE_POINT:
				if (symbol.contains("JPY")) {
					value = 0.001;
				}
				else {
					value = 0.00001;
				}
				break;

			case ExpertAdvisor.MODE_DIGITS:
				if (symbol.contains("JPY")) {
					value = 3;
				}
				else {
					value = 5;
				}
				break;
				
			case ExpertAdvisor.MODE_BID:
				value = open[symbolPosition];
				break;
				
			case ExpertAdvisor.MODE_ASK:
				value = open[symbolPosition] + marketInfo(symbol, ExpertAdvisor.MODE_SPREAD) * marketInfo(symbol, ExpertAdvisor.MODE_POINT);
				break;
				
			case ExpertAdvisor.MODE_SPREAD:
				if (symbol.equals("EURUSD")) {
					value = 16;
				}
				else if (symbol.equals("USDJPY")) {
					value = 20;
				}
				else if (symbol.equals("EURGBP")) {
					value = 21;
				}
				else if (symbol.equals("GBPUSD")) {
					value = 36; //2.5;
				}
				else if (symbol.equals("USDCHF")) {
					value = 26;
				}
				else if (symbol.equals("AUDUSD")) {
					value = 77;
				}
				else if (symbol.equals("EURJPY")) {
					value = 30;
				}
				else if (symbol.equals("EURCHF")) {
					value = 30;
				}
				else if (symbol.equals("USDCAD")) {
					value = 31;
				}
				else if (symbol.equals("AUDNZD")) {
					value = 54;
				}
				else if (symbol.equals("USDSGD")) {
					value = 56;
				}
				else if (symbol.equals("GBPJPY")) {
					value = 62;
				}
				else if (symbol.equals("GBPCHF")) {
					value = 68;
				}
				else if (symbol.equals("AUDCAD")) {
					value = 76;
				}
				else if (symbol.equals("EURAUD")) {
					value = 77;
				}
				else if (symbol.equals("EURCAD")) {
					value = 78;
				}
				else if (symbol.equals("XAUUSD")) {
					value = 525;
				}
				else {
					value = 30;
				}
				break;
		}
		return value;
	}
	
	@Override
	public boolean orderSelect(int number, int selectionType) {
		return orderSelect(number, selectionType, ExpertAdvisor.MODE_TRADES);
	}
	
	@Override
	public boolean orderSelect(int number, int selectionType, int ordersPool) {
		ArrayList<Trade> trades = null;
		
		if (ordersPool == ExpertAdvisor.MODE_TRADES)
			trades = openTrades;
		else if (ordersPool == ExpertAdvisor.MODE_HISTORY)
			trades = closedTrades;
		
		if (trades != null) {
			if (selectionType == ExpertAdvisor.SELECT_BY_POS) {
				if (number >= 0 && number < trades.size()) {
					selectedTrade = trades.get(number);
					return true;
				}
			}
			else if (selectionType == ExpertAdvisor.SELECT_BY_TICKET) {
				for (int i = 0; i < trades.size(); i++) {
					if (trades.get(i).ticket == number) {
						selectedTrade = trades.get(i);
						return true;
					}
				}
			}
		}
		
		selectedTrade = null;
		return false;
	}
	
	@Override
	public double orderProfit() {
		if (selectedTrade != null) {
			if (selectedTrade.type == ExpertAdvisor.OP_BUY) {
				double profit = selectedTrade.lots * marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_LOTSIZE) * (selectedTrade.closePrice - selectedTrade.openPrice) / marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_TICKVALUE);
				return profit;
			}
			else if (selectedTrade.type == ExpertAdvisor.OP_SELL) {
				double profit = selectedTrade.lots * marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_LOTSIZE) * (selectedTrade.openPrice - selectedTrade.closePrice) / marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_TICKVALUE);
				return profit;
			}
		}
		return 0;
	}
	
	@Override
	public double orderSwap() {
		return 0;
	}
	
	@Override
	public double orderCommission() {
		return 0;
	}
	
	@Override
	public int ordersTotal() {
		return openTrades.size();
	}
	
	@Override
	public int ordersHistoryTotal() {
		return closedTrades.size();
	}
	
	@Override
	public double accountBalance() {
		return accountBalance;
	}
	
	@Override
	public double accountEquity() {
		return accountEquity;
	}
	
	@Override
	public long orderOpenTime() {
		if (selectedTrade != null) {
			return selectedTrade.openTime;
		}
		return (-1);
	}
	
	@Override
	public int orderType() {
		if (selectedTrade != null) {
			return selectedTrade.type;
		}
		return (-1);
	}
	
	@Override
	public String orderSymbol() {
		if (selectedTrade != null) {
			return selectedTrade.symbol;
		}
		return null;
	}
	
	@Override
	public String orderComment() {
		if (selectedTrade != null) {
			return selectedTrade.comment;
		}
		return null;
	}
	
	@Override
	public int orderMagicNumber() {
		if (selectedTrade != null) {
			return selectedTrade.magicNumber;
		}
		return (-1);
	}
	
	@Override
	public int orderTicket() {
		if (selectedTrade != null) {
			return selectedTrade.ticket;
		}
		return (-1);
	}
	
	@Override
	public double orderLots() {
		if (selectedTrade != null) {
			return selectedTrade.lots;
		}
		return (-1);
	}
	
	@Override
	public double orderClosePrice() {
		if (selectedTrade != null) {
			return selectedTrade.closePrice;
		}
		return (-1);
	}
	
	@Override
	public double orderOpenPrice() {
		if (selectedTrade != null) {
			return selectedTrade.openPrice;
		}
		return (-1);
	}
	
	@Override
	public double orderStopLoss() {
		if (selectedTrade != null) {
			return selectedTrade.stopLoss;
		}
		return (-1);
	}
	
	@Override
	public double orderTakeProfit() {
		if (selectedTrade != null) {
			return selectedTrade.takeProfit;
		}
		return (-1);
	}
	
	@Override
	public boolean orderClose(int ticket, double lots, double closePrice, double maxSlippage) {
		for (int i = 0; i < openTrades.size(); i++) {
			if (openTrades.get(i).ticket == ticket) {
				selectedTrade = openTrades.get(i);
				selectedTrade.closePrice = closePrice;
				selectedTrade.closeTime = currentTime + 60 * 1000;
				openTrades.remove(i);
				closedTrades.add(selectedTrade);
				accountBalance += orderProfit() + orderSwap() + orderCommission();
				log.info(selectedTrade.symbol + ": CLOSE (" + selectedTrade.ticket + ") " + new Date(selectedTrade.closeTime) + ": " + (selectedTrade.type == 0 ? "buy" : "sell") + ", " + selectedTrade.lots + ", " + selectedTrade.closePrice + ", " + selectedTrade.stopLoss + ", " + selectedTrade.takeProfit);
				log.info("Profit: " + orderProfit() + ", Balance: " + accountBalance);
				log.info("Max. equity: " + maxEquity + ", Max. drawdown: " + maxDrawdownPercent + "%");
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean orderModify(int ticket, double price, double stopLoss, double takeProfit, long expiration) {
		for (int i = 0; i < openTrades.size(); i++) {
			if (openTrades.get(i).ticket == ticket) {
				selectedTrade = openTrades.get(i);
				selectedTrade.stopLoss = stopLoss;
				selectedTrade.takeProfit = takeProfit;
				log.info(selectedTrade.symbol + ": MODIFY (" + selectedTrade.ticket + ") " + new Date(currentTime + 60 * 1000) + ": " + (selectedTrade.type == 0 ? "buy" : "sell") + ", " + selectedTrade.lots + ", " + selectedTrade.closePrice + ", " + selectedTrade.stopLoss + ", " + selectedTrade.takeProfit);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int orderSend(String symbol, int type, double lots,
			double openPrice, double maxSlippage, double stopLoss,
			double takeProfit, String comment, int magicNumber) {
		
		selectedTrade = new Trade();
		selectedTrade.magicNumber = magicNumber;
		selectedTrade.symbol = symbol;
		selectedTrade.type = type;
		selectedTrade.lots = lots;
		selectedTrade.openPrice = openPrice;
		if (type == ExpertAdvisor.OP_BUY) {
			selectedTrade.closePrice = close[symbolsMap.get(symbol)];
		}
		else {
			selectedTrade.closePrice = close[symbolsMap.get(symbol)] + marketInfo(symbol, ExpertAdvisor.MODE_SPREAD) * marketInfo(symbol, ExpertAdvisor.MODE_POINT) * pointMultiplier;
		}
		selectedTrade.openTime = currentTime;
		selectedTrade.stopLoss = stopLoss;
		selectedTrade.takeProfit = takeProfit;
		selectedTrade.comment = comment;
		selectedTrade.ticket = currentTicket;
		currentTicket++;
		openTrades.add(selectedTrade);

		log.info(selectedTrade.symbol + ": OPEN (" + selectedTrade.ticket + ") " + new Date(selectedTrade.openTime) + ": " + (selectedTrade.type == 0 ? "buy" : "sell") + ", " + selectedTrade.lots + ", " + selectedTrade.openPrice + ", " + selectedTrade.stopLoss + ", " + selectedTrade.takeProfit);
		
		return selectedTrade.ticket;
	}

	void calculateTimeframe(int symbol, int timeframe, long currentTime, double open, double low, double high, double close, double volume) {
		
		long timeframeTimeInMillis = 0;
		switch (timeframe) {
			case 0:
				timeframeTimeInMillis = 60 * 1000;
				break;
				
			case 1:
				timeframeTimeInMillis = 5 * 60 * 1000;
				break;
				
			case 2:
				timeframeTimeInMillis = 15 * 60 * 1000;
				break;
				
			case 3:
				timeframeTimeInMillis = 30 * 60 * 1000;
				break;
				
			case 4:
				timeframeTimeInMillis = 60 * 60 * 1000;
				break;
				
			case 5:
				timeframeTimeInMillis = 4 * 60 * 60 * 1000;
				break;
				
			case 6:
				timeframeTimeInMillis = 24 * 60 * 60 * 1000;
				break;
		}
		
		if (bars[symbol][timeframe].size() == 0 || currentTime - bars[symbol][timeframe].get(0).time >= timeframeTimeInMillis) {
			Bar currentBar = new Bar();
			currentBar.time = currentTime - currentTime % timeframeTimeInMillis;
			currentBar.open = open;
			currentBar.low = low;
			currentBar.high = high;
			currentBar.close = close;
			currentBar.volume = 0;
			bars[symbol][timeframe].add(0, currentBar);
			
			while (bars[symbol][timeframe].size() > maxPeriods) {
				bars[symbol][timeframe].remove(bars[symbol][timeframe].size() - 1);
			}
		}
		
		Bar currentBar = bars[symbol][timeframe].get(0);
		currentBar.close = close;
		if (low < currentBar.low)
			currentBar.low = low;
		
		if (high > currentBar.high)
			currentBar.high = high;
		
		currentBar.volume += volume;
	}
	
	@Override
	public void newBar(int symbol, long time, double open, double low,
			double high, double close, double volume) {
		
		this.symbol = symbols[symbol];
		
		this.open[symbol] = open;
		this.low[symbol] = low;
		this.high[symbol] = high;
		this.close[symbol] = close;
		this.volume[symbol] = volume;
		
		currentTime = time;
		
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_M1, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_M5, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_M15, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_M30, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_H1, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_H4, currentTime, open, low, high, close, volume);
		calculateTimeframe(symbol, ExpertAdvisor.PERIOD_D1, currentTime, open, low, high, close, volume);
	}
	
	@Override
	public void refresh() {
		accountEquity = accountBalance;
		
		Trade temp = selectedTrade;
		
		for (int i = 0; i < openTrades.size(); i++) {
			selectedTrade = openTrades.get(i);
			if (selectedTrade.type == ExpertAdvisor.OP_BUY) {
				double tp = (selectedTrade.takeProfit == 0 || selectedTrade.takeProfit == (-1) ? 100000 : selectedTrade.takeProfit);
				if (high[symbolsMap.get(selectedTrade.symbol)] >= tp)
					selectedTrade.closePrice = tp;
				else if (low[symbolsMap.get(selectedTrade.symbol)] <= selectedTrade.stopLoss)
					selectedTrade.closePrice = selectedTrade.stopLoss;
				else
					selectedTrade.closePrice = close[symbolsMap.get(selectedTrade.symbol)];
			}
			else if (selectedTrade.type == ExpertAdvisor.OP_SELL) {
				double sl = (selectedTrade.stopLoss == 0 || selectedTrade.stopLoss == (-1) ? 100000 : selectedTrade.stopLoss);
				if (low[symbolsMap.get(selectedTrade.symbol)] + marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_SPREAD) * marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_POINT) * pointMultiplier <= selectedTrade.takeProfit)
					selectedTrade.closePrice = selectedTrade.takeProfit;
				else if (high[symbolsMap.get(selectedTrade.symbol)] + marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_SPREAD) * marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_POINT) * pointMultiplier >= sl)
					selectedTrade.closePrice = sl;
				else
					selectedTrade.closePrice = close[symbolsMap.get(selectedTrade.symbol)] + marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_SPREAD) * marketInfo(selectedTrade.symbol, ExpertAdvisor.MODE_POINT) * pointMultiplier;
			}
			
			accountEquity += orderProfit() + orderSwap() + orderCommission();
			
			if (selectedTrade.closePrice == selectedTrade.takeProfit || selectedTrade.closePrice == selectedTrade.stopLoss) {
				selectedTrade.closeTime = currentTime + 60 * 1000;
				openTrades.remove(i);
				closedTrades.add(selectedTrade);
				accountBalance += orderProfit() + orderSwap() + orderCommission();
				log.info(selectedTrade.symbol + ": CLOSE (TP/SL) (" + selectedTrade.ticket + ") " + new Date(selectedTrade.closeTime) + ": " + (selectedTrade.type == 0 ? "buy" : "sell") + ", " + selectedTrade.lots + ", " + selectedTrade.closePrice + ", " + selectedTrade.stopLoss + ", " + selectedTrade.takeProfit);
				log.info("Profit: " + orderProfit() + ", Balance: " + accountBalance);
				log.info("Max. equity: " + maxEquity + ", Max. drawdown: " + maxDrawdownPercent + "%");
			}
		}
		
		if (accountEquity > maxEquity) {
			maxEquity = accountEquity;
		}
		
		double drawdownPercent = (maxEquity - accountEquity) * 100 / maxEquity;
		if (drawdownPercent > maxDrawdownPercent) {
			maxDrawdownPercent = drawdownPercent;
		}
		
		selectedTrade = temp;
		
		if (accountEquity <= 0) {
			System.out.println("SIN PASTA!!! ;-)");
			System.exit(0);
		}
	}
}
