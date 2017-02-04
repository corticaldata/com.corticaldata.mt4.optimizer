package controllers;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author paco@hernandezgomez.com
 */
public abstract class Controller {
	protected static Logger log = Logger.getLogger("Controller");
	
	public abstract void reset(HashMap<String, Integer> symbolsMap, String[] symbols);
	
	public abstract void newBar(int symbol, long time, double open, double low, double high, double close, double volume);
	
	public abstract boolean refresh();
	
	public abstract long timeCurrent();
	
	public abstract boolean isConnected();
	
	public abstract boolean isTradeAllowed();
	
	public abstract boolean isTradeContextBusy();
	
	public abstract String symbol();
	
	public abstract int iBars(String symbol, int timeframe);
	
	public abstract double iHigh(String symbol, int timeframe, int shift);
	
	public abstract double iLow(String symbol, int timeframe, int shift);
	
	public abstract double iOpen(String symbol, int timeframe, int shift);
	
	public abstract double iClose(String symbol, int timeframe, int shift);
	
	public abstract double iVolume(String symbol, int timeframe, int shift);
	
	public abstract boolean refreshRates();
	
	public abstract double marketInfo(String symbol, int type);
	
	public abstract boolean orderSelect(int number, int selectionType);
	
	public abstract boolean orderSelect(int number, int selectionType, int ordersPool);
	
	public abstract double orderProfit();
	
	public abstract double orderSwap();
	
	public abstract double orderCommission();
	
	public abstract int ordersTotal();
	
	public abstract int ordersHistoryTotal();
	
	public abstract double accountBalance();
	
	public abstract double accountEquity();
	
	public abstract long orderOpenTime();
	
	public abstract int orderType();
	
	public abstract String orderSymbol();
	
	public abstract String orderComment();
	
	public abstract int orderMagicNumber();
	
	public abstract int orderTicket();
	
	public abstract double orderLots();
	
	public abstract double orderClosePrice();
	
	public abstract double orderOpenPrice();
	
	public abstract double orderStopLoss();
	
	public abstract double orderTakeProfit();
	
	public abstract boolean orderClose(int ticket, double lots, double closePrice, double maxSlippage);
	
	public abstract boolean orderModify(int ticket, double price, double stopLoss, double takeProfit, long expiration);
	
	public abstract int orderSend(
			String symbol, 
			int type, 
			double lots, 
			double openPrice, 
			double maxSlippage, 
			double stopLoss, 
			double takeProfit, 
			String comment, 
			int magicNumber);
}
